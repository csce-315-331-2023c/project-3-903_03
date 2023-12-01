import pool from "$lib/db/pg";
/** @type {import('./$types').PageServerLoad} */

export async function load() {
    let connection = await pool.connect();
    let i_sql = "SELECT * FROM _ingredient;"; 
    try {
        const i_result = await connection.query(i_sql);
        // console.log(i_result.rows);
        return { ingredients : i_result.rows, };
    } finally {
        connection.release();
    }
}