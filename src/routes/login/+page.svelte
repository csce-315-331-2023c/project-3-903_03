<script>
    import { auth, isAuthenticated } from '$lib/auth.js';
    import { goto } from '$app/navigation';
    import { Form, InputGroup, InputGroupText, Input, Button, ButtonGroup, Card, CardBody } from 'sveltestrap';
    import { onMount } from 'svelte';

    let username = '';
    let password = '';

    

    async function login() {
        const input = `/login/get?username=${username}&password=${password}`;
        const response = await fetch(input);
        const data = await response.json();
        if (data.success && data.user.length == 1) {
            let id = data.user[0].id;
            let name = data.user[0].name;
            let category = data.user[0].category;
            set_auth(true, id, username, password, name, category);
            username = '';
            password ='';
            message = '';
            goto(`/${category}`, { replace: true });
        } else {
            message = 'Wrong credentials'
        }
    }   
    
    $: name = ($auth.user == null ? '' : $auth.user.name);
    if (name !== '') {
        auth.set({
            isAuthenticated: false, 
            id : null,
            username : null,
            password : null,
            name : null,
            category : null
        });      
    }
   
    function logout() {
        set_auth(false, null, null, null, null, null);
    }

    function set_auth (isA, id, username, password, name, category) {
        auth.set({
            isAuthenticated: isA, 
            id : id,
            username : username,
            password : password,
            name : name,
            category : category
        });      
    }

    function check() {
        if (isAuthenticated()) 
            logout();
        else
            login();
    }    

    let message = '';

// Replace with your GitHub OAuth application credentials
const githubClientId = 'e5c959e86a3530c6169f';

async function loginWithOAuth() {
    // Generate a random code verifier
    const codeVerifier = 'your_random_code_verifier'; // Replace with an actual random value

    // Create a code challenge from the code verifier
    const codeChallenge = btoa(codeVerifier);

    // Construct the OAuth authorization URL
    const oauthURL = `https://github.com/login/oauth/authorize?client_id=${githubClientId}`;

    // Redirect the user to the OAuth authorization URL
    window.location.href = oauthURL;
}

// Handle the callback after the user is redirected back from the OAuth provider
async function handleCallback() {
    // Extract the authorization code from the URL
    const urlParams = new URLSearchParams(window.location.search);
    const authorizationCode = urlParams.get('code');

    if (authorizationCode) {
        // Exchange the authorization code for an access token
        await authenticateWithOAuth(authorizationCode);
    }
}

// Replace with your actual GitHub OAuth application credentials
const githubClientSecret = '8b6973ee465c4b4ac5f6770b11480e5bc6e21ee2';
const redirectUri = 'http://127.0.0.1:5173/'; // Replace with your actual callback URL

async function authenticateWithOAuth(code) {
    // Send the authorization code to your server to exchange it for an access token
    const response = await fetch('/exchange-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            code,
            clientId: githubClientId,
            clientSecret: githubClientSecret,
            redirectUri,
        }),
    });

    const data = await response.json();

    if (data.token) {
        // Store the access token securely (for example, in local storage)
        localStorage.setItem('oauthToken', data.token);

        // Continue with your application logic...
    } else {
        // Handle error when exchanging code for token
        console.error('Error exchanging code for token:', data.error);
    }
}

// Extract the authorization code from the URL
const urlParams = new URLSearchParams(window.location.search);
const authorizationCode = urlParams.get('code');

if (authorizationCode) {
    // The URL contains an authorization code
    // Exchange it for an access token
    authenticateWithOAuth(authorizationCode);
}


    async function onLoad() {

    // Check if the URL contains an authorization code
    //alert('Loading');
    const urlParams = new URLSearchParams(window.location.search);
    const authorizationCode = urlParams.get('code');

    if (authorizationCode) {
    // The URL contains an authorization code
    // Exchange it for an access token
    await authenticateWithOAuth(authorizationCode);

    // Remove the code from the URL (optional, depending on your app design)
    //const urlWithoutCode = window.location.origin + window.location.pathname;
    window.history.replaceState({}, document.title, urlWithoutCode);
    }
    }

    onMount(onLoad);
</script>


<img src="https://consultancy.innotecuk.com/wp-content/uploads/2017/10/cookies-banner.jpg" alt= "Cookies" style="width:100%" height="175">
<div class="center-container" style="width: 100%; height: 100%">
    <p style="font-size: 40px; text-align: center; margin-bottom: 0 auto" >Welcome to Tiff's Treats! <br></p>
    <Form >
        <InputGroup  style="width: 300px;padding: 10px;">
            <InputGroupText style="width: 35%">Username:</InputGroupText>
            <Input id="username" bind:value={username} autocomplete="off"/>
        </InputGroup>
        <InputGroup style="width: 300px;padding: 10px;">
            <InputGroupText style="width: 35%">Password:</InputGroupText>
            <Input id="password" type="password" bind:value={password} autocomplete="off"/>
        </InputGroup>
        <ButtonGroup style="width: 300px;padding: 10px;">
            <Button active on:click={check}>Login</Button>
            <Button color="primary" on:click={loginWithOAuth}>Login with OAuth</Button>
        </ButtonGroup>
        &nbsp
        {#if (message !== '')}
            <Card style="text-align: center; color: white" color="danger">
                    {message}
            </Card>
        {/if}

        
    </Form>

</div>

<style>
    .center-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }
  </style>
  

