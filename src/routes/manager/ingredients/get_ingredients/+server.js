import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET() {
    let connection = await pool.connect();
    let sql = "SELECT * FROM _ingredient;"; 
    try {
        const result = await connection.query(sql);
        // console.log(i_result.rows);
        return json(result.rows);
    } finally {
        connection.release();
    }
}