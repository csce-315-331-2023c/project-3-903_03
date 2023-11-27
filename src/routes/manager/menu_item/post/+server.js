import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST( {request} ) {
    let connection = await pool.connect();
    const { name, price, calories, season} = await request.json();
    let sql = 
        `WITH max_id AS (SELECT COALESCE(MAX(menu_item_id), 0) + 1 AS new_id FROM _menu_item) \
         INSERT INTO _menu_item(menu_item_id, name, price, calories, season) \
         SELECT new_id, '${name}', '$${price}', ${calories}, '${season}' FROM max_id \
         RETURNING menu_item_id`;
    console.log(sql);
    try {
        const result = await connection.query(sql);
        const id = result.rows[0].menu_item_id;
        return json({success: true, id: id});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}