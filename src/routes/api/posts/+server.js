export const GET = async ({ request }) => {
    const authHeader = request.headers.get('Authorization')
    
    if (authHeader !== 'customer') {
        return new Response(JSON.stringify({message: 'Invalid credentials'}), { status: 401 })
    }

    const res = await fetch('https://dummyjson.com/posts')
    const data = await res.json()

    return new Response(JSON.stringify(data), { status: 200 })
}

const { Pool } = require('pg');

const pool = new Pool({
  user: 'your_db_user',
  host: 'your_db_host',
  database: 'your_db_name',
  password: 'your_db_password',
  port: 'your_db_port',
});

// Example query
const getSomeData = async () => {
  const client = await pool.connect();
  try {
    const result = await client.query('SELECT * FROM your_table');
    return result.rows;
  } finally {
    client.release();
  }
};