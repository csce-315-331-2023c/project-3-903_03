import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET({url}) {
    let connection = await pool.connect();
    const username = url.searchParams.get('username');
    const password = url.searchParams.get('password');
    let sql = 
        `SELECT u.id, u.name, uc.name AS category FROM _user AS u, _user_category AS uc \
         WHERE u.username = '${username}' and u.password = '${password}' and u.category_id = uc.category_id; `
    try {
        const result = await connection.query(sql);
        return json({success: true, user: result.rows});
    } catch {
        return json({success: false, error: 'User not found'});
    } finally {
        connection.release();
    }
}