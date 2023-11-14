import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST( {request} ) {
    let connection = await pool.connect();
    const { name, current_qty, needed_qty, cost } = await request.json();
    let sql = 
        `WITH max_id AS (SELECT COALESCE(MAX(ingredient_id), 0) + 1 AS new_id FROM _ingredient) \
         INSERT INTO _ingredient(ingredient_id, name, current_qty, needed_qty, cost) \
         SELECT new_id, '${name}', ${current_qty}, ${needed_qty}, '$${cost}' FROM max_id \
         RETURNING ingredient_id`;
    console.log(sql);
    try {
        const result = await connection.query(sql);
        const id = result.rows[0].ingredient_id;
        return json({success: true, id: id});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}