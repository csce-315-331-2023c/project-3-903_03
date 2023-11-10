import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET() {
    let connection = await pool.connect();
    let sql = "SELECT i.name AS name, i.current_qty AS current_qty, ROUND(i.needed_qty * 0.25, 0) AS min_qty \
               FROM _ingredient AS i \
               WHERE i.current_qty < i.needed_qty * 0.25"
    try {
        const result = await connection.query(sql);
        // console.log(result.rows);
        return json(result.rows);
    } finally {
        connection.release();
    }
}