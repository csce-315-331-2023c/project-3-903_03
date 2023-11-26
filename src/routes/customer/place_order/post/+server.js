import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST( {request} ) {
    let connection = await pool.connect();
    const { id, cost, order_date, order_time } = await request.json();
    let sql = 
        `WITH max_id AS (SELECT COALESCE(MAX(customer_order_id), 0) + 1 AS new_id FROM _customer_order) \
         INSERT INTO _customer_order(customer_order_id, id, cost, order_date, order_time) \
         SELECT new_id, '${id}', '${cost}', '${order_date}', '${order_time}' FROM max_id \
         RETURNING customer_order_id;`;
    try {
        const result = await connection.query(sql);
        const id = result.rows[0].customer_order_id;
        return json({success: true, id: id});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}