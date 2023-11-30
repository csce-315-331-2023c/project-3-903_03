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
  } from 'sveltestrap';

  let id = 2;
  let customer_orders = [];
  let statuses = {};

  onMount(() => {
    get_statuses();
    get_data();
    return () => {}; 
  });

  async function get_statuses() {
    const input = `/customer/order_history/get_statuses`;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      for (let status of data.statuses)
        statuses[status.status_id] = status.status;
      console.log(statuses);
    } else {
      console.log(data.error);
    }
  }

  async function get_customer_orders(id) {
    const input = `/customer/order_history/get_customer_orders?id=${id}`;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      return data.customer_orders;
    } else {
      console.log(data.error);
    }
  }

  async function get_ordered_items(customer_order_id) {
    const input = `/customer/order_history/get_ordered_items?customer_order_id=${customer_order_id}`;
    const response = await fetch(input);
    const data = await response.json();
    if (data.success) {
      return data.ordered_items;
    } else {
      console.log(data.error);
    }
  }

  async function get_data() {
    let orders = await get_customer_orders(id);
    if (orders) {
      for (let order of orders) {
        let items = await get_ordered_items(order.customer_order_id);
        if (items) {
          let customer_order = {
            customer_order_id : order.customer_order_id,
            order_date : order.order_date,
            order_time : order.order_time,
            cost : order.cost,
            status_id : order.status_id,
            items : items,
          };
          customer_orders = [...customer_orders, customer_order];
        }
      }
    }
  }

  </script>
  
  <title>Customer Order History</title>
  
  <header>
    Order History
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
              </CardTitle>
              <CardText style="font-size: 20px">
               
              </CardText>
              
            </CardHeader>
            <CardBody>
              <Table bordered style="font-size: 30px">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Qty</th>
                  </tr>
                </thead>
                <tbody>
                    {#each customer_order.items as item }
                      <tr>
                        <td>{item.menu_item_id}</td>
                        <td> 
                          <Image src = {get_image(item.menu_item_id)} alt = {item.name} width="64" height="64"/>
                        </td>
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
  