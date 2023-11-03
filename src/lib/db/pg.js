import { Pool } from 'pg'

// Create pool
const pool = new Pool({
    user: 'csce315_903_03user',
    host: 'csce-315-db.engr.tamu.edu',
    database: 'csce315_903_03db',
    password: 'lunar little lively',
    port: 5432,
});

export default pool;

