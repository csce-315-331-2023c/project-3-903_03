<script>
    import Nav from "../../Nav.svelte";

    let name = 'Philip Ritchey'

    import { Table } from 'sveltestrap';

    let isOpen = false;

    function handleUpdate(event) {
        isOpen = event.detail.isOpen;
    }

    let isEditing = false;
    function toggleEdit() {
        isEditing = !isEditing;
    }

export let data;
</script>

<style>
    header {
        text-align: left;
        font-size: 20px;
        padding: 10px;
    }

</style>

<title>Manager: Menu Items</title>
<Nav />

<div>
    <header >Manager: { name }</header>
</div>

<div>
    <header >Menu Items</header>
</div>

<button on:click={toggleEdit}>Toggle Edit Mode</button>

<Table bordered>
    <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Price</th>
            <th>Calories</th>
            <th>Ingredients</th>
        </tr>
    </thead>
    <tbody>
        {#each data.menu_items as menu_item}
        <tr>
            <td>{menu_item.menu_item_id}</td>
            <td>
                {#if isEditing}
                    <input type="text" bind:value={menu_item.name}/>
                {:else}
                    {menu_item.name}
                {/if}
            </td>
            <td>
                {#if isEditing}
                    <input type="text" bind:value={menu_item.price}/>
                {:else}
                    {menu_item.price}
                {/if}
            </td>    
            <td>{menu_item.calories}</td>
            <td>{menu_item.ingredients}</td>
        </tr>
        {/each}
    </tbody>
</Table>