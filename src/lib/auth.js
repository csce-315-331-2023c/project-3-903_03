import { writable } from 'svelte/store';
import { goto } from '$app/navigation';

export let auth = writable({ 
    isAuthenticated: false, 
    id : null,
    username : null,
    password : null,
    name : null,
    category : null
});

export function checkAuthentication() {
    if (!auth.isAuthenticated)
    goto(`/login`, { replace: true });
}

export function isAuthenticated() {
    return auth.isAuthenticated;
}



