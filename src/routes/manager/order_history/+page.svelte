<script>
  import { onMount } from 'svelte';
  import { ListGroup, ListGroupItem } from 'sveltestrap';
  import { Table, Image } from 'sveltestrap';
  import { get_image } from "$lib/item_images";
  import {
    Button,
    Card,
    CardBody,
    CardFooter,
    CardHeader,
    CardSubtitle,
    CardText,
    CardTitle,
    Row,
    Col,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader,
    Input,
    InputGroup,
    InputGroupText
  } from 'sveltestrap';
	import { get_custom_elements_slots, get_spread_update } from 'svelte/internal';

  let open_filter = false;
  const toggle_filter = () => (open_filter = !open_filter);

  let name = 'Sophia Dronova';
  let id = 2;
  let customer_orders = [];
  let statuses = {};
  let from_date = "";
  let to_date = "";
  let checked_statuses = [true, true, true];

  onMount(() => {
    get_statuses();
    get_customer_orders(from_date, to_date, checked_statuses);
    return () => {}; 
  });

  
  async function get_statuses() {
    const input = `/manager/order_history/get_statuses`;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      for (let status of data.statuses)
        statuses[status.status_id] = status.status;
    } else {
      console.log(data.error);
    }
  }

  async function get_customer_orders(from_date, to_date, checked_statuses) {
    const route_input = `/manager/order_history/get_customer_orders`;
    const from_date_input = `from_date=${from_date}`;
    const to_date_input = `to_date=${to_date}`;
    const pending_input= `pending=${checked_statuses[0]}`;
    const completed_input= `completed=${checked_statuses[1]}`;
    const canceled_input= `canceled=${checked_statuses[2]}`;
    const input = route_input + `?` + from_date_input 
                              + `&` + to_date_input
                              + `&` + pending_input
                              + `&` + completed_input
                              + `&` + canceled_input;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      customer_orders = data.customer_orders;
      customer_orders = [...customer_orders];
    } else {
      console.log(data.error);
    }
  }

  async function get_ordered_items(customer_order_id) {
    const route_input = `/manager/order_history/get_ordered_items`;
    const customer_order_id_input = `customer_order_id=${customer_order_id}`;
    const input = route_input + `?` + customer_order_id_input;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      return data.ordered_items;
    } else {
      console.log(data.error);
    }
  }

  function toggle_statuses(index) {
    checked_statuses[index] = !checked_statuses[index];
  }

  function filter() {
    toggle_filter();
    get_customer_orders(from_date, to_date, checked_statuses);
  }

  async function delete_order(customer_order_id) {
    const data = {
            customer_order_id: customer_order_id,
        };
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/order_history/delete_order', options);
        await response.json();
        get_customer_orders(from_date, to_date, checked_statuses);
  }

  async function patch_order(customer_order_id, status_id) {
        const data = {
            customer_order_id: customer_order_id,
            status_id : status_id,
        };
        const options = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/order_history/patch_order', options);
        await response.json();
        get_customer_orders(from_date, to_date, checked_statuses);
  }

  async function update_order(customer_order_id, status_id) {
    if (patch_ingredients(customer_order_id, status_id)) 
      patch_order(customer_order_id, status_id);
    else {
      console.log("Not enough ingredients");
    }
  }

  async function patch_ingredients(customer_order_id, status_id) {
    if (status_id != 1)
      return true;
      const data = {
            customer_order_id: customer_order_id,
        };
        const options = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        };
        const response = await fetch('/manager/order_history/patch_ingredients', options);
        const result = await response.json();
        if (result.success)
          return true;
        return false;
  }

  let size = 'lg';
  </script>
  
  <title>Manager Order History</title>

  <header>
    All Orders

    <Button on:click={toggle_filter}>Filter</Button>
    <Modal isOpen={open_filter} backdrop="static" {toggle_filter} {size}>
      <ModalHeader {toggle_filter}>Filter</ModalHeader>
      <ModalBody>
        <ListGroup>
            <InputGroup>
                <InputGroupText>From Date:</InputGroupText>
                <Input id="from_date" type = "date" bind:value={from_date} autocomplete="off"/>
                &nbsp &nbsp 
                <Col>
                  <Input
                    id="canceled"
                    type="checkbox"
                    label="Canceled"
                    bind:checked={checked_statuses[2]}
                    on:change={() => toggle_statuses(2)}
                  />
                </Col>                                      
            </InputGroup>
        </ListGroup>
        
      </ModalBody>
      <ModalFooter>
        <Button color="primary" on:click={filter}>Apply</Button>
        <Button color="secondary" on:click={toggle_filter}>Cancel</Button>
      </ModalFooter>
    </Modal>
  </header>
 
  <body>
    &nbsp
    <ListGroup>
      {#each customer_orders as customer_order}
        <ListGroupItem style="font-size: 30px; width: 50%; margin-left: 25%">
          <Card>
            <CardHeader>
              <CardTitle style="font-size: 35px">
                Order #{customer_order.customer_order_id} {statuses[customer_order.status_id]}

                <Button style="float:right; margin:5px;" color="danger" on:click={() => delete_order(customer_order.customer_order_id)}>Delete</Button>
                {#if customer_order.status_id == 0}
                  <Button style="float:right; margin:5px" color="warning" on:click={() => update_order(customer_order.customer_order_id, 2)}>Cancel</Button>
                  <Button style="float:right; margin:5px" color="success" on:click={() => update_order(customer_order.customer_order_id, 1)}>Complete</Button>              
                {/if} 
                
              </CardTitle>
                
            
            </CardHeader>

            <CardBody>
              <Table bordered style="font-size: 30px">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Qty</th>
                  </tr>
                </thead>
                <tbody>
                    {#each customer_order.items as item }
                      <tr>
                        <td>{item.menu_item_id}</td>
                        <td>{item.name}</td>
                        <td>{item.amount}</td>  
                      </tr>
                    {/each} 
                </tbody>
               </Table>
              </CardBody>
            <CardFooter style="font-size: 20px">
              <Row>
                <Col>
                  Placed on {customer_order.order_date} 
                  at {customer_order.order_time}                
                </Col>
                <Col style="text-align: right">
                  Total Cost: {customer_order.cost} 
                </Col>
              </Row>
 
            </CardFooter>
            
          </Card>
        </ListGroupItem>
      {/each}
    </ListGroup>  
  </body>

  <style>
    header {
        text-align: center;
        font-size: 40px;
        padding: 5px;
    }
    body {
      background-color: #f5f5f5; 
    }
</style>
  