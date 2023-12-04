import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

async function can_complete_order(customer_order_id) {
    let connection = await pool.connect();
    let sql = 
        `WITH temp AS (
            SELECT ii.ingredient_id, (SUM(oi.amount * ii.quantity) <= i.current_qty) AS available 
            FROM _ordered_item AS oi, _item_ingredient AS ii, _ingredient AS i 
            WHERE oi.customer_order_id = ${customer_order_id} AND 
                  oi.menu_item_id = ii.menu_item_id AND
                  ii.ingredient_id = i.ingredient_id
            GROUP BY ii.ingredient_id, i.current_qty
        )
        SELECT COUNT(*) as count
        FROM temp
        WHERE available = false;`;
    try {
        const result = await connection.query(sql);
        if (result) {
            const count = Number(result.rows[0].count);
            return (count === 0);    
        }
    } finally {
        connection.release();
        return false;
    }
}

async function update_quantaties(customer_order_id) {
    let connection = await pool.connect();
    let sql = 
        `SELECT ii.ingredient_id AS ingredient_id, SUM(oi.amount * ii.quantity) AS used_qty 
         FROM _ordered_item AS oi, _item_ingredient AS ii, _ingredient AS i 
         WHERE oi.customer_order_id = ${customer_order_id} AND 
               oi.menu_item_id = ii.menu_item_id AND
               ii.ingredient_id = i.ingredient_id
         GROUP BY ii.ingredient_id;`;

    try {
        const result = await connection.query(sql);
        for (let row of result.rows) {
            let ingredient_id = row.ingredient_id;
            let used_qty = row.used_qty;
            let usql =    
                `UPDATE _ingredient
                SET current_qty = current_qty - ${used_qty}
                WHERE ingredient_id = ${ingredient_id};`;
            const result = await connection.query(usql);
        }
    } finally {
        connection.release();
    }
}

export async function PATCH( {request} ) {
    let connection = await pool.connect();
    const {customer_order_id} = await request.json();
    const output = await can_complete_order(customer_order_id);;
    if (output) {
        update_quantaties(customer_order_id);
        return json({success: true});
    }
    return json({success: false});
 }