<script lang="ts">
	import { get_slot_changes } from "svelte/internal";
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

    let selected_report = "";
    let is_div_visible = false;

    function select_report() {
        is_div_visible = false;
    }

    let restock = [];
    async function get_restock() {
        const response = await fetch('/manager/trends/get_restock');
        restock = await response.json();
    }

    let usage = []
    async function get_usage(from_date, to_date) {
        let input = `/manager/trends/get_usage?from_date=${from_date}&to_date=${to_date}`;
        const response = await fetch(input);
        usage = await response.json();
    }

    let sales = []
    async function get_sales(from_date, to_date) {
        let input = `/manager/trends/get_sales?from_date=${from_date}&to_date=${to_date}`;
        const response = await fetch(input);
        sales = await response.json();
    }

    let from_date = ''
    let to_date = ''
    function generate() {
        is_div_visible = false;
        switch(selected_report) {
            case "usage":
                get_usage(from_date, to_date);
                break;
            case "sales":
                get_sales(from_date, to_date);
                break;
            case "restock":
                get_restock();
                break;
        }
        is_div_visible = true;
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
            <FormGroup>
                <Label for="select_report" style="font-weight:bold; font-size:20px">Select Report</Label>
                <Input type="select" name="select_report" id="select_report" bind:value={selected_report}>
                    <option value="usage">Usage</option>
                    <option value="sales">Sales</option>
                    <option value="excess">Excess</option>
                    <option value="together">Together</option>
                    <option value="restock">Restock</option>
                </Input>
            </FormGroup>            
        </div>

        <div class="col-sm-3">
            <FormGroup>
                <Label for="from_date" style="font-weight:bold; font-size:20px">From Date</Label>
                <Input
                bind:value={from_date}
                type="date"
                name="date"
                id="from_date"
                placeholder="date placeholder"
                />
            </FormGroup>
        </div>

        <div class="col-sm-3">
            <FormGroup>
                <Label for="to_date" style="font-weight:bold; font-size:20px">To Date</Label>
                <Input
                bind:value={to_date}
                type="date"
                name="date"
                id="to_date"
                placeholder="date placeholder"
                />
            </FormGroup>             
        </div>

        <div class="col-sm-3">
            <Button on:click={generate} style="width:150px;
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

    {#if is_div_visible}
        {#if selected_report === "usage"}
            <div>
                <Table bordered>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each usage as i}
                            <tr>
                                <td>{i.name}</td>
                                <td>{i.amount}</td>
                            </tr>
                        {/each}
                    </tbody>
                </Table>
            </div>
        {:else if selected_report === "sales"}
            <div>
                <Table bordered>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each sales as i}
                            <tr>
                                <td>{i.name}</td>
                                <td>{i.amount}</td>
                            </tr>
                        {/each}
                    </tbody>
                </Table>
            </div>
        {:else if selected_report === "restock"}
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
        {/if}
    {/if}            

</div>

