import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST( {request} ) {    
    let connection = await pool.connect();
    const { customer_order_id, items } = await request.json();
    try {
        for (const item of items) {
            const query = `INSERT INTO _ordered_item (customer_order_id, menu_item_id, amount) \
                            VALUES (${customer_order_id}, ${item.menu_item_id},  ${item.amount});`;

            const result = await connection.query(query);
        }
        return json({success: true, data: null});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}