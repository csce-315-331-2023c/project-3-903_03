import { writable } from 'svelte/store';

export const auth = writable({ isAuthenticated: false, user: null, token: null, });

export function checkAuthentication() {
    return $auth.isAuthenticated;
}