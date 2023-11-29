import pool from "$lib/db/pg";
import { json } from '@sveltejs/kit';

export async function GET({url}) {
    let connection = await pool.connect();
    const id = url.searchParams.get('id');
    const from_date = url.searchParams.get('from_date');
    const to_date = url.searchParams.get('to_date');
    const pending = url.searchParams.get('pending');
    const completed = url.searchParams.get('completed');
    const canceled = url.searchParams.get('canceled');
    const fd = (from_date == "") ? '' : ` and '${from_date}' <= co.order_date`;
    const td = (to_date == "") ? '' : ` and '${to_date}' >= co.order_date`;
    let status = '';
    status += ` and (`;
    status += (pending == 'true' ? `co.status_id = 0` : `false`);
    status += ` or `;
    status += (completed == 'true' ? `co.status_id = 1` : `false`);
    status += ` or `;
    status += (canceled == 'true' ? `co.status_id = 2` : `false`);
    status += `) `;    

    let co_sql = 
        `SELECT co.customer_order_id, co.cost, co.status_id, \
         TO_CHAR(co.order_date, 'YYYY-MM-DD') AS order_date, \
         co.order_time AS order_time \
         FROM _customer_order AS co \
         WHERE co.id = '${id}' ${fd} ${td} ${status}\ 
         ORDER BY co.customer_order_id DESC \
         LIMIT 10; `;
    let co_oi_sql = 
         `SELECT mi.menu_item_id, mi.name, oi.amount \
          FROM _ordered_item AS oi, _menu_item AS mi \
          WHERE oi.customer_order_id = $1 \
          AND oi.menu_item_id = mi.menu_item_id; `
    
    try {
        const co_result = await connection.query(co_sql);
        for (const co of co_result.rows) {
            const co_oi_result = await connection.query(co_oi_sql, [co.customer_order_id]);
            co['items'] = co_oi_result.rows;
        }
        return json({success: true, customer_orders: co_result.rows});
    } catch {
        return json({success: false, error: 'Orders were not found'});
    } finally {
        connection.release();
    }
}