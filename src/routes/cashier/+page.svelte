<script>
    import { Button } from 'sveltestrap';

    import { auth } from '$lib/auth.js';
    import { goto } from '$app/navigation';
    let category;
    $: category = $auth.category
    $: if (!import.meta.env.SSR &&  category !== 'cashier')
        goto(`/login`, { replace: true });
</script>


<title>Cashier</title>

<header>
    Welcome Cashier
</header>

<body class="center">
    <Button href="/cashier/place_order" style="margin: 10px; width: 200px; padding:10px; font-size:17px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Place Order</Button>
    <Button href="/cashier/order_history" style="margin: 10px; width: 200px; padding:10px; font-size:17px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Order History</Button>
</body>

<style>
    header {
        text-align: center;
        font-size: 25px;
        padding: 20px;
    }
    body {
      background-color: #dbdbdd; 
    }

    .center {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 20px;
    }
</style>
