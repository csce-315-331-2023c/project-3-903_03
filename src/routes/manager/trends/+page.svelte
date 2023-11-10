<script lang="ts">
    import Nav from "../../Nav.svelte";

    let name = 'Philip Ritchey'
    
    import {
        Button,
        Form,
        FormGroup,
        FormText,
        Input,
        Label,
        Table
    } from 'sveltestrap';

    let restock = [];
    async function get_restock() {
        const response = await fetch('/manager/trends/get_restock');
        restock = await response.json();
    }

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

<div class="container">
    <div class="row">
        <div class="col-sm-3">
            <FormGroup style="width:250px; float:left">
                <Label for="selectReport" style="font-weight:bold; font-size:20px">Select Report</Label>
                <Input type="select" name="select" id="selectReport">
                <option value="usage">Usage</option>
                <option>Sales</option>
                <option>Excess</option>
                <option>Together</option>
                <option>Restock</option>
                </Input>
            </FormGroup>            
        </div>

        <div class="col-sm-3">
            <FormGroup style="width:200px; float:left">
                <Label for="fromDate" style="font-weight:bold; font-size:20px">From Date</Label>
                <Input
                type="date"
                name="date"
                id="fromDate"
                placeholder="date placeholder"
                />
            </FormGroup>
        </div>

        <div class="col-sm-3">
            <FormGroup style="width:200px; float:left">
                <Label for="toDate" style="font-weight:bold; font-size:20px">To Date</Label>
                <Input
                type="date"
                name="date"
                id="toDate"
                placeholder="date placeholder"
                />
            </FormGroup>             
        </div>

        <div class="col-sm-3">
            <Button on:click={get_restock} style="width:150px;
                           padding:7px;
                           font-size:17px;
                           float: left;
                           margin-top: 35px;
                           box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19">Generate</Button>
        </div>
    </div>

    &nbsp

    <div>
        <header>Report:</header>
    </div>

    <div>
        <Table bordered>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Current Qty</th>
                    <th>Minimum Qty</th>
                </tr>
            </thead>
            <tbody>
                {#each restock as i}
                    <tr>
                        <td>{i.name}</td>
                        <td>{i.current_qty}</td>
                        <td>{i.min_qty}</td>
                    </tr>
                {/each}
            </tbody>
        </Table>
    </div>


</div>

