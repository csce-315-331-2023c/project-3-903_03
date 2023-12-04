// src/routes/auth/[provider].callback.js

import passport from 'passport';

export async function get({ params }) {
  // Complete the GitHub OAuth flow
  passport.authenticate(params.provider, { failureRedirect: '/' })(this.req, this.res);
  this.res.end();
}
