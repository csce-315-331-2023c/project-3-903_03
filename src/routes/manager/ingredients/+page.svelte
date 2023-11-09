<script>
    import Nav from "../../Nav.svelte";

    let name = 'Philip Ritchey'
    

    import { Table, Button, Card, CardBody, CardText} from 'sveltestrap';

    let isOpen = false;

    function handleUpdate(event) {
        isOpen = event.detail.isOpen;
    }

    let isEditing = false;
    function toggleEdit() {
        isEditing = !isEditing;
    }

    let cost = 0

    export let data;

</script>

<style>
    header {
        text-align: left;
        font-size: 20px;
        padding: 10px;
    }

</style>

<title>Manager: Ingredients</title>
<Nav />

<div>
    <header >Manager: { name }</header>
</div>

<div>
    <header style="text-align:center; font-size:25px">Ingredients</header>
</div>


<Button>Add Ingredient</Button>
<Button bind:active={isEditing} on:click={toggleEdit}>Edit Mode</Button>
<Button>Update</Button>    


<div style="float:right">
    <h>Restock Cost ($) :</h>
    <input type="number" bind:value={cost} readonly/>
    <Button on:click={cost}>Place Restock Order</Button>    
</div>

<div>&nbsp</div>


<Table bordered>
    <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Current Qty</th>
            <th>Needed Qty</th>
            <th>Cost</th>
            <th>Restock Cost</th>
        </tr>
    </thead>
    <tbody>
        {#each data.ingredients as i}
        <tr>
            <td>{i.ingredient_id}</td>
            <td>
                {#if isEditing}
                    <input type="text" bind:value={i.name}/>
                {:else}
                    {i.name}
                {/if}
            </td>

            <td>
                {#if isEditing}
                    <input type="text" bind:value={i.current_qty}/>
                {:else}
                    {i.current_qty}
                {/if}
            </td>

            <td>
                {#if isEditing}
                    <input type="text" bind:value={i.needed_qty}/>
                {:else}
                    {i.needed_qty}
                {/if}
            </td>

            <td>{i.cost}</td>
            
            <td>
                {#if i.current_qty <= i.needed_qty}
                    <td>${(i.cost.slice(1) * (i.needed_qty - i.current_qty)).toFixed(2)}</td>
                {:else}
                    <td>$0.00</td>
                {/if}                
            </td>

        </tr>
        {/each}
    </tbody>
</Table>