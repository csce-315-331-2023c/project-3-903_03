<script>
    import Nav from "../../Nav.svelte";

    let name = 'Philip Ritchey'

    import { Table, Button } from 'sveltestrap';

    // let isEditing = false;
    // function toggleEdit() {
    //     isEditing = !isEditing;
    // }

    let editable_row = null;
    function toggle_edit(row) {
        if (editable_row === row)
            editable_row = null;
        else
            editable_row = row;
    }

    async function update_row(row) {
        console.log(row);
        const data = {
            id: row.menu_item_id,
            name: row.name,
            price : row.price,
        };
        console.log(data);
        const options = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/menu_item/patch', options);
        row = await response.json();
        editable_row = null;
    }

    export let data;
    let menu_items = data.menu_items;
    menu_items.sort((a, b) => a.menu_item_id - b.menu_item_id);
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

<!-- <Button>Add New Menu Item</Button>
<Button bind:active={isEditing} on:click={toggleEdit}>Edit Mode</Button>
<Button>Update</Button> -->

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
        {#each menu_items as menu_item}
        <tr class:editable={menu_item === editable_row}>
            <td>{menu_item.menu_item_id}</td>
            <td>
                {#if menu_item === editable_row}
                    <input type="text" bind:value={menu_item.name}/>
                {:else}
                    {menu_item.name}
                {/if}
            </td>
            <td>              
                {#if menu_item === editable_row}
                    <input type="text" pattern="^\$\d{1,3}(,\d{3})*(\.\d+)?$" bind:value={menu_item.price}/>
                {:else}
                    {menu_item.price}
                {/if}
            </td>    
            <td>{menu_item.calories}</td>
            <td>{menu_item.ingredients}</td>
            <td>
                {#if menu_item !== editable_row}
                    <Button on:click={() => toggle_edit(menu_item)}>edit</Button>
                {:else}
                    <Button on:click={() => update_row(menu_item)}>update</Button>
                    <Button on:click={() => toggle_edit(menu_item)}>cancel</Button>
                {/if}
            </td>
        </tr>
        {/each}
    </tbody>
</Table>