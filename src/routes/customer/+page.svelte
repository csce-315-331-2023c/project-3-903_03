<script>
    import { Button } from 'sveltestrap';

    let name = 'John Smith'
</script>


<title>Customer</title>

<header>
    Welcome Customer, { name }
</header>

<body class="center">
    <Button href="/customer/place_order" style="margin: 10px; width:250px; padding:13px; font-size:19px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19" color="primary">Place Order</Button>
    <Button href="/customer/order_history" style="margin: 10px; width: 200px; padding:10px; font-size:17px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Order History</Button>
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
