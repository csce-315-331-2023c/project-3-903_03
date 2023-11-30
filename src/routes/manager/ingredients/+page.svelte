<script>
    import { onMount } from 'svelte';

    onMount(() => {
        get_ingredients();
        return () => {
        };
    });

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
             Label,
             Row,
             Col
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

    async function get_ingredients() {
        let input = `/manager/ingredients/get_ingredients`;
        const response = await fetch(input);
        ingredients = await response.json();
        if (ingredients) {
            ingredients.sort((a, b) => a.ingredient_id - b.ingredient_id);
            calculate_total_restock();
        }
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
        get_ingredients();
    }

    let open_add = false; 
    
    let post_ingredients = [];
    let ingredients = [];
    
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
        get_ingredients();
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
        get_ingredients();
    }

    async function post_restock() {
        const current_date = new Date();
        const year = current_date.getFullYear();
        const month = (current_date.getMonth() + 1).toString().padStart(2, '0');
        const day = current_date.getDate().toString().padStart(2, '0');
        const formatted_date = `'${year}-${month}-${day}'`;
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
        
        patch_ingredients();        
        post_restock_ingredient(value.id);
        get_ingredients();
    }

    let total_restock = 0;
    function calculate_total_restock() {
        total_restock = 0;
        for (const i of ingredients) {
            const qty = i.needed_qty - i.current_qty;
            if (qty <= 0)
                continue;
            total_restock += (Number(i.cost.slice(1)) * qty);
            post_ingredients.push({'ingredient_id' : i.ingredient_id, 'quantity' : qty, 'needed_qty':i.needed_qty});
            
        }
    }  

    let edit_open = false;
    let edit_id = 0;
    let edit_name = '';
    let edit_current = 0;
    let edit_needed = 0;
    let edit_cost = '';

    function edit_toggle(ingredient) {
        edit_open = !edit_open;
        edit_id = ingredient.ingredient_id;
        edit_name = ingredient.name;
        edit_current = ingredient.current_qty;
        edit_needed = ingredient.needed_qty;
        edit_cost = ingredient.cost;
    }

    function edit_cancel() {
        edit_open = !edit_open;
    }

    async function edit_ingredient() {
        edit_open = !edit_open;
        const data = {
            id: edit_id,
            name: edit_name,
            current_qty: edit_current,
            needed_qty: edit_needed,
            cost: edit_cost,
        };
        const options = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/ingredients/patch', options);
        await response.json();
        get_ingredients();
    }

    async function delete_ingredient(ingredient_id) {
        const data = {
                ingredient_id: ingredient_id,
            };
            const options = {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            };
            const response = await fetch('/manager/ingredients/delete', options);
            await response.json();
            get_ingredients();
  }

</script>

<style>
    header {
        text-align: left;
        font-size: 20px;
        padding: 10px;
    }

</style>

<title>Manager: Ingredients</title>

<div>
    <header style="text-align:center; font-size:25px">Ingredients</header>
</div>

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

        </ModalBody>
        <ModalFooter style="background-color:grey">
            <Button color="primary" on:click={add_ingredient}>Add New Ingredient</Button>
            <Button color="light" on:click={cancel_ingredient}>Cancel</Button>
        </ModalFooter>
    </Modal>

    <div style="float:right">
        <h>Restock Cost ($) :</h>
        <input type="text" bind:value={total_restock} readonly/>
        <Button on:click={post_restock} style="margin-right:30px" >Place Restock Order</Button>    
    </div>

</div>
&nbsp 

<Table bordered>
    <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Current Qty</th>
            <th>Needed Qty</th>
            <th>Cost</th>
            <th>Restock Cost</th>
            <th>Edit Button</th>
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
            <td>
                <Row>
                    <Col>
                        <Button color="primary" style="margin-left:25px" on:click={() => edit_toggle(i)}>Edit</Button>
                        <Modal isOpen={edit_open} backdrop={false}>
                            <ModalHeader style="background-color:gray; color:white">Edit Ingredient</ModalHeader>
                            <ModalBody style="background-color:lightgray">
                                <FormGroup>
                                    <Label for="ename_${i.ingredient_id}">Name</Label>
                                    <Input
                                        type="text"
                                        name="name"
                                        id="ename_${i.ingredient_id}"
                                        placeholder="name"
                                        bind:value={edit_name}
                                        autocomplete="off"
                                    />
                                </FormGroup>
                    
                    
                                <FormGroup>
                                    <Label for="ecurrent_${i.ingredient_id}">Current Quantity</Label>
                                    <Input
                                        type="number"
                                        name="current_qty"
                                        id="ecurrent_${i.ingredient_id}"
                                        placeholder="current qty"
                                        bind:value={edit_current}
                                        autocomplete="off"
                                    />
                                </FormGroup>

                                <FormGroup>
                                    <Label for="eneeded_${i.ingredient_id}">Needed Quantity</Label>
                                    <Input
                                        type="number"
                                        name="needed_qty"
                                        id="eneeded_${i.ingredient_id}"
                                        placeholder="needed qty"
                                        bind:value={edit_needed}
                                        autocomplete="off"
                                    />
                                </FormGroup>

                                <FormGroup>
                                    <Label for="ecost_${i.ingredient_id}">Cost</Label>
                                    <Input
                                        type="text"
                                        name="cost"
                                        id="ecost_${i.ingredient_id}"
                                        placeholder="cost"
                                        bind:value={edit_cost}
                                        autocomplete="off"
                                    />
                                </FormGroup>

                    
                            </ModalBody>
                            <ModalFooter style="background-color:grey">
                                <Button color="primary" on:click={edit_ingredient}>Update</Button>
                                <Button color="light" on:click={edit_cancel}>Cancel</Button>
                            </ModalFooter>
                        </Modal>
                    </Col>
                    <Col>
                        <Button color="warning" on:click={() => delete_ingredient(i.ingredient_id)}>Delete</Button> 
                    </Col>                    
                </Row>

                        
                
            </td>            
        </tr>
        {/each}
    </tbody>
</Table>