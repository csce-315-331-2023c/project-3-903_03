import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function DELETE( {request} ) {
    let connection = await pool.connect();
    const {ingredient_id} = await request.json();
    let sql =
        `DELETE FROM _ingredient \
        WHERE ingredient_id = ${ingredient_id};`;
    try {
        await connection.query(sql);
        return json({success: true});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}