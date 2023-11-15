//import bcrypt from 'bcrypt';
import pool from '$lib/db/pg';

export async function createUser(
    
    username: string,
    password: string,
    name: string,
    catID: BigInteger,
  ): Promise<void> {

    //const hashedPassword = await bcrypt.hash(password, 12);
    //console.log(hashedPassword);
    console.log(password);
    const sql = {
    text: 'insert into _user (name, username, password, category_id) VALUES($1, $2, $3, $4)',
    values: [name, username, password, catID],
    }

    
    const res = await pool.query(sql);
    console.log(res.rows[0]);
  }
  