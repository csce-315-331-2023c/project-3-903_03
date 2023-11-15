<script>
    import Nav from "../../Nav.svelte";
    import { onMount } from 'svelte';

    onMount(() => {
        get_ingredients();
        return () => {
        };
    });

    import {
        Table, 
        Button,
        Modal,
        ModalBody,
        ModalFooter,
        ModalHeader,
        Form,
        FormGroup,
        FormText,
        Input,
        Label
    } from 'sveltestrap';

    let name = 'Philip Ritchey'

    let ingredients = [];

    let mi_name = '';
    let mi_price = '';
    let mi_calories = 0;
    let mi_ingredients = [];

    let open_modal = false;
    let editable_row = null;
    let open_add = false;

    async function get_ingredients() {
        let input = `/manager/menu_item/get_ingredients`;
        const response = await fetch(input);
        ingredients = await response.json();
    }

    function toggle_menu_item() {
        open_add = !open_add;
    }

    function cancel_menu_item() {
        toggle_menu_item();
    }

    async function add_item_ingredients(id) {
        const data = {
            id: id,
            ingredients: mi_ingredients,
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        console.log(mi_ingredients);
        const response = await fetch('/manager/menu_item/post_ingredients', options);
        await response.json();
    }

    async function add_menu_item() {
        toggle_menu_item();
        const data = {
            name: mi_name,
            price: mi_price,
            calories: mi_calories,
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/menu_item/post', options);
        let value = await response.json();
        add_item_ingredients(value.id);
        mi_name = '';
        mi_price = 0;
        mi_calories = 0;
    }

    function handleCheckboxChange(event, item) {
        if (event.target.checked) {
            mi_ingredients = [...mi_ingredients, item];
        } else {
            mi_ingredients = mi_ingredients.filter(ingredient_group => ingredient_group !== item);
        }
    }


    let open_edit = false;
    let e_mi_name = '';
    let e_mi_price = '';
    let e_mi_calories = 0;

    function toggle_edit() {
        open_edit = !open_edit;
    }

    function toggle(menu_item) {
        open_edit = !open_edit;
        e_mi_name = menu_item.name;
        e_mi_price = menu_item.price;
        e_mi_calories = menu_item.calories;
    }

    function cancel_edit() {
        toggle_edit();
    }


    async function update_row(row) {
        console.log(row);
        const data = {
            id: row.menu_item_id,
            name: row.name,
            price : row.price,
        };
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
    <header style="text-align:center; font-size:25px">Menu Items</header>
</div>

<div>
    <Button color="primary" style="margin-left:25px" on:click={toggle_menu_item}>Add New Menu Item</Button>
    <Modal isOpen={open_add} backdrop={false} {toggle_menu_item} >
        <ModalHeader style="background-color:gray; color:white" {toggle_menu_item} >Add New Menu Item</ModalHeader>
        <ModalBody style="background-color:lightgray">
            <FormGroup>
                <Label for="name">Name</Label>
                <Input
                    type="text"
                    name="name"
                    id="name"
                    placeholder="name"
                    bind:value={mi_name}
                />
            </FormGroup>

            <FormGroup>
                <Label for="price">Price</Label>
                <Input
                    type="text"
                    name="price"
                    id="price"
                    placeholder="price"
                    bind:value={mi_price}
                />
            </FormGroup>

            <FormGroup>
                <Label for="calories">Calories</Label>
                <Input
                    type="number"
                    name="calories"
                    id="calories"
                    placeholder="calories"
                    bind:value={mi_calories}
                />
            </FormGroup>

            <FormGroup>
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
            </FormGroup>
            

        </ModalBody>
        <ModalFooter style="background-color:grey">
            <Button color="primary" on:click={add_menu_item}>Add New Item</Button>
            <Button color="light" on:click={cancel_menu_item}>Cancel</Button>
        </ModalFooter>
    </Modal>
</div>
&nbsp 

<Table bordered>
    <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Price</th>
            <th>Calories</th>
            <th>Ingredients</th>
            <th>Edit Button</th>
        </tr>
    </thead>
    <tbody>
        {#each menu_items as menu_item }
        <tr>
            <td>{menu_item.menu_item_id}</td>
            <td>
                <!-- {#if menu_item === editable_row}
                    <input type="text" bind:value={menu_item.name}/>
                {:else}
                    {menu_item.name}
                {/if} -->
                {menu_item.name}
            </td>
            <td>              
                <!-- {#if menu_item === editable_row}
                    <input type="text" pattern="^\$\d{1,3}(,\d{3})*(\.\d+)?$" bind:value={menu_item.price}/>
                {:else}
                    {menu_item.price}
                {/if} -->
                {menu_item.price}
            </td>    
            <td>{menu_item.calories}</td>
            <td>{menu_item.ingredients}</td>
            <td>
                <!-- <div>
                    <Button color="primary" style="margin-left:25px" on:click={toggle(menu_item)}>Edit</Button>
                    <Modal isOpen={open_edit} backdrop={false} {toggle_edit} >
                        <ModalHeader style="background-color:gray; color:white" {toggle_edit} >Edit Menu Item</ModalHeader>
                        <ModalBody style="background-color:lightgray">
                            <FormGroup>
                                <Label for="name">Name</Label>
                                <Input
                                    type="text"
                                    name="name"
                                    id="name"
                                    placeholder="name"
                                    bind:value={e_mi_name}
                                />
                            </FormGroup>
                
                            <FormGroup>
                                <Label for="price">Price</Label>
                                <Input
                                    type="text"
                                    name="price"
                                    id="price"
                                    placeholder="price"
                                    bind:value={e_mi_price}
                                />
                            </FormGroup>
                
                            <FormGroup>
                                <Label for="calories">Calories</Label>
                                <Input
                                    type="number"
                                    name="calories"
                                    id="calories"
                                    placeholder="calories"
                                    bind:value={e_mi_calories}
                                />
                            </FormGroup>
                
                        </ModalBody>
                        <ModalFooter style="background-color:grey">
                            <Button color="primary" on:click={update_row}>Update</Button>
                            <Button color="light" on:click={cancel_edit}>Cancel</Button>
                        </ModalFooter>
                    </Modal>
                </div> -->
            </td>
        </tr>
        {/each}
    </tbody>
</Table>
