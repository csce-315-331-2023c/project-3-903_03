import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function PATCH( {request} ) {
    let connection = await pool.connect();
    const {ingredients} = await request.json();

    try {
        for (const ingredient of ingredients) {
            const sql = `UPDATE _ingredient \
                SET current_qty = ${ingredient.needed_qty} \
                WHERE ingredient_id = ${ingredient.ingredient_id};`;
            
            await connection.query(sql);         
        }
        return json({success: true, data: null});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}