<script>
    import { auth, isAuthenticated } from '$lib/auth.js';
    import { goto } from '$app/navigation';
    import { Form, InputGroup, InputGroupText, Input, Button, ButtonGroup, Card, CardBody } from 'sveltestrap';

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
  

