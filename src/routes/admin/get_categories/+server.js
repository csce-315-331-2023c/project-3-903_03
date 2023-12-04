import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET() {
    let connection = await pool.connect();
    let sql = "SELECT * FROM _user_category;"; 
    try {
        const result = await connection.query(sql);
        return json({success : true, categories : result.rows});
    } catch {    
        return json({success : false, categories : result.rows});
        
    } finally {
        connection.release();
    }
}