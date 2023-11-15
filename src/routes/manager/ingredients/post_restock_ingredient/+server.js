import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST( {request} ) {    
    let connection = await pool.connect();
    const { r_id, ingredients } = await request.json();

    try {
        for (const ingredient of ingredients) {
            const query = `INSERT INTO _restock_ingredient (ingredient_id, restock_order_id, quantity) \
                            VALUES (${ingredient.ingredient_id}, ${r_id}, ${ingredient.quantity});`;
            const result = await connection.query(query); 
        }
        return json({success: true, data: null});
    } catch {
        console.error('Error updating post_restock_ingredient: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}