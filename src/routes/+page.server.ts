  import { signIn, signOut } from "@auth/sveltekit/client"
  import { page } from "$app/stores"

/*
export const load = (async ({ fetch, locals }) => {
  let url = '';
  try {
    const session = await locals.getSession();
    if (!session?.user) {
      const tokenCall = await fetch('/auth/csrf');
      const csrfTokenResponse = await new Response(tokenCall.body).json();
      const csrfToken = csrfTokenResponse.csrfToken;

      const params = new URLSearchParams();
      params.append('scope', 'api openid profile email');

      const formData = new URLSearchParams();
      formData.append('redirect', 'false');
      formData.append('csrfToken', csrfToken);

      const signInRequest = await fetch('/auth/signin/auth0? ' + params.toString(), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'X-Auth-Return-Redirect': '1'
        },
          body: formData.toString()
      });
      const signInResponse = await new Response(signInRequest.body).json();

      if (signInResponse?.url) {
        url = signInResponse.url;
      }
    }
  } catch (e: any) {
    console.log('Exception thrown while auto-sign-in: ', e);
  }

  if (url) {
    throw redirect(302, url);
  }
}) satisfies PageServerLoad;

/*
import pool from "$lib/db/pg";
//@type {import('./$types').PageServerLoad}
import { _updateClient } from './+page.js';

export async function load() {

    //alert("HELLO");
    //console.log("hello");
    let username = "uname";
    let password = "pword";

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
            }
            sql += username;
            unknown ++;
        } else if (sqlTemplate[i] == "?") {

            if (password == "") {

                password = "jump560";
            }
            sql += password;
        } else {

            sql += sqlTemplate[i];
        }
    }

    console.log(sql);

    _updateClient();
    try {
        const result = await connection.query(sql);
        console.log(result.rows);
        return { _user : result.rows, };
    } finally {
        connection.release();
    } 
    
 }
 export async function actions(request) {

    if (request.method === 'POST') {
      const { username } = JSON.parse(request.body);
  
      let connection = await pool.connect();
      let sql = `SELECT name FROM _user WHERE username = $1`;
  
      try {
        const result = await connection.query(sql, [username]);
        console.log(result.rows);
        return {
          body: JSON.stringify({ _user: result.rows }),
        };
      } finally {
        connection.release();
      }
    }
  }
  
*/