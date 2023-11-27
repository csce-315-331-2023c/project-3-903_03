import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET() {
    let connection = await pool.connect();
    let sql = "SELECT mi.name AS name, mi.season as season  \
               FROM _menu_item AS mi \
               WHERE mi.season <> 'None';"
    try {
        const result = await connection.query(sql);
        // console.log(result.rows);
        return json(result.rows);
    } finally {
        connection.release();
    }
}