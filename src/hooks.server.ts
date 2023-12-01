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


//import { SvelteKitAuth, type SvelteKitAuthConfig } from '@auth/sveltekit';
import Auth0Provider from '@auth/core/providers/auth0';
import type { Provider } from '@auth/core/providers';
import type { Handle } from '@sveltejs/kit';
import { SvelteKitAuth } from "@auth/sveltekit";
import GitHub from "@auth/core/providers/github";

/*
const config: SvelteKitAuthConfig = {
    providers: [
      Auth0Provider({
        id: 'auth0',
        name: 'Auth0',
        clientId: 'e2RZMYbCylyu53V2EY93IpFbtoKsspVq',
        clientSecret: '0xSj6zHJhoB4IS5mvNV4iuH29lwgwtRf0qRWRX-ObQxVi_qnaY9s0aya877kMul6',
        issuer: 'https://dev-xismmavk62ixzjka.us.auth0.com/',  // <- remember to add trailing `/` 
        wellKnown: 'https://dev-xismmavk62ixzjka.us.auth0.com/.well-known/openid-configuration',
        userinfo: {
          //url: "/",
          // The result of this method will be the input to the `profile` callback.
          async conform(response) {
            if (response.ok) {
              const body = await response.clone().json()
              if (body?.response?.access_token) {
                return new Response(JSON.stringify(body.response), response)
              } else if (body?.access_token) {
                console.warn("Token response conforms to the standard, workaround not needed.")
              }
            }
            return response
          }
        }
      }) as Provider
    ],
    trustHost: true,
    secret: 'cfc1bb18fc9ba615ea8a3f6db2df089c',
    debug: true,
    session: {
      //maxAge: 1800 // 30 mins
      maxAge: 1 // instant
    }
  };

  
  export const handle = SvelteKitAuth(config) satisfies Handle;
  */

const passport = require('passport');
const OAuth2Strategy = require('passport-oauth2').Strategy;
const pgp = require('pg-promise')();
const db = pgp('postgres://username:password@localhost:5432/database');

passport.use(new OAuth2Strategy({
    // OAuth configuration
    clientID: 'e2RZMYbCylyu53V2EY93IpFbtoKsspVq',
    clientSecret: '0xSj6zHJhoB4IS5mvNV4iuH29lwgwtRf0qRWRX-ObQxVi_qnaY9s0aya877kMul6',
    callbackURL: '/',
  },
  async (accessToken, refreshToken, profile, done) => {
    // Use profile data to fetch user data from PostgreSQL
    try {
      const user = await db.oneOrNone('SELECT * FROM users WHERE oauth_id = $1', profile.id);
      return done(null, user);
    } catch (error) {
      return done(error);
    }
  }
));

// Serialize and deserialize user for session support
passport.serializeUser((user, done) => done(null, user));
passport.deserializeUser((obj, done) => done(null, obj));
