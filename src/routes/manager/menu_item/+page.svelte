<script>
    import { onMount } from 'svelte';
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

    onMount(() => {
        get_ingredients();
        return () => {
        };
    });

    async function get_ingredients() {
        let input = `/manager/menu_item/get_ingredients`;
        const response = await fetch(input);
        ingredients = await response.json();
    }

    let name = 'Philip Ritchey'

    let ingredients = [];

    let add_name = '';
    let add_price = '';
    let add_calories = 0;
    let add_season = 'None';
    let add_ingredients = [];
    let add_open = false;

    function add_toggle() {
        add_open = !add_open;
    }

    function add_cancel() {
        add_toggle();
    }

    async function add_item_ingredients(id) {
        const data = {
            id: id,
            ingredients: add_ingredients,
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        console.log(add_ingredients);
        const response = await fetch('/manager/menu_item/post_ingredients', options);
        await response.json();
    }

    async function add_menu_item() {
        add_toggle();
        const data = {
            name: add_name,
            price: add_price,
            calories: add_calories,
            season: add_season,
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
        add_name = '';
        add_price = 0;
        add_calories = 0;
        add_season = '';
    }

    function handleCheckboxChange(event, ingredient) {
        if (event.target.checked) {
            add_ingredients = [...add_ingredients, ingredient];
        } else {
            add_ingredients = add_ingredients.filter(ingredient_group => ingredient_group !== ingredient);
        }
    }


    let edit_open = false;
    let edit_id = 0;
    let edit_name = '';
    let edit_price = '';
    let edit_calories = 0;
    let edit_season = '';

    function edit_toggle(menu_item) {
        edit_open = !edit_open;
        edit_id = menu_item.menu_item_id;
        edit_name = menu_item.name;
        edit_price = menu_item.price;
        edit_calories = menu_item.calories;
        edit_season = menu_item.season;
    }

    function edit_cancel() {
        edit_open = !edit_open;
    }

    async function edit_menu_item() {
        edit_open = !edit_open;
        const data = {
            id: edit_id,
            name: edit_name,
            price: edit_price,
            calories: edit_calories,
            season: edit_season,
        };
        const options = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/menu_item/patch', options);
        await response.json();
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

<div>
    <header >Manager: { name }</header>
</div>

<div>
    <header style="text-align:center; font-size:25px">Menu Items</header>
</div>

<div>
    <Button color="primary" style="margin-left:25px" on:click={add_toggle}>Add New Menu Item</Button>
    <Modal isOpen={add_open} backdrop={false} {add_toggle} >
        <ModalHeader style="background-color:gray; color:white" {add_toggle} >Add New Menu Item</ModalHeader>
        <ModalBody style="background-color:lightgray">
            <FormGroup>
                <Label for="aname">Name</Label>
                <Input
                    type="text"
                    name="name"
                    id="aname"
                    placeholder="name"
                    bind:value={add_name}
                    autocomplete="off"
                />
            </FormGroup>

            <FormGroup>
                <Label for="aprice">Price</Label>
                <Input
                    type="text"
                    name="price"
                    id="aprice"
                    placeholder="price"
                    bind:value={add_price}
                    autocomplete="off"
                />
            </FormGroup>

            <FormGroup>
                <Label for="acalories">Calories</Label>
                <Input
                    type="number"
                    name="calories"
                    id="acalories"
                    placeholder="calories"
                    bind:value={add_calories}
                    autocomplete="off"
                />
            </FormGroup>

            <FormGroup>
                <Label for="aseason">Season</Label>
                <Input
                    type="text"
                    name="season"
                    id="aseason"
                    placeholder="none"
                    bind:value={add_season}
                    autocomplete="off"
                />
            </FormGroup>

            <FormGroup>
                <Label>Ingredients</Label>
                {#each ingredients as i}
                    <Input
                        id={i.ingredient_id}
                        type="checkbox"
                        bind:group={add_ingredients}
                        value="{i.name}"
                        on:change={(event) => handleCheckboxChange(event, i)}
                        label={i.name}
                    />
                {/each}
            </FormGroup>

        </ModalBody>
        <ModalFooter style="background-color:grey">
            <Button color="primary" on:click={add_menu_item}>Add New Item</Button>
            <Button color="light" on:click={add_cancel}>Cancel</Button>
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
            <th>Season</th>
            <th>Ingredients</th>
            <th>Edit Button</th>
        </tr>
    </thead>
    <tbody>
        {#each menu_items as menu_item }
        <tr>
            <td>{menu_item.menu_item_id}</td>
            <td>{menu_item.name}</td>
            <td>{menu_item.price}</td>    
            <td>{menu_item.calories}</td>
            <td>{menu_item.season}</td>
            <td>{menu_item.ingredients}</td>
            <td>
                <div>
                    <Button color="primary" style="margin-left:25px" on:click={() => edit_toggle(menu_item)}>Edit</Button>
                    <Modal isOpen={edit_open} backdrop={false}>
                        <ModalHeader style="background-color:gray; color:white">Edit Menu Item</ModalHeader>
                        <ModalBody style="background-color:lightgray">
                            <FormGroup>
                                <Label for="ename_${menu_item.menu_item_id}">Name</Label>
                                <Input
                                    type="text"
                                    name="name"
                                    id="ename_${menu_item.menu_item_id}"
                                    placeholder="name"
                                    bind:value={edit_name}
                                    autocomplete="off"
                                />
                            </FormGroup>
                
                            <FormGroup>
                                <Label for="eprice_${menu_item.menu_item_id}">Price</Label>
                                <Input
                                    type="text"
                                    name="price"
                                    id="eprice_${menu_item.menu_item_id}"
                                    placeholder="price"
                                    bind:value={edit_price}
                                    autocomplete="off"
                                />
                            </FormGroup>
                
                            <FormGroup>
                                <Label for="ecalories_${menu_item.menu_item_id}">Calories</Label>
                                <Input
                                    type="number"
                                    name="calories"
                                    id="ecalories_${menu_item.menu_item_id}"
                                    placeholder="calories"
                                    bind:value={edit_calories}
                                    autocomplete="off"
                                />
                            </FormGroup>

                            <FormGroup>
                                <Label for="eseason_${menu_item.menu_item_id}">Season</Label>
                                <Input
                                    type="text"
                                    name="season"
                                    id="eseason_${menu_item.menu_item_id}"
                                    placeholder="season"
                                    bind:value={edit_season}
                                    autocomplete="off"
                                />
                            </FormGroup>
                
                        </ModalBody>
                        <ModalFooter style="background-color:grey">
                            <Button color="primary" on:click={edit_menu_item}>Update</Button>
                            <Button color="light" on:click={edit_cancel}>Cancel</Button>
                        </ModalFooter>
                    </Modal>
                </div>
            </td>
        </tr>
        {/each}
    </tbody>
</Table>
