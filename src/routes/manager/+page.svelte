<script>
    import Nav from "../Nav.svelte";
    import { Button } from 'sveltestrap';

    let name = 'Philip Ritchey'
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
<Nav />

<div>
    <header >Welcome Manager, { name }</header>
</div>

<div class="center">
    <Button href="/manager/menu_item" style="margin: 10px; width: 300px;">Menu Items</Button>
    <Button href="/manager/ingredients" style="margin: 10px; width: 300px">Ingredients</Button>
    <Button href="/manager/trends" style="margin: 10px; width: 300px">Trends</Button>
</div>
