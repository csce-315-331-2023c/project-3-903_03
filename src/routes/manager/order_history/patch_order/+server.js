import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function PATCH( {request} ) {
    let connection = await pool.connect();
    const {customer_order_id, status_id} = await request.json();
    let sql = 
        `UPDATE _customer_order \
        SET status_id=${status_id}\
        WHERE customer_order_id = ${customer_order_id};`;
    try {
        const result = await connection.query(sql);
        
        const updated_row = result.rows[0];
        return json({success: true, data: updated_row});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}