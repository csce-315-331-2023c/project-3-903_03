import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET() {
    let connection = await pool.connect();
    let sql = 
        `SELECT * FROM _status;`
    try {
        let result = await connection.query(sql);
        return json({success: true, statuses: result.rows});
    } catch {
        return json({success: false, error: 'Statuses were not found'});
    } finally {
        connection.release();
    }
}