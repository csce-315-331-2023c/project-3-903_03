import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET({url}) {
    let connection = await pool.connect();
    const customer_order_id = url.searchParams.get('customer_order_id');
    let sql = 
        `SELECT mi.menu_item_id, mi.name, oi.amount \
         FROM _ordered_item AS oi, _menu_item AS mi \
         WHERE oi.customer_order_id = '${customer_order_id}' \
         AND oi.menu_item_id = mi.menu_item_id; `
    try {
        const result = await connection.query(sql);
        return json({success: true, ordered_items: result.rows});
    } catch {
        return json({success: false, error: 'ordered items were not found'});
    } finally {
        connection.release();
    }
}