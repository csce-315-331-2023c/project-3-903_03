<script lang="ts">
  import { Accordion, AccordionItem } from 'sveltestrap';

  export let data;

</script>
  
<head>
  <title>Menu Board</title>    
</head>

<div class ="header" style="text-align: center; font-size: 30px">Menu Items</div>


<Accordion stayOpen>
  {#each data.menu_items as menu_item}
    <AccordionItem header={menu_item.name}>
      <img src = "favicon.png" alt = {menu_item.name}>
      <p>price: {menu_item.price}</p>
      <p>calories: {menu_item.calories} cals</p>
    </AccordionItem> 
  {/each}    

</Accordion>

