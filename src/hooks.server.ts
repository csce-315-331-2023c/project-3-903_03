// import { connectToDB } from '$lib/db';

// const menuitems = await sql`
//     select 
//         *
//     from
//         _menu_item
// `;

// console.log(menuitems);

// export async function handle({ event, resolve }) {
//     const dbconn = await connectToDB();
//     event.locals = { dbconn };
//     const response = await resolve(event);
//     dbconn.release();

//     return response;
// }

import { SvelteKitAuth, type SvelteKitAuthConfig } from '@auth/sveltekit';
import Auth0Provider from '@auth/core/providers/auth0';
import type { Provider } from '@auth/core/providers';
import type { Handle } from '@sveltejs/kit';

const config: SvelteKitAuthConfig = {
    providers: [
      Auth0Provider({
        id: 'auth0',
        name: 'Auth0',
        clientId: 'e2RZMYbCylyu53V2EY93IpFbtoKsspVq',
        clientSecret: '0xSj6zHJhoB4IS5mvNV4iuH29lwgwtRf0qRWRX-ObQxVi_qnaY9s0aya877kMul6',
        issuer: 'https://dev-xismmavk62ixzjka.us.auth0.com/',  // <- remember to add trailing `/` 
        wellKnown: 'https://dev-xismmavk62ixzjka.us.auth0.com/.well-known/openid-configuration'
      }) as Provider
    ],
    secret: 'cfc1bb18fc9ba615ea8a3f6db2df089c',
    debug: true,
    session: {
      maxAge: 1800 // 30 mins
    }
  };
  
  export const handle = SvelteKitAuth(config) satisfies Handle;