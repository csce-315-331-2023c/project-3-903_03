import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET({url}) {
    let connection = await pool.connect();
    const from_date = url.searchParams.get('from_date');
    const to_date = url.searchParams.get('to_date');
    const pending = url.searchParams.get('pending');
    const completed = url.searchParams.get('completed');
    const canceled = url.searchParams.get('canceled');
    const fd = (from_date == "") ? 'true' : ` '${from_date}' <= order_date`;
    const td = (to_date == "") ? 'true' : ` '${to_date}' >= order_date`;
    let status = ` (`;
    status += (pending == 'true' ? `status_id = 0` : `false`);
    status += ` or `;
    status += (completed == 'true' ? `status_id = 1` : `false`);
    status += ` or `;
    status += (canceled == 'true' ? `status_id = 2` : `false`);
    status += `) `;    

    let sql = 
        `SELECT COUNT(*) as total \
         FROM _customer_order \
         WHERE ${fd} AND ${td} AND ${status};`;
    try {
        const result = await connection.query(sql);
        return json({success: true, total: result.rows[0].total});
    } catch {
        return json({success: false, error: 'Orders were not found'});
    } finally {
        connection.release();
    }
}