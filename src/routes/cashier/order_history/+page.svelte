<script>
  import { onMount } from 'svelte';
  import {
    Button,
    Card,
    CardBody,
    CardFooter,
    CardHeader,
    Table,
    CardTitle,
    Row,
    Col,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader,
    Input,
    InputGroup,
    InputGroupText,
    Container,
    ListGroup,
    ListGroupItem
  } from 'sveltestrap';

  import { auth } from '$lib/auth.js';
  import { goto } from '$app/navigation';
  let category;
  let id;
  $: category = $auth.category
  $: id = $auth.id;
  $: if (!import.meta.env.SSR &&  category !== 'cashier')
      goto(`/login`, { replace: true });

  let open_filter = false;
  const toggle_filter = () => (open_filter = !open_filter);

  let modal_open = false;
  const modal_toggle = () => (modal_open = !modal_open);

  let customer_orders = [];
  let statuses = {};
  let from_date = "";
  let to_date = "";
  let checked_statuses = [true, true, true];

  onMount(() => {
    get_statuses();
    get_orders_total(id, from_date, to_date, checked_statuses);
    get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
    return () => {}; 
  });

  
  async function get_statuses() {
    const input = `/cashier/order_history/get_statuses`;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      for (let status of data.statuses)
        statuses[status.status_id] = status.status;
    } else {
      console.log(data.error);
    }
  }

  const limit = 25; 
  let first = 1;
  let last = 25;
  let total = 0;
  let offset = 0;
  let page = 0;

  async function get_orders_total(id, from_date, to_date, checked_statuses) {
    const route_input = `/cashier/order_history/get_orders_total`;
    const id_input = `id=${id}`;
    const from_date_input = `from_date=${from_date}`;
    const to_date_input = `to_date=${to_date}`;
    const pending_input= `pending=${checked_statuses[0]}`;
    const completed_input= `completed=${checked_statuses[1]}`;
    const canceled_input= `canceled=${checked_statuses[2]}`;
    const input = route_input + `?` + id_input 
                              + `&` + from_date_input 
                              + `&` + to_date_input
                              + `&` + pending_input
                              + `&` + completed_input
                              + `&` + canceled_input;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      total = parseInt(data.total);
      if (total == 0) {
        first = 0;
        last = 0;
        offset = 0;
      } else {
        let pages = Math.floor((total + limit - 1) / limit);
        if (page >= pages)
          page = pages - 1;
        offset = page * limit;
        first = offset + 1;
        last = first + limit - 1;
        if (last >= total)
          last = total;
      }
    } else {
      console.log(data.error);
    }
  }  

  async function get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit) {
    const route_input = `/cashier/order_history/get_customer_orders`;
    const id_input = `id=${id}`;
    const from_date_input = `from_date=${from_date}`;
    const to_date_input = `to_date=${to_date}`;
    const pending_input= `pending=${checked_statuses[0]}`;
    const completed_input= `completed=${checked_statuses[1]}`;
    const canceled_input= `canceled=${checked_statuses[2]}`;
    const offset_input= `offset=${offset}`;
    const limit_input= `limit=${limit}`;
    const input = route_input + `?` + id_input 
                              + `&` + from_date_input 
                              + `&` + to_date_input
                              + `&` + pending_input
                              + `&` + completed_input
                              + `&` + canceled_input
                              + `&` + offset_input
                              + `&` + limit_input;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      customer_orders = data.customer_orders;
      customer_orders = [...customer_orders];
    } else {
      console.log(data.error);
    }
  }

  function toggle_statuses(index) {
    checked_statuses[index] = !checked_statuses[index];
  }

  function filter() {
    toggle_filter();
    get_orders_total(id, from_date, to_date, checked_statuses);
    get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
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
        const response = await fetch('/cashier/order_history/delete_order', options);
        await response.json();

        get_orders_total(id, from_date, to_date, checked_statuses);
        get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
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
        const response = await fetch('/cashier/order_history/patch_order', options);
        await response.json();
        get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
  }

  async function update_order(customer_order_id, status_id) {
    if (await patch_ingredients(customer_order_id, status_id)) {
      patch_order(customer_order_id, status_id);
    }
    else {
      // not enough ingredients
      modal_toggle();
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
        const response = await fetch('/cashier/order_history/patch_ingredients', options);
        const result = await response.json();
        if (result.success)
          return true;
        return false;
  }

  function first_page () {
    if (page == 0)
      return;
    page = 0;
    offset = page * limit;
    first = offset + 1;
    last = first + limit - 1;
    if (last >= total)
      last = total;
    get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
  }
  
  function prev_page () {
    if (page == 0)
      return;
    page--;
    offset = page * limit;
    first = offset + 1;
    last = first + limit - 1;
    if (last >= total)
      last = total;
    get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
  }

  function next_page () {
    if (total == 0)
      return;
    let pages = Math.floor((total + limit - 1) / limit);
    if (page + 1 == pages)
      return;
    if (page + 1 < pages)
      page++;
    offset = page * limit;
    first = offset + 1;
    last = first + limit - 1;
    if (last >= total)
      last = total;
    get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
  }

  function last_page () {
    if (total == 0)
      return;
    let pages = Math.floor((total + limit - 1) / limit);
    if (page + 1 == pages)
      return;
    page = pages - 1;
    offset = page * limit;
    first = offset + 1;
    last = first + limit - 1;
    if (last >= total)
      last = total;
    get_customer_orders(id, from_date, to_date, checked_statuses, offset, limit);
  }

  let size = 'lg';
  </script>
  
  <title>Cashier Order History</title>

  <header>
    Order History
  </header>
  <Container style="font-size: 20px; width: 50%; margin-right: 25%;">
    <Button on:click={toggle_filter}>Filter</Button>
    <Modal isOpen={open_filter} backdrop="static" {toggle_filter} {size}>
      <ModalHeader {toggle_filter}>Filter</ModalHeader>
      <ModalBody>
        <ListGroup>
          <InputGroup>
              <InputGroupText>From Date:</InputGroupText>
              <Input id="from_date" type = "date" bind:value={from_date} autocomplete="off"/>
              &nbsp &nbsp
              <InputGroupText>To Date:</InputGroupText>
              <Input id="to_date" type = "date" bind:value={to_date} autocomplete="off"/>               
          </InputGroup>
          &nbsp
          <InputGroup>
              <Col>Status:</Col>
              <Col>
                <Input 
                  id="pending" 
                  type="checkbox" 
                  label="Pending"
                  bind:checked={checked_statuses[0]}
                  on:change={() => toggle_statuses(0)}
                />
              </Col>
              <Col>
                <Input
                  id="completed"
                  type="checkbox"
                  label="Completed"
                  bind:checked={checked_statuses[1]}
                  on:change={() => toggle_statuses(1)}
                />
              </Col>
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
    <Button color="primary" on:click={first_page}>&lt;&lt;</Button>
    <Button color="primary" on:click={prev_page}>&lt;</Button>
    <Button color="primary" on:click={next_page}>&gt;</Button>
    <Button color="primary" on:click={last_page}>&gt;&gt;</Button>    
    <h>Orders {first}-{last} of {total}</h>    
  </Container>
 
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
    <Modal isOpen={modal_open} {modal_toggle}>
      <ModalHeader {modal_toggle}>Warning</ModalHeader>
      <ModalBody>
        Not enough ingredients to complete order.
      </ModalBody>
      <ModalFooter>
        <Button color="primary" on:click={modal_toggle}>OK</Button>
      </ModalFooter>
    </Modal>  
  </body>

  <style>
    header {
        text-align: center;
        font-size: 40px;
        padding: 5px;
    }
    body {
      padding:10px; 
    }
</style>
  