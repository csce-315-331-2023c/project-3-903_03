import pool from "$lib/db/pg";
/** @type {import('./$types').PageServerLoad} */

export async function load() {
    let connection = await pool.connect();
    let mi_sql = "SELECT * FROM _menu_item;";
    let mi_ing_sql = "SELECT i.name FROM _ingredient AS i, _item_ingredient AS ii WHERE i.ingredient_id = ii.ingredient_id AND ii.menu_item_id = $1";
    try {
        const mi_result = await connection.query(mi_sql);
        for (const mi of mi_result.rows) {
            const mi_ing_result = await connection.query(mi_ing_sql, [mi.menu_item_id]);
            const ingredients = [];
            for (const i of mi_ing_result.rows) {
                ingredients.push(i.name);
            }
            mi['ingredients'] = ingredients;
        }
        //console.log(mi_result.rows);
        return { menu_items : mi_result.rows, };
    } finally {
        connection.release();
    }
 }
