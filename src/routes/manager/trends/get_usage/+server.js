import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET({url}) {
    let connection = await pool.connect();
    const from_date = Number(url.searchParams.get('from_date'));
    const to_date = Number(url.searchParams.get('to_date'));
    let sql = "SELECT i.name AS name, SUM(oi.amount * ii.quantity) AS amount \
        FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi, _item_ingredient AS ii, _ingredient AS i \
        WHERE co.order_date >= $1 AND \
            co.order_date <= $2 AND \
            co.customer_order_id = oi.customer_order_id AND \
            oi.menu_item_id = mi.menu_item_id AND \
            mi.menu_item_id = ii.menu_item_id  \
            ii.ingredient_id = i.ingredient_id \
        GROUP BY i.name \
        ORDER BY amount DESC;"
    try {
        const result = await connection.query(sql, [from_date], [to_date]);
        // console.log(result.rows);
        return json(result.rows);
    } finally {
        connection.release();
    }
}