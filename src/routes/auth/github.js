// src/routes/auth/[provider].js

import passport from 'passport';
import { Strategy as OAuth2Strategy } from 'passport-oauth2';
import pgp from 'pg-promise';

const db = pgp('postgres://username:password@localhost:5432/database');

passport.use(new OAuth2Strategy({
  // OAuth configuration
  clientID: 'e5c959e86a3530c6169f',
  clientSecret: 'b3fb0238542aada79b360776c9c7a325d652a1ac',
  callbackURL: 'https://project-3-903-03.fly.dev/auth/github/callback',
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
}));

export async function get({ params }) {
    // Start the GitHub OAuth flow
    passport.authenticate(params.provider)(this.req, this.res);
}