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
	import { get_spread_update } from 'svelte/internal';

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
    get_customer_orders(id, from_date, to_date, checked_statuses);
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

  async function get_customer_orders(id, from_date, to_date, checked_statuses) {
    const route_input = `/cashier/order_history/get_customer_orders`;
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
      customer_orders = data.customer_orders;
      customer_orders = [...customer_orders];
    } else {
      console.log(data.error);
    }
  }

  async function get_ordered_items(customer_order_id) {
    const route_input = `/cashier/order_history/get_ordered_items`;
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
    get_customer_orders(id, from_date, to_date, checked_statuses);
  }

  let size = 'lg';
  </script>
  
  <title>Cashier Order History</title>
  
  <header>
    Cashier, { name }
  </header>
  <header>
    Order History
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
  </header>
 
  <body>
    <ListGroup>
      {#each customer_orders as customer_order}
        <ListGroupItem style="font-size: 30px; width: 50%; margin-left: 25%">
          <Card>
            <CardHeader>
              <CardTitle style="font-size: 35px">
                Order #{customer_order.customer_order_id} {statuses[customer_order.status_id]}
              </CardTitle>
              <CardText style="font-size: 20px">
               
              </CardText>
              
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
        font-size: 50px;
        padding: 5px;
    }
    body {
      background-color: #f5f5f5; 
    }
</style>
  