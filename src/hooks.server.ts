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
// Import required modules
import { SvelteKitAuth, type SvelteKitAuthConfig } from '@auth/sveltekit';
import GitHub from '@auth/core/providers/github';
import type { Provider } from '@auth/core/providers';
import type { Handle } from '@sveltejs/kit';

// SvelteKitAuth configuration
const config = {
  providers: [
    GitHub({
      id: 'github',
      name: 'GitHub',
      clientId: 'e5c959e86a3530c6169f',
      clientSecret: 'b3fb0238542aada79b360776c9c7a325d652a1ac',
    }),
  ],
  trustHost: true,
  secret: 'b3fb0238542aada79b360776c9c7a325d652a1ac',
  debug: true,
  session: {
    maxAge: 1800, // 30 mins
  },
};

export const handle = SvelteKitAuth(config);

/*
const passport = require('passport');
const OAuth2Strategy = require('passport-oauth2').Strategy;
const pgp = require('pg-promise')();
const db = pgp('postgres://username:password@localhost:5432/database');

// GitHub OAuth configuration
passport.use(new OAuth2Strategy({
    clientID: 'e5c959e86a3530c6169f',
    clientSecret: 'b3fb0238542aada79b360776c9c7a325d652a1ac',
    callbackURL: 'https://project-3-903-03.fly.dev/',
  },
  async (accessToken, refreshToken, profile, done) => {
    try {
      // Extract relevant user information from GitHub profile
      const { id, username, displayName, emails } = profile;

      // Use the extracted information to fetch or create a user in your database
      const user = await db.oneOrNone('SELECT * FROM _user WHERE github_id = $1', id);
      if (!user) {
        // If the user doesn't exist, create a new user in the database
        await db.none('INSERT INTO _user (github_id, username, display_name, email) VALUES ($1, $2, $3, $4)', [id, username, displayName, emails[0].value]);
      }

      // Return the user data
      return done(null, { id, username, displayName, email: emails[0].value });
    } catch (error) {
      return done(error);
    }
  }
));

// Serialize and deserialize user for session support
passport.serializeUser((user, done) => done(null, user));
passport.deserializeUser((obj, done) => done(null, obj));

// SvelteKitAuth configuration
const config: SvelteKitAuthConfig = {
  providers: [
    GitHub({
      id: 'github',
      name: 'GitHub',
      clientId: 'e5c959e86a3530c6169f',
      clientSecret: 'b3fb0238542aada79b360776c9c7a325d652a1ac',
    }) as Provider,
  ],
  trustHost: true,
  secret: 'b3fb0238542aada79b360776c9c7a325d652a1ac',
  debug: true,
  session: {
    maxAge: 1800, // 30 mins
  },
};

export const handle = SvelteKitAuth(config) as Handle;
*/