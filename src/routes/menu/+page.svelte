<script lang="ts">
    import { Accordion, AccordionItem } from 'sveltestrap';

    const menu_items = ['Chocolate Chip Cookie', 'Mint Chocolate Chip Cookie', 'Vanilla Ice Cream']
    
  </script>
  
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
       <AccordionItem header={menu_item}>{menu_item}</AccordionItem> 
	{/each}    

  </Accordion>
