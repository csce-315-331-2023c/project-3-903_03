import pool from "$lib/db/pg";
/** @type {import('./$types').PageServerLoad} */

export async function load() {
    let connection = await pool.connect();
    let sql = "SELECT * FROM _menu_item;";
    try {
        const result = await connection.query(sql);
        // console.log(result.rows);
        return { menu_items : result.rows, };
    } finally {
        connection.release();
    }
 }

 