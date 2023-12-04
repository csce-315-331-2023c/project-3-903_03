import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function POST({request}) {
    let connection = await pool.connect();
    const {name, username, password, category_id} = await request.json();
    let sql = 
        `WITH max_id AS (SELECT COALESCE(MAX(id), 0) + 1 AS new_id FROM _user) \
         INSERT INTO _user(id, name, username, password, category_id) \
         SELECT new_id, '${name}', '${username}', '${password}', ${category_id} FROM max_id`;
    try {
        const result = await connection.query(sql);
        return json({success: true});
    } catch {
        console.error('Error updating row: ');
        return json({sucess: false, error: 'Failed to updated row'});
    } finally {
        connection.release();
    }
}