<script>
    import { Button, Table } from 'sveltestrap';
    import { Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Label } from 'sveltestrap';
    import { Card, CardHeader, CardBody, CardFooter } from 'sveltestrap';
    import { onMount } from 'svelte';

    onMount(() => {
        get_categories();
        get_users();
        return () => {
        };
    });

    let categories = {};
    let users = [];

    async function get_users() {
        let input = `/admin/get_users`;
        const response = await fetch(input);
        users = await response.json();
        if (users) {
            users.sort((a, b) => a.id - b.id);
        }
    }

    async function get_categories() {
        const input = `/admin/get_categories`;
        const response = await fetch(input);
        const data = await response.json();
        if (data.success) {
            for (let category of data.categories)
                categories[category.category_id] = category.name;
        } else {
            console.log(data.error);
        }
    }

    function getKeyByValue(object, value) {
        return Object.keys(object).find(key => object[key] === value);
    }

    let add_open = false;
    let add_name = '';
    let add_username = '';
    let add_password = '';
    let add_category = categories[3];

    function add_toggle() {
        add_name = '';
        add_username = '';
        add_password = '';
        add_category = categories[3];
        add_open = !add_open;
    }

    function add_cancel() {
        add_open = !add_open;
    }

    async function add_user() {
        if (add_name == '' & add_username == '' | add_password == '')
            return;

        add_open = !add_open;
        const data = {
            name: add_name,
            username: add_username,
            password: add_password,
            category_id: getKeyByValue(categories, add_category),
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/admin/post', options);
        await response.json();
        get_users();
    }
    
    let edit_open = false;
    let edit_id = 0;
    let edit_name = '';
    let edit_username = '';
    let edit_password = '';
    let edit_category = '';

    function edit_toggle(user) {
        edit_open = !edit_open;
        edit_id = user.id;
        edit_name = user.name;
        edit_username = user.username;
        edit_password = user.password;
        edit_category = categories[user.category_id];
    }

    function edit_cancel() {
        edit_open = !edit_open;
    }

    async function edit_user() {
        edit_open = !edit_open;
        const data = {
            id: edit_id,
            name: edit_name,
            username: edit_username,
            password: edit_password,
            category_id: getKeyByValue(categories, edit_category),
        };
        const options = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/admin/patch', options);
        await response.json();
        get_users();
    }


</script>


<title>Admin</title> 

<header>Users</header>

<body>
    <Card style="font-size: 20px; width: 60%; margin-left: 20%; margin-right: 20%">
        <CardHeader>
            <Button color="primary" on:click={() => add_toggle()}>Add New User</Button>
            <Modal isOpen={add_open} backdrop={false}>
                <ModalHeader style="background-color:gray; color:white">Add New User</ModalHeader>
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
                        <Label for="ausername">Username</Label>
                        <Input
                            type="text"
                            name="username"
                            id="ausername"
                            placeholder="username"
                            bind:value={add_username}
                            autocomplete="off"
                        />
                    </FormGroup>
        
                    <FormGroup>
                        <Label for="apassword">Password</Label>
                        <Input
                            type="text"
                            name="password"
                            id="apassword"
                            placeholder="password"
                            bind:value={add_password}
                            autocomplete="off"
                        />
                    </FormGroup>

                    <FormGroup>
                        <Label for="acategory">Category</Label>
                        <Input
                            type="select"
                            name="category"
                            id="acategory"
                            placeholder="category"
                            bind:value={add_category}
                            autocomplete="off"
                        >
                            <option>{categories[1]}</option>
                            <option>{categories[2]}</option>
                            <option>{categories[3]}</option>
                        </Input>
                    </FormGroup>
        
                </ModalBody>
                <ModalFooter style="background-color:grey">
                    <Button color="primary" on:click={add_user}>Add</Button>
                    <Button color="light" on:click={add_cancel}>Cancel</Button>
                </ModalFooter>
            </Modal>
        </CardHeader>
        <CardBody>
            <Table bordered>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Category</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {#each users as user }
                        <tr>
                            <td>{user.id}</td>
                            <td>{user.name}</td>
                            <td>{user.username}</td>    
                            <td>{user.password}</td>
                            <td>{categories[user.category_id]}</td>
                            <td>
                                {#if categories[user.category_id] !== 'guest'}
                                    <Button color="primary" on:click={() => edit_toggle(user)}>Edit</Button>
                                {/if}
                                <Modal isOpen={edit_open} backdrop={false}>
                                    <ModalHeader style="background-color:gray; color:white">Edit User</ModalHeader>
                                    <ModalBody style="background-color:lightgray">
                                        <FormGroup>
                                            <Label for="ename_${user.id}">Name</Label>
                                            <Input
                                                readonly={edit_category == 'admin'}
                                                type="text"
                                                name="name"
                                                id="ename_${user.id}"
                                                placeholder="name"
                                                bind:value={edit_name}
                                                autocomplete="off"
                                            />
                                        </FormGroup>
                            
                                        <FormGroup>
                                            <Label for="eusername_${user.id}">Username</Label>
                                            <Input
                                                readonly={edit_category == 'admin'}
                                                type="text"
                                                name="username"
                                                id="eusername_${user.id}"
                                                placeholder="username"
                                                bind:value={edit_username}
                                                autocomplete="off"
                                            />
                                        </FormGroup>
                            
                                        <FormGroup>
                                            <Label for="epassword_${user.id}">Password</Label>
                                            <Input
                                                type="text"
                                                name="password"
                                                id="epassword_${user.id}"
                                                placeholder="password"
                                                bind:value={edit_password}
                                                autocomplete="off"
                                            />
                                        </FormGroup>

                                        <FormGroup>
                                            <Label for="ecategory_${user.id}">Category</Label>
                                            <Input
                                                disabled={edit_category == 'admin'}
                                                type="select"
                                                name="category"
                                                id="ecategory_${user.id}"
                                                placeholder="category"
                                                bind:value={edit_category}
                                                autocomplete="off"
                                            >
                                                {#if edit_category == 'admin'}
                                                    <option>{categories[0]}</option>
                                                {/if}  
                                                <option>{categories[1]}</option>
                                                <option>{categories[2]}</option>
                                                <option>{categories[3]}</option>
                                           
                                            </Input>
                                        </FormGroup>
                            
                                    </ModalBody>
                                    <ModalFooter style="background-color:grey">
                                        <Button color="primary" on:click={edit_user}>Update</Button>
                                        <Button color="light" on:click={edit_cancel}>Cancel</Button>
                                    </ModalFooter>
                                </Modal>
                            </td> 
                        </tr>
                    {/each}
                </tbody>
            </Table>
        </CardBody>
        <CardFooter>
        </CardFooter>
    </Card>
</body>

<style>
    header {
        text-align: center;
        font-size: 40px;
        padding: 5px;
    }

</style>
