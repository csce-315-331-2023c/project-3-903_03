import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST( {request} ) {    
    let connection = await pool.connect();
    const { id, ingredients } = await request.json();
    try {
        for (const ingredient of ingredients) {
            const query = `INSERT INTO _item_ingredient (ingredient_id, menu_item_id, quantity) \
                            VALUES (${ingredient.ingredient_id}, ${id}, 1);`;

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