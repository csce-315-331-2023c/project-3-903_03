// const express = require('express');
// const fetch = require('node-fetch');
// const bodyParser = require('body-parser');
// const app = express();
// const port = 3000;

// // Replace these with your actual GitHub OAuth application credentials
// const githubClientId = 'e5c959e86a3530c6169f';
// const githubClientSecret = '8b6973ee465c4b4ac5f6770b11480e5bc6e21ee2';
// const redirectUri = 'http://localhost:3000/exchange-code'; // Must match the GitHub OAuth app settings

// app.use(bodyParser.json());

// app.post('/exchange-code', async (req, res) => {
//     const { code, clientId, clientSecret, redirectUri } = req.body;

//     // Exchange the authorization code for an access token
//     const tokenResponse = await fetch('https://github.com/login/oauth/access_token', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify({
//             code,
//             client_id: clientId,
//             client_secret: clientSecret,
//             redirect_uri: redirectUri,
//         }),
//     });

//     const tokenData = await tokenResponse.json();

//     if (tokenData.error) {
//         // Handle error
//         return res.status(400).json({ error: tokenData.error });
//     }

//     // Respond with the access token
//     return res.json({ token: tokenData.access_token });
// });

// app.listen(port, () => {
//     console.log(`Server is running at http://localhost:${port}`);
// });
