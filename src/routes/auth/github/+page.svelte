
<script>
	import Nav from '$lib/Nav.svelte';
	import { navigate } from 'svelte-routing';
	import { goto } from '$app/navigation';

	import { Form, InputGroup, InputGroupText, Input, Button, ButtonGroup } from 'sveltestrap';
	let username = '';
	let password = '';
	let id;
	let name;
	let category;

	const handleLogin = () => {
    // Redirect the user to the GitHub OAuth login page
    	goto('/auth/github');
  	};

	async function login() {
		let input = `/login/get?username=${username}&password=${password}`;
		const response = await fetch(input);
		let data = await response.json();
		if (data.success && data.user.length == 1) {
			id = data.user[0].id;
			name = data.user[0].name;
			category = data.user[0].category;
			username = '';
			password = '';
			navigate(`/${category}`, { replace: true });
			location.reload();
		} else {
			console.log('Wrong credential');
		}
	}
</script>

<Nav />

<img
	src="https://consultancy.innotecuk.com/wp-content/uploads/2017/10/cookies-banner.jpg"
	style="width:100%"
	height="175"
/>
<div class="center-container" style="width: 100%; height: 100%; background: #D9D9D9">
	<p style="font-size: 40px; text-align: center; margin-bottom: 0 auto">
		Welcome to Tiff's Treats! <br />
	</p>
	<Form>
		<InputGroup style="width: 300px;padding: 10px;">
			<InputGroupText>Username:</InputGroupText>
			<Input id="username" bind:value={username} autocomplete="off" />
		</InputGroup>
		<InputGroup style="width: 300px;padding: 10px;">
			<InputGroupText>Password:</InputGroupText>
			<Input id="password" type="password" bind:value={password} autocomplete="off" />
		</InputGroup>
		<ButtonGroup style="width: 300px;padding: 10px;">
			<Button active on:click={login}>Login</Button>
			<Button>Create New Account</Button>
			<Button active on:click={handleLogin}>Login with GitHub</Button>
		</ButtonGroup>
	</Form>
</div>

<nav>
	<a href="/manager">manager</a>
	<a href="/customer">customer</a>
	<a href="/cashier">cashier</a>
	<!-- Work on other navigation screens-->
</nav>

<style>
	.center-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
</style>
