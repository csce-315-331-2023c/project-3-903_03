import { connectToDB } from '$lib/db';

// const menuitems = await sql`
//     select 
//         *
//     from
//         _menu_item
// `;

// console.log(menuitems);

export async function handle({ event, resolve }) {
    const dbconn = await connectToDB();
    event.locals = { dbconn };
    const response = await resolve(event);
    dbconn.release();

    return response;
}
