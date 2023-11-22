<script lang="ts">
    import { Container, Row, Col, Image } from 'sveltestrap';
    import { Card, CardBody, } from 'sveltestrap';

    export let data;

    let images = [
      {id:1, name:"Chocolate Chip Cookie.jpeg"},
      {id:2, name:"Snickerdoodle Cookie.jpeg"},  
      {id:3, name:"Sugar Cookie with M&Ms Cookie.jpeg"},  
      {id:4, name:"Double Chocolate Chip Cookie.png"},  
      {id:5, name:"Pecan Chocolate Chip Cookie.jpeg"},  
      {id:6, name:"Peanut Butter Cookie.webp"},  
      {id:7, name:"Sugar Cookie.webp"},  
      {id:8, name:"Oatmeal Raisin Cookie.jpeg"},  
      {id:9, name:"Peanut Butter Chocolate Chip Cookie.jpeg"},  
      {id:10, name:"Connor Man Brookie.png"},  
      {id:11, name:"Brownie.jpeg"},  
      {id:12, name:"Peanut Butter Chocolate Bar.jpeg"},  
      {id:13, name:"Salted Caramel Blondie.jpeg"},  
      {id:14, name:"Oatmeal Chocolate Chip Cookie.jpeg"},  
      {id:15, name:"Vanilla Ice Cream Pint.webp"},  
      {id:16, name:"Mint Chocolate Chip Pint.webp"},  
      {id:17, name:"Chocolate Ice Cream Pint.jpeg"},  
      {id:18, name:"Cookies n Cream Pint.jpeg"},  
      {id:19, name:"1p Milk.png"},  
      {id:20, name:"Chocolate Milk.png"},  
      {id:21, name:"Gingerbread Cookie.jpeg"},  
    ];

    function get_image(id) {
      const image = images.find(obj => obj.id === id);
      if (image)
        return `/pictures/${image.name}`;
      else
        return ``;
    }

    let menuitems = data.menu_items;
    menuitems.sort((mi1, mi2) => mi1.menu_item_id - mi2.menu_item_id);
</script>
  
<head>
  <title>Menu Board</title>    
</head>

<div class ="header" style="text-align: center; font-size: 50px">Menu Items</div>

{#each menuitems as menu_item}
  <Card class="mb-2">
    <!-- <CardHeader>
       <p style="text-align: center; font-size: 30px">{menu_item.name} </p>
    </CardHeader> -->
    <CardBody>
      <Container>
        <Row>
          <Col xs="auto">
            <Image src = {get_image(menu_item.menu_item_id)} alt = {menu_item.name} width="196" height="196"/>
          </Col>
          <Col xs="6">
            <p style="font-size: 30px">{menu_item.name} </p>
            <p style="font-size: 20px">Price: {menu_item.price} </p>
            <p style="font-size: 20px">Calories: {menu_item.calories} cals</p>
            <p>Ingredients: {menu_item.ingredients}</p>
          </Col>
        </Row>
      </Container>
    </CardBody>
  </Card>
{/each}    

<!-- <Accordion stayOpen>
  {#each data.menu_items as menu_item}
    <AccordionItem active header={menu_item.name} >
      <Container>
        <Row>
          <Col xs="auto">
            <Image src = "/pictures/Chocolate Chip Cookie.jpeg" alt = {menu_item.name} width="196" height="196"/>
          </Col>
          <Col xs="auto">
            <p style="text-align: center; font-size: 30px">Price: {menu_item.price} </p>
          </Col>
          <Col xs="auto">
            <p style="text-align: center; font-size: 30px">Calories: {menu_item.calories} cals</p>
           </Col>
          <Col xs="6">
            <p>Ingredients: {menu_item.ingredients}</p>
          </Col>
        </Row>
      </Container>
    </AccordionItem> 
  {/each}    
</Accordion>
 -->
