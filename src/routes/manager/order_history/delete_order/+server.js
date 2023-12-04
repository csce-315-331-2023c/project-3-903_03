import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function DELETE( {request} ) {
    let connection = await pool.connect();
    const {customer_order_id} = await request.json();
    let oi_sql =
        `DELETE FROM _ordered_item \
        WHERE customer_order_id = ${customer_order_id};`;
    let co_sql = 
        `DELETE FROM _customer_order \
        WHERE customer_order_id = ${customer_order_id};`;
    try {
        await connection.query(oi_sql);
        await connection.query(co_sql);
        return json({success: true});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}