<script>
    import { Button } from 'sveltestrap';

    import { auth } from '$lib/auth.js';
    import { goto } from '$app/navigation';
    let category;
    $: category = $auth.category
    $: if (!import.meta.env.SSR &&  category !== 'manager')
        goto(`/login`, { replace: true });

</script>

<style>
    header {
        text-align: center;
        font-size: 25px;
        padding: 20px;
    }

    .center {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 20px;
    }
</style>

<title>Manager</title>

<header >Welcome Manager</header>

<div class="center">
    <Button href="/manager/menu_item" style="margin: 10px; width:200px; padding:10px; font-size:17px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Menu Items</Button>
    <Button href="/manager/ingredients" style="margin: 10px; width: 200px; padding:10px; font-size:17px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Ingredients</Button>
    <Button href="/manager/trends" style="margin: 10px; width: 200px; padding:10px; font-size:17px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Trends</Button>
    <Button href="/manager/order_history" style="margin: 10px; width: 200px; padding:10px; font-size:17px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Order History</Button>
</div>
