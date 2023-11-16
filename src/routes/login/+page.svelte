<script lang="ts">
    import Nav from "../Nav.svelte";
    import { signIn } from '@auth/sveltekit/client';

    let username = "";
    let password = "";
</script>

<Nav />


<img src="https://consultancy.innotecuk.com/wp-content/uploads/2017/10/cookies-banner.jpg" alt="cookies" style="width:100%" height="175">
<div style="width: 100%; height: 100%; background: #D9D9D9">
    <p style="font-size: 40px; text-align: center; margin-bottom: 0 auto" >Welcome to Tiff's Treats! <br></p>
    <form style="text-align: center" onsubmit='window.location.reload()'>
        <label for="uname">username:</label>
        <input type="text" id="uname" name="username" bind:value={username}><br><br>
      
        <label for="pword">password:</label>
        <input type="password" id="pword" name="password" bind:value={password}><br><br>
      
        <input type="submit" value="Login" style="margin-bottom: 20px; background:white"> 
        <input type="submit" value="Create New Account" style="background:#63C5DA">
      </form>

      <button on:click={() => signIn(
        'auth0', {
          redirect: false,
          callbackUrl: 'http://localhost:4000/about'
        },
        {
          scope: 'api openid profile email'
        }
      )}>Sign In with Auth0</button>
      
</div>
