import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET({url}) {
    let connection = await pool.connect();
    const id = url.searchParams.get('id');
    let sql = 
        `SELECT co.customer_order_id, co.cost, co.status_id, \
         TO_CHAR(co.order_date, 'YYYY-MM-DD') AS order_date, \
         co.order_time AS order_time \
         FROM _customer_order AS co \
         WHERE co.id = '${id}'; `
    try {
        let result = await connection.query(sql);
        return json({success: true, customer_orders: result.rows});
    } catch {
        return json({success: false, error: 'Orders were not found'});
    } finally {
        connection.release();
    }
}