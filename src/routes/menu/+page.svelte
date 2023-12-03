<script>
    import { Container, Row, Col, Image } from 'sveltestrap';
    import { Card, CardBody, } from 'sveltestrap';
    import { get_image } from "$lib/item_images";

    export let data;

    let menuitems = data.menu_items;
    menuitems.sort((mi1, mi2) => mi1.menu_item_id - mi2.menu_item_id);
</script>
  
<head>
	<title>Menu Board</title>
</head>

<div class ="header" style="text-align: center; font-size: 40px">Menu Items</div>
&nbsp
<Card style="margin-left:25%; margin-right:25%;">
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
</Card>