<script>
    import Nav from "../../Nav.svelte";

    let manager_name = 'Philip Ritchey'
    let manager_id = 1;
    

    import { Table,
             Button,
             Modal,
             ModalBody,
             ModalFooter,
             ModalHeader,
             FormGroup,
             Input,
             Label
            } from 'sveltestrap';


    let i_name = '';
    let i_current = 0;
    let i_needed = 0;
    let i_cost = '';


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
        const response = await fetch('/manager/ingredients/post_ingredient', options);
        await response.json();
        i_name = '';
        i_cost = 0;
    }

    let open_add = false; 
    
    let post_ingredients = [];
    
    async function post_restock_ingredient(id) {
        const data = {
            r_id: id,
            ingredients: post_ingredients,
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/ingredients/post_restock_ingredient', options);
        await response.json();
    }

    async function patch_ingredients() {
        const data = {
            ingredients: post_ingredients,
        };
        const options = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/ingredients/patch_ingredients', options);
        await response.json();
    }

    const current_date = new Date();
    const year = current_date.getFullYear();
    const month = (current_date.getMonth() + 1).toString().padStart(2, '0');
    const day = current_date.getDate().toString().padStart(2, '0');
    const formatted_date = `'${year}-${month}-${day}'`;

    async function post_restock() {
        const data = {
            id: manager_id,
            restock_date: String(formatted_date),
            cost: total_restock,
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/ingredients/post_restock', options);
        let value = await response.json();
        
        post_restock_ingredient(value.id);
        patch_ingredients();
        location.reload();
    }

    let total_restock = 0;
    function calculate_total_restock() {
        total_restock = 0;
        for (const i of data.ingredients) {
            const qty = i.needed_qty - i.current_qty;
            if (qty <= 0)
                continue;
            total_restock += (Number(i.cost.slice(1)) * qty);
            post_ingredients.push({'ingredient_id' : i.ingredient_id, 'quantity' : qty, 'needed_qty':i.needed_qty});
            
        }
    }  

    export let data;
    let ingredients = data.ingredients;
    ingredients.sort((a, b) => a.ingredient_id - b.ingredient_id);
    calculate_total_restock();

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
    <header >Manager: { manager_name }</header>
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
    <input type="text" bind:value={total_restock} readonly/>
    <Button on:click={post_restock}>Place Restock Order</Button>    
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
        {#each ingredients as i}
        <tr>
            <td>{i.ingredient_id}</td>
            <td>{i.name}</td>
            <td>{i.current_qty}</td>
            <td>{i.needed_qty}</td>
            <td>{i.cost}</td>
            
            <td>
                {#if i.current_qty <= i.needed_qty}
                    ${(i.cost.slice(1) * (i.needed_qty - i.current_qty)).toFixed(2)}
                {:else}
                    $0.00
                {/if}
            </td>    
        </tr>
        {/each}
    </tbody>
</Table>