import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET() {
    let connection = await pool.connect();
    let sql = `SELECT * FROM _menu_item;`;
    try {
        const result = await connection.query(sql);
        // console.log(result.rows);
        return json(result.rows);
    } finally {
        connection.release();
    }
}