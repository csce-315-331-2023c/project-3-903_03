import pool from "$lib/db/pg";
/** @type {import('./$types').PageServerLoad} */


export async function load() {

    //alert("HELLO");
    //console.log("hello");
    var password = "";
    var username = "";

    let connection = await pool.connect();
    let sqlTemplate = "SELECT name FROM _user WHERE username = '?' and password = '?';";
    //console.log(sqlTemplate);
    let sql = "";
    var unknown = 1;
    
    for (var i = 0; i < sqlTemplate.length; i ++) {
        //console.log(i);
        //console.log(sqlTemplate[i]);
        if (sqlTemplate[i] == "?" && unknown == 1) {
            
            if (username == "") {

                username = "aweng";
                unknown ++;
            }
            sql += username;
        } else if (sqlTemplate[i] == "?" && unknown == 2) {

            if (password == "") {

                password = "jump560";
            }
            sql += password;
        } else {

            sql += sqlTemplate[i];
        }
    }

    console.log(sql);

    try {
        const result = await connection.query(sql);
        console.log(result.rows);
        return { _user : result.rows, };
    } finally {
        connection.release();
    } 

 }

 
