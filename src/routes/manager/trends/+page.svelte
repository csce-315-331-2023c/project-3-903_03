<script lang="ts">
	import { get_slot_changes, validate_component } from "svelte/internal";
    import Nav from "$lib/Nav.svelte";

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

    let valid_message = '';

    function is_valid_date(date) {
        const parsedDate = Date.parse(date);
        return !isNaN(parsedDate);
    }

    function valid_dates(from_date, to_date) {
        if (!is_valid_date(from_date)) {
            valid_message = 'From Date is Invalid';
            return false;
        }

        if (!is_valid_date(to_date)) {
            valid_message = 'To Date is Invalid';
            return false;
        }
        
        if (from_date > to_date) {
            valid_message = 'From Date is Greater than To Date';
            return false;            
        }
        return true;
    }

    let selected_report = "";
    let is_div_visible = false;

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

    let excess = []
    async function get_excess(from_date, to_date) {
        let input = `/manager/trends/get_excess?from_date=${from_date}&to_date=${to_date}`;
        const response = await fetch(input);
        excess = await response.json();
    }

    let together = []
    async function get_together(from_date, to_date) {
        let input = `/manager/trends/get_together?from_date=${from_date}&to_date=${to_date}`;
        const response = await fetch(input);
        together = await response.json();
    }

    let from_date = ''
    let to_date = ''
    function generate() {
        is_div_visible = false;
        valid_message = '';
        switch(selected_report) {
            case "usage":
                if (valid_dates(from_date, to_date)) {
                    get_usage(from_date, to_date);
                    is_div_visible = true;
                }
                    
                break;
            case "sales":
                if (valid_dates(from_date, to_date)) {
                    get_sales(from_date, to_date);
                    is_div_visible = true;
                }
                break;
            case "excess":
                if (valid_dates(from_date, to_date)) {
                    get_excess(from_date, to_date);
                    is_div_visible = true;
                }
                break;
            case "together":
                if (valid_dates(from_date, to_date)) {
                    get_together(from_date, to_date); 
                    is_div_visible = true;
                }   
                break;
            case "restock":
                get_restock();
                is_div_visible = true;
                break;
        }
        
    }

    function handle_input_change() {
        is_div_visible = false;     
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
                <Input type="select" name="select_report" id="select_report" bind:value={selected_report} on:input={handle_input_change}>
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
                on:input={handle_input_change}
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
                on:input={handle_input_change}
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
        <header>Report: {valid_message}</header>
    </div>

    {#if is_div_visible}
        {#if selected_report === "usage"}
            <div>
                <p>Amount of Inventory Used From {from_date} To {to_date}</p>
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
                <p>Number of Items Sold From {from_date} To {to_date}</p>
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
        {:else if selected_report === "excess"}
            <div>
                <p>List of Menu Items That Only Sold Less Than 10% Of Their Inventory From {from_date} To {to_date}</p>
                <Table bordered>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Amount</th>
                            <th>Ten Percent</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each excess as i}
                            <tr>
                                <td>{i.name}</td>
                                <td>{i.amount}</td>
                                <td>{i.ten_percent}</td>
                            </tr>
                        {/each}
                    </tbody>
                </Table>
            </div>
        {:else if selected_report === "together"}
            <div>
                <p>Pairs of Menu Items That Sell Together Often From {from_date} To {to_date}</p>
                <Table bordered>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Name</th>
                            <th>Frequency</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each together as i}
                            <tr>
                                <td>{i.name1}</td>
                                <td>{i.name2}</td>
                                <td>{i.frequency}</td>
                            </tr>
                        {/each}
                    </tbody>
                </Table>
            </div>
        {:else if selected_report === "restock"}
            <div>
                <p>Inventory That Is Less Than Minimum Amount To Have Before Restock</p>
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

