import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET({url}) {
    let connection = await pool.connect();
    const from_date = url.searchParams.get('from_date');
    const to_date = url.searchParams.get('to_date');
    // console.log(url);
    // console.log(from_date);
    // console.log(to_date);
    let sql =
        `SELECT mi1.name AS name1, mi2.name AS name2, COUNT(*) AS frequency
        FROM
            (SELECT co.customer_order_id, mi.menu_item_id, mi.name
            FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi
            WHERE co.order_date >= $1 AND
                co.order_date <= $2 AND
                co.customer_order_id = oi.customer_order_id AND
                oi.menu_item_id = mi.menu_item_id) mi1
        JOIN
            (SELECT co.customer_order_id, mi.menu_item_id, mi.name
            FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi
            WHERE co.order_date >= $1 AND
                co.order_date <= $2 AND
                co.customer_order_id = oi.customer_order_id AND
                oi.menu_item_id = mi.menu_item_id) mi2
        ON (mi1.menu_item_id < mi2.menu_item_id AND
            mi1.customer_order_id = mi2.customer_order_id)
        GROUP BY name1, name2
        ORDER BY frequency DESC;`

    // console.log(sql);
    try {
        const result = await connection.query(sql, [from_date, to_date]);
        // console.log(result.rows);
        return json(result.rows);
    } finally {
        connection.release();
    }
}