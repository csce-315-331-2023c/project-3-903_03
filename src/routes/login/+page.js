import { browser } from '$app/environment';
import Nav from "../Nav.svelte";

//import { createUser } from '$lib/db/index.ts';
//import type { ActionData } from './$types';
//import { _login } from './authenticator.js';
let username = "";
let password = "";

export async function load() {
    
    console.log(username);
}

export async function _updateClient() {
    if (typeof document !== 'undefined') {
        const response = await fetch('/path/to/your/server/endpoint', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ username }),
        });
    
        const result = await response.json();
        console.log(result);
      }
  }