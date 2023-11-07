import { Pool } from 'pg'

// Create pool
export const pool = new Pool({
    database: 'postgres',
    user: 'postgres',
    host: 'project3-db.fly.dev',
    password: 'IQ4zWl70KfNQFpF',
    port: 5432
  });

