<script>
    import Nav from "../Nav.svelte";
    import {navigate} from "svelte-routing";
    
    import { Form, InputGroup, InputGroupText, Input, Button, ButtonGroup } from 'sveltestrap';
    let username = '';
    let password = '';
    let id;
    let name;
    let category;


    async function login() {
        let input = `/login/get?username=${username}&password=${password}`;
        const response = await fetch(input);
        let data = await response.json();
        if (data.success && data.user.length == 1) {
            id = data.user[0].id;
            name = data.user[0].name;
            category = data.user[0].category;
            username = '';
            password ='';
            navigate(`/${category}`, { replace: true });
            location.reload();
      } else {
            console.log("Wrong credential");
        }
    }    

</script>

<Nav />


<img src="https://consultancy.innotecuk.com/wp-content/uploads/2017/10/cookies-banner.jpg" style="width:100%" height="175">
<!-- <div style="width: 100%; height: 100%; background: #D9D9D9">
    <p style="font-size: 40px; text-align: center; margin-bottom: 0 auto" >Welcome to Tiff's Treats! <br></p>
    <form style="text-align: center" action="/action_page.php">
        <label for="uname">username:</label>
        <input type="text" id="uname" name="username"><br><br>
        <label for="pword">password:</label>
        <input type="password" id="pword" name="password"><br><br>
        <input type="submit" value="Login" style="margin-bottom: 20px; background:white"> 
        <input type="submit" value="Create New Account" style="background:#63C5DA">
    </form>
</div> -->
<div class="center-container" style="width: 100%; height: 100%; background: #D9D9D9">
    <p style="font-size: 40px; text-align: center; margin-bottom: 0 auto" >Welcome to Tiff's Treats! <br></p>
    <Form >
        <InputGroup  style="width: 300px;padding: 10px;">
            <InputGroupText>Username:</InputGroupText>
            <Input bind:value={username}/>
        </InputGroup>
        <InputGroup style="width: 300px;padding: 10px;">
            <InputGroupText>Password:</InputGroupText>
            <Input type="password" bind:value={password}/>
        </InputGroup>
        <ButtonGroup style="width: 300px;padding: 10px;">
            <Button active on:click={login}>Login</Button>
            <Button>Create New Account</Button>
        </ButtonGroup>
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
  
<nav>
    <a href="./manager">manager</a>
    <a href="./customer">customer</a>
    <a href="./cashier">cashier</a>
    <!-- Work on other navigation screens-->   
</nav>
