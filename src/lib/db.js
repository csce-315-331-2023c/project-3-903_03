import { Pool } from 'pg'

const connectionString = 'postgresql://postgres:Pbk7yhWrq93LDfR@project3.fly.dev:5432/postgres'
 
export const pool = new Pool({
  connectionString,
})


// the pool will emit an error on behalf of any idle clients
// it contains if a backend error or network partition happens
pool.on('error', (err, client) => {
  console.error('Unexpected error on idle client', err)
  process.exit(-1)
})

/**
 * Connect to the PostgreSQL database.
 * @returns {Promise<import("pg").Client>} A new client from the connection pool.
 */
export const connectToDB = async () => await pool.connect();