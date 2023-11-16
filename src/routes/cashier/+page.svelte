<script>
    import Nav from "../Nav.svelte";
    import { onMount } from 'svelte';

    onMount(() => {
        get_menu_items();
        return () => {
        };
    });

    import { Button } from 'sveltestrap';

    let name = 'Sophia Dronova'

    let menu_items = [];
    async function get_menu_items() {
        let input = `/manager/cashier/get_menu_items`;
        const response = await fetch(input);
        menu_items = await response.json();
    }
</script>

<style>
    header {
        text-align: left;
        font-size: 25px;
        padding: 20px;
    }

</style>

<title>Manager</title>
<Nav />

<div>
    <header >Welcome Cashier, { name }</header>
</div>