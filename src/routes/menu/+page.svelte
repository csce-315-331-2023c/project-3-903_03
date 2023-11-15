<script lang="ts">
  import { Accordion, AccordionItem } from 'sveltestrap';
  import Nav from "../Nav.svelte";

  export let data;

</script>
  
<head>
  <title>Menu Board</title>    
</head>
<Nav />

<div class ="header" style="text-align: center; font-size: 30px">Menu Items</div>


<Accordion stayOpen>
  {#each data.menu_items as menu_item}
    <AccordionItem header={menu_item.name}>
      <img src = "favicon.png" alt = {menu_item.name}>
      <p>Price: {menu_item.price}</p>
      <p>Calories: {menu_item.calories} cals</p>
      <p>Ingredients: {menu_item.ingredients}</p>
    </AccordionItem> 
  {/each}    

</Accordion>

