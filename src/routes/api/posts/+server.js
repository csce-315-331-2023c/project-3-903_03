const { Pool } = require('pg');

const pool = new Pool({
  user: 'csce315_903_03user',
  host: 'your_db_host',
  database: 'csce315_903_03db',
  password: 'lunar little lively',
  port: 'your_db_port',
});

const getUsername = async () => {
  const client = await pool.connect();
  try {
    const result = await client.query('SELECT * FROM ');
    return result.rows;
  } finally {
    client.release();
  }
};

export const POST = async ({ request }) => {
  const requestBody = await request.json();
  const { username, password } = requestBody;

  // check credentials
  const dbResults = await getUsername(username);

  if (dbResults.length === 1 && dbResults[0].password === password) {
    // successful login
    return new Response(JSON.stringify({ message: 'Login successful' }), { status: 200 });
  } else {
    // invalid credentials
    return new Response(JSON.stringify({ message: 'Invalid credentials' }), { status: 401 });
  }
};


