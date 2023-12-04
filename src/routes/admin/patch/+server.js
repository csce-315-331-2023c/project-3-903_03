import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function PATCH({request}) {
    let connection = await pool.connect();
    const {id, name, username, password, category_id} = await request.json();
    let sql = 
        `UPDATE _user \
        SET name='${name}', \
            username='${username}', \
            password='${password}', \
            category_id='${category_id}' \
        WHERE id = ${id};`;
    try {
        const result = await connection.query(sql);
        const updated_row = result.rows[0];
        return json({success: true, data: updated_row});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to update row'});
    } finally {
        connection.release();
    }
}