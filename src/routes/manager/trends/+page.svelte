<script lang="ts">
    import Nav from "../../Nav.svelte";

    let name = 'Philip Ritchey'
    

    import {
        DropdownMenu,
        DropdownToggle,
		ButtonDropdown,
        Modal,
        ModalBody,
        ModalFooter,
        ModalHeader,
        Button,
        Form,
        FormGroup,
        FormText,
        Input,
        Label
    } from 'sveltestrap';

    let openUsage = false;
    let openSales = false;

    let fullscreen;

    const toggleUsage = () => {
        fullscreen = true;
        openUsage = !openUsage;
    };

    const toggleSales = () => {
        fullscreen = true;
        openSales = !openSales;
    };

    const closeUsage = () => {
        fullscreen = undefined;
        openUsage = !openUsage;
    };

    const closeSales = () => {
        fullscreen = undefined;
        openSales= !openSales;
    };

</script>

<style>
    header {
        text-align: left;
        font-size: 20px;
        padding: 10px;
    }

</style>

<title>Manager: Trends</title>
<Nav />

<div>
    <header >Manager: { name }</header>
</div>

<div>
    <FormGroup style="width:455px; margin-left:15%">
        <Label for="selectReport" style="font-weight:bold">Select Report</Label>
        <Input type="select" name="select" id="selectReport">
        <option value="usage">Usage</option>
        <option>Sales</option>
        <option>Excess</option>
        <option>Together</option>
        <option>Restock</option>
        </Input>
    </FormGroup>
</div>

<div style="display:inline">
    <FormGroup style="width:200px">
        <Label for="fromDate" style="font-weight:bold">From Date</Label>
        <Input
        type="date"
        name="date"
        id="fromDate"
        placeholder="date placeholder"
        />
    </FormGroup>

    <FormGroup style="width:200px">
        <Label for="toDate" style="font-weight:bold">To Date</Label>
        <Input
        type="date"
        name="date"
        id="toDate"
        placeholder="date placeholder"
        />
    </FormGroup>    
</div>




<div>&nbsp</div>
<!-- <div style="width:100%; float:center">
    <Button on:click={toggleUsage} style="width:200px">Generate</Button>   
</div> -->


<!-- <ButtonDropdown>
    <DropdownToggle caret style="width:200px">Trends</DropdownToggle>
    <DropdownMenu>
        <button style="width:200px; text-align:center; border:none" on:click={toggleUsage}>Usage</button>
        <button style="width:200px; text-align:center; border:none" on:click={toggleSales}>Sales</button>
        <button style="width:200px; text-align:center; border:none">Restock</button>  
        <button style="width:200px; text-align:center; border:none">Excess</button>
        <button style="width:200px; text-align:center; border:none">Together</button>
    </DropdownMenu>    
</ButtonDropdown> -->




<Modal isOpen={openUsage} {fullscreen}>
    <ModalHeader>Usage</ModalHeader>       
    <ModalBody>
        Show usage trend
    </ModalBody>
    <ModalFooter>
      <Button color="secondary" on:click={closeUsage}>Close</Button>
    </ModalFooter>
</Modal>

<Modal isOpen={openSales} {fullscreen}>
    <ModalHeader >Sales</ModalHeader>   
    <ModalBody>
        Show sales trend
    </ModalBody>
    <ModalFooter>
      <Button color="secondary" on:click={closeSales}>Close</Button>
    </ModalFooter>
</Modal>