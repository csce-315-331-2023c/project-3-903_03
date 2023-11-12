import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function PATCH( {request} ) {
    let connection = await pool.connect();
    const {id, name, price} = await request.json();
    let sql = 
        `UPDATE _menu_item \
        SET name = $2, price = $3 \
        WHERE menu_item_id = $1`;
    try {
        const result = await connection.query(sql, [id, name, price]);
        console.log(result);
        const updated_row = result.rows[0];
        return json({success: true, data: updated_row});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}