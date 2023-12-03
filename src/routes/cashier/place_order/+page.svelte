<script>
  import { onMount } from 'svelte';
  import { InputGroup, InputGroupText, Input } from 'sveltestrap';
  import { Table, Image, Button, Tooltip } from 'sveltestrap';
  import { Col, Container, Row } from 'sveltestrap';
  import { Card, CardBody, CardFooter, CardHeader, CardSubtitle, CardText, CardTitle } from 'sveltestrap';
  import { get_image } from "$lib/item_images";
  import { Modal, ModalBody, ModalFooter, ModalHeader } from 'sveltestrap';
  let modal_open = false;
  const modal_toggle = () => (modal_open = !modal_open);

  import { auth } from '$lib/auth.js';
  import { goto } from '$app/navigation';
  let category;
  let id;
  $: category = $auth.category
  $: id = $auth.id;
  $: if (!import.meta.env.SSR &&  category !== 'cashier')
      goto(`/login`, { replace: true });

  let orderitems = []
  let menuitems = [];
  let total = "$0.00";
  let status_id = 0;

  async function get_menuitems() {
        let input = `/cashier/place_order/get_menu_items`;
        const response = await fetch(input);
        menuitems = await response.json();
        menuitems.sort((a, b) => a.menu_item_id - b.menu_item_id); 
        for (let menuitem of menuitems) {
          menuitem.qty = 0;
          menuitem.subtotal = 0;
        }
    }

  onMount(() => { 
    get_menuitems(); 
    return () => {}; 
  });

    function get_button_name(menuitem) {
      if (menuitem.qty == 0) {
        return '+';
      }
      return '-'
    }

    function get_tooltip(menuitem) {
      if (menuitem.qty == 0) {
        return 'Add Item to Cart';
      }
      return 'Delete Item from Cart'
    }

    function action_cart(menuitem) {
      if (menuitem.qty == 0) {
        menuitem.qty = 1;
        menuitem.subtotal = Number(menuitem.price.slice(1));
        orderitems.push(menuitem);
      } else {
        menuitem.qty = 0;
        orderitems = orderitems.filter(item => item.menu_item_id !== menuitem.menu_item_id);
      }
      orderitems.sort((a, b) => a.menu_item_id - b.menu_item_id); 
      menuitems = [...menuitems] // reactivity
      orderitems = [...orderitems]
      update_total();
    }

    function clear_cart() {
      for (let menuitem of orderitems) 
        menuitem.qty = 0;
      orderitems = [];
      menuitems = [...menuitems] // reactivity
      update_total();
    }

    function place_order() {
      insert_order();
      clear_cart();
    }

    function update_qty(menuitem) {
      if (menuitem.qty == 0) {
        orderitems = orderitems.filter(item => item.menu_item_id !== menuitem.menu_item_id);
        menuitems = [...menuitems] // reactivity
      } else {
        menuitem.subtotal = menuitem.qty * Number(menuitem.price.slice(1));
      }
      orderitems = [...orderitems] 
      update_total();
    }

    function update_total() {
      let sum = 0;
      for (let menuitem of orderitems) {
        sum += menuitem.subtotal;
      }
      total = '$'+ sum.toFixed(2);
    }

    function format_time(date) {
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${hours}:${minutes}:${seconds}`;
    }

    function format_date(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); 
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }

    async function insert_ordered_items(id, items) {
      const data = {
          customer_order_id: id,
          items: items,
      };
     const options = {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify(data),
      };
      const response = await fetch('/cashier/place_order/post_items', options);
    }

    async function insert_order() {
      if (orderitems.length == 0)
        return;

      let items = [];
      for (let menuitem of orderitems) {
        let item = {menu_item_id : menuitem.menu_item_id, amount : menuitem.qty};
        items.push(item);
      }

      const now = new Date();
      const fmt_date = format_date(now);
      const fmt_time = format_time(now);
      const data = {
          id : id, 
          cost : total, 
          order_date : fmt_date, 
          order_time : fmt_time,
          status_id: status_id,  
      };
      const options = {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify(data),
      };
      const response = await fetch('/cashier/place_order/post', options);
      let value = await response.json();
      insert_ordered_items(value.id, items);
      modal_toggle();
    }

</script>


<title>Cashier Place Order</title>

<header style="text-align: center">
  What would you like to order?
</header>

<body>
  <Container>
    <Row>
      <Col xs="6">
        <Card class="mb-3">
          <CardHeader>
            <CardTitle class="fs-2">Menu</CardTitle>
          </CardHeader>
          <CardBody>
            <Table bordered class="fs-3">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th>Price</th>
                  <th>A</th>
                </tr>
              </thead>
              <tbody>
                {#each menuitems as menuitem }
                  <tr>
                    <td>{menuitem.menu_item_id}</td>
                    <td>{menuitem.name}</td>
                    <td>{menuitem.price}</td>  
                    <td>
                      <Button class="fs-3" id={`btn-${menuitem.menu_item_id}`} on:click={() => action_cart(menuitem)}>{get_button_name(menuitem)}</Button>
                      <Tooltip target={`btn-${menuitem.menu_item_id}`} placement="right"> {get_tooltip(menuitem)}</Tooltip>
                    </td>  
                  </tr>
                {/each} 
              </tbody>
            </Table>            
          </CardBody>
          <CardFooter>
          </CardFooter>
        </Card>
      </Col>
      <Col xs="6">
        <Card class="mb-3">
          <CardHeader >
            <CardTitle class="fs-2">Cart</CardTitle>
          </CardHeader>
          <CardBody>
            <Table bordered class="fs-3">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Image</th>
                  <th>Name</th>
                  <th>Qty</th>
                  <th>SubTotal</th>
                </tr>
              </thead>
              <tbody>
                {#each orderitems as orderitem }
                  <tr>
                    <td>{orderitem.menu_item_id}</td>
                    <td> 
                      <Image src = {get_image(orderitem.menu_item_id)} alt = {orderitem.name} width="96" height="96"/>
                    </td>
                    <td>{orderitem.name}</td>
                    <td>
                      <Input id={`inp-${orderitem.menu_item_id}`} bind:value={orderitem.qty} on:change={() => update_qty(orderitem)} min={0} max={72} type="number" step="1" class="fs-4"/>  
                    </td>
                    <td>${orderitem.subtotal.toFixed(2)}</td>
                  </tr>
                {/each} 
              </tbody>
            </Table>
          </CardBody>
          <CardFooter >
            <InputGroup>
              <Button class="fs-4" on:click={clear_cart}>Clear</Button>
              <InputGroupText class="fs-4">Total:</InputGroupText>
              <Input readonly id="total" bind:value={total} class="fs-4"/>
              <Button class="fs-4" on:click={place_order}>Place Order</Button>
            </InputGroup>
          </CardFooter>
        </Card>
      </Col>
    </Row>
  </Container>
  <div>
    <Modal isOpen={modal_open} {modal_toggle}>
      <ModalHeader {modal_toggle}>Order Confirmation</ModalHeader>
      <ModalBody>
        The order is placed.
      </ModalBody>
      <ModalFooter>
        <Button color="primary" on:click={modal_toggle}>OK</Button>
      </ModalFooter>
    </Modal>
  </div>
</body>

<style>
    header {
        text-align: center;
        font-size: 25px;
        padding: 20px;
    }
    body {
      background-color: #dbdbdd; 
    }
</style>
