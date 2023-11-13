import pool from "$lib/db/pg";
/** @type {import('./$types').PageServerLoad} */

export async function _login(username, password) {
    //alert("HELLO");
    //console.log("hello");
    var pword = password;
    var uname = username;

    let connection = await pool.connect();
    let sqlTemplate = "SELECT name FROM _user WHERE username = '?' and password = '?';";
    console.log(sqlTemplate);
    let sql = "";
    var unknown = 1;
    
    for (var i = 0; i < sqlTemplate.length; i ++) {
        //console.log(i);
        //console.log(sqlTemplate[i]);
        if (sqlTemplate[i] == "?" && unknown == 1) {
            
            if (uname == "") {

              return 1;
            }
            sql += username;
            unknown ++;
        } else if (sqlTemplate[i] == "?" && unknown == 2) {

            if (pword == "") {

              return 1
            }
            sql += pword;
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
