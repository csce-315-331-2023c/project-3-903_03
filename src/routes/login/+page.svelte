<script lang="ts">
    import { browser } from '$app/environment';
    import Nav from "../Nav.svelte";
    //import { createUser } from '$lib/db/index.ts';
    //import type { ActionData } from './$types';
    //import { _login } from './authenticator.js';
    let username = "";
    let password = "";

    function big() {

        alert("big");
    }
</script>

<Nav />


<img src="https://consultancy.innotecuk.com/wp-content/uploads/2017/10/cookies-banner.jpg" alt="cookies" style="width:100%" height="175">
<div style="width: 100%; height: 100%; background: #D9D9D9">
    <p style="font-size: 40px; text-align: center; margin-bottom: 0 auto" >Welcome to Tiff's Treats! <br></p>
    <form style="text-align: center" onsubmit="window.location.reload();">
        <label for="uname">username:</label>
        <input type="text" id="uname" name="username" bind:value={username}><br><br>
      
        <label for="pword">password:</label>
        <input type="password" id="pword" name="password" bind:value={password}><br><br>
      
        <input type="submit" value="Login" style="margin-bottom: 20px; background:white"> 
        <input type="submit" value="Create New Account" style="background:#63C5DA" onclick="createUser()">
      </form>
</div>
