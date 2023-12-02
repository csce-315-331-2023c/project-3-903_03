import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST( {request} ) {
    let connection = await pool.connect();
    const { id, restock_date, cost } = await request.json();
    let sql = 
        `WITH max_id AS (SELECT COALESCE(MAX(restock_order_id), 0) + 1 AS new_id FROM _restock_order) \
         INSERT INTO _restock_order(restock_order_id, id, restock_date, cost) \
         SELECT new_id, ${id}, ${restock_date}, '$${cost}' FROM max_id \
         RETURNING restock_order_id`;
    try {
        const result = await connection.query(sql);
        const r_id = result.rows[0].restock_order_id;
        return json({success: true, id: r_id});
    } catch {
        console.error('Error updating post_restock: ');
        return json({success: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}