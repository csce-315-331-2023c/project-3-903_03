<script>
    import Nav from "../../Nav.svelte";

    let name = 'Philip Ritchey'
    

    import { Table,
             Button,
             Card,
             CardBody,
             CardText,
             Modal,
             ModalBody,
             ModalFooter,
             ModalHeader,
             FormGroup,
             Input,
             Label
            } from 'sveltestrap';

    let isOpen = false;

    let ingredients = [];

    let i_name = '';
    let i_current = 0;
    let i_needed = 0;
    let i_cost = '';

    let open_modal = false;
    let editable_row = null;

    function toggle_ingredient() {
        open_add = !open_add;
    }

    function cancel_ingredient() {
        toggle_ingredient();
    }

    async function add_ingredient() {
        toggle_ingredient();
        const data = {
            name: i_name,
            current_qty: i_current,
            needed_qty: i_needed,
            cost: i_cost,
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/menu_item/post_ingredient', options);
        await response.json();
        i_name = '';
        i_cost = 0;
    }

    let open_add = false;

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


<!-- <Button>Add Ingredient</Button>
<Button bind:active={isEditing} on:click={toggleEdit}>Edit Mode</Button>
<Button>Update</Button>     -->
<div>
    <Button color="primary" style="margin-left:25px" on:click={toggle_ingredient}>Add New Ingredient</Button>
    <Modal isOpen={open_add} backdrop={false} {toggle_ingredient} >
        <ModalHeader style="background-color:gray; color:white" {toggle_ingredient} >Add New Ingredient</ModalHeader>
        <ModalBody style="background-color:lightgray">
            <FormGroup>
                <Label for="name">Name</Label>
                <Input
                    type="text"
                    name="name"
                    id="name"
                    placeholder="name"
                    bind:value={i_name}
                />
            </FormGroup>

            <FormGroup>
                <Label for="current_qty">Current Quantity</Label>
                <Input
                    type="number"
                    name="current_qty"
                    id="current_qty"
                    placeholder="current quantity"
                    bind:value={i_current}
                />
            </FormGroup>
            
            <FormGroup>
                <Label for="needed_qty">Needed Quantity</Label>
                <Input
                    type="number"
                    name="needed_qty"
                    id="needed_qty"
                    placeholder="needed quantity"
                    bind:value={i_needed}
                />
            </FormGroup>

            <FormGroup>
                <Label for="cost">Cost</Label>
                <Input
                    type="text"
                    name="cost"
                    id="cost"
                    placeholder="cost"
                    bind:value={i_cost}
                />
            </FormGroup>

            <!-- <FormGroup>
                <Label for="ingredient_group">Ingredients</Label>
                {#each ingredients as i}
                    <Input
                        id={i.ingredient_id}
                        type="checkbox"
                        bind:group={mi_ingredients}
                        value="{i.name}"
                        on:change={(event) => handleCheckboxChange(event, i)}
                        label={i.name}
                    />
                {/each}
            </FormGroup> -->
            

        </ModalBody>
        <ModalFooter style="background-color:grey">
            <Button color="primary" on:click={add_ingredient}>Add New Ingredient</Button>
            <Button color="light" on:click={cancel_ingredient}>Cancel</Button>
        </ModalFooter>
    </Modal>
</div>
&nbsp 


<div style="float:right">
    <h>Restock Cost ($) :</h>
    <input type="number" bind:value={cost} readonly/>
    <Button>Place Restock Order</Button>    
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
                <!-- {#if isEditing}
                    <input type="text" bind:value={i.name}/>
                {:else}
                    {i.name}
                {/if} -->
                {i.name}
            </td>

            <td>
                <!-- {#if isEditing}
                    <input type="text" bind:value={i.current_qty}/>
                {:else}
                    {i.current_qty}
                {/if} -->
                {i.current_qty}
            </td>

            <td>
                <!-- {#if isEditing}
                    <input type="text" bind:value={i.needed_qty}/>
                {:else}
                    {i.needed_qty}
                {/if} -->
                {i.needed_qty}
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