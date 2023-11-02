<script lang="ts">
    import { Accordion, AccordionItem } from 'sveltestrap';
    import Nav from "../Nav.svelte"

    const menu_items = [
      {id : 1, name : 'Chocolate Chip Cookie', price : 1.00, calories : 50 },
      {id : 2, name : 'Mint Chocolate Chip Cookie', price : 1.25, calories : 75 },
      {id : 3, name : 'Vanilla Ice Cream', price : 2.50, calories : 150 }
      ]
    
  </script>
  <Nav />
  <head>
    <title>Menu Board</title>    
  </head>


  <div class ="header" style="text-align: center; font-size: 30px">Menu Items</div>


  <Accordion stayOpen>
    <!-- <AccordionItem header="Alpha" style="align: center">Alpha Flight</AccordionItem>
    <AccordionItem header="Bravo" style="margin: 0px auto">Johnny Bravo</AccordionItem>
    <AccordionItem header="Charlie">Charlie Brown</AccordionItem>
    <AccordionItem header="Delta">Delta Force</AccordionItem>
    <AccordionItem header="Echo">Echo and the Bunnymen</AccordionItem> -->
	{#each menu_items as menu_item}
      <AccordionItem header={menu_item.name}>
        <img src = "favicon.png" alt = {menu_item.name}>
        <p>price: ${menu_item.price}</p>
        <p>calories: {menu_item.calories} cals</p>
      </AccordionItem> 
	{/each}    

  </Accordion>
