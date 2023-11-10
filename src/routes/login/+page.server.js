import pool from "$lib/db/pg";
/** @type {import('./$types').PageServerLoad} */


export async function load() {

    var password = "";
    var username = "";

    let connection = await pool.connect();
    let sqlTemplate = "SELECT * FROM _user WHERE username = ?, password = ?";
    let sql = "";
    
    for (var i = 0; i < sqlTemplate.length; i ++) {

        var unknown = 1;
        if (sqlTemplate[i] == "?" && unknown == 1) {

            sql.concat("");
        } else if (sqlTemplate[i] == "?" && unknown == 2) {

            sql.concat("");
        } else {

            sql.concat(sqlTemplate[i]);
        }
    }

    console.log(sql);

    try {
        const result = await connection.query(sql);
        console.log(result.rows);
        return { menu_items : result.rows, };
    } finally {
        connection.release();
    }
 }

