<script>
    import { auth, checkAuthentication } from '$lib/auth.js';
    import {
      Collapse,
      Navbar,
      NavbarToggler,
      NavbarBrand,
      Nav,
      NavItem,
      NavLink,
      Dropdown,
      DropdownToggle,
      DropdownMenu,
      DropdownItem
    } from 'sveltestrap';

    import { weatherData, getCurrentConditions } from '$lib/weather.ts';
  
    let isOpen = false;
    $: name = ($auth.name == null ? '' : $auth.name);
    $: category = ($auth.category == null ? '' : $auth.category);
    $: cat = ($auth.category == null ? '' : category[0].toUpperCase() + category.slice(1));
    $: is_manager_visible = (category == 'manager' ? true : false);
    $: is_customer_visible = (category == 'customer' ? true : false);
    $: is_cashier_visible = (category == 'cashier' ? true : false);
    $: is_admin_visible = (category == 'admin' ? true : false);
    $: entry = (category === '' ? 'Login' : 'Logout');


    function handleUpdate(event) {
      isOpen = event.detail.isOpen;
    }

    const current = weatherData.current;
    const minutely15 = weatherData.minutely15;
    const currentTemp = Math.round(current.temperature2m);

    const currentConditions = getCurrentConditions(current.weatherCode);

  </script>
  
   <Navbar color="light" light expand="md">
    <NavbarBrand href="/">Tiff's Treats</NavbarBrand>
    <NavbarToggler on:click={() => (isOpen = !isOpen)} />
    {#if cat !== ''}
      <NavItem>
        {cat}  {name}
      </NavItem>
    {/if}      
    <Collapse {isOpen} navbar expand="md" on:update={handleUpdate}>
      <Nav class="ms-auto" navbar>
        <NavItem>
          <NavLink href="/">Home</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="/menu">Menu</NavLink>
        </NavItem>
        {#if is_manager_visible}
          <Dropdown  nav inNavbar>
            <DropdownToggle nav caret>Manager</DropdownToggle>
            <DropdownMenu end>
              <DropdownItem href="/manager">Manager</DropdownItem>
              <DropdownItem divider />
              <DropdownItem href="/manager/menu_item">Menu Items</DropdownItem>
              <DropdownItem href="/manager/ingredients">Ingredients</DropdownItem>
              <DropdownItem href="/manager/trends">Trends</DropdownItem>
              <DropdownItem href="/manager/order_history">Order History</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        {/if}
        {#if is_customer_visible}
          <Dropdown nav inNavbar>
            <DropdownToggle nav caret>Customer</DropdownToggle>
            <DropdownMenu end>
              <DropdownItem href="/customer">Customer</DropdownItem>
              <DropdownItem divider />
              <DropdownItem href="/customer/place_order">Place Order</DropdownItem>
              <DropdownItem href="/customer/order_history">Order History</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        {/if}
        {#if is_cashier_visible}
          <Dropdown nav inNavbar>
            <DropdownToggle nav caret>Cashier</DropdownToggle>
            <DropdownMenu end>
              <DropdownItem href="/cashier">Cashier</DropdownItem>
              <DropdownItem divider />
              <DropdownItem href="/cashier/place_order">Place Order</DropdownItem>
              <DropdownItem href="/cashier/order_history">Order History</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        {/if}
        {#if is_admin_visible}
          <NavItem>
            <NavLink href="/admin">Admin</NavLink>
          </NavItem>        
        {/if}
        <NavItem>
          <NavLink href="/login">{entry}</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="/about">About</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="https://open-meteo.com/en/docs" target="_blank">{currentTemp}°F | {currentConditions}</NavLink>
        </NavItem>
      </Nav>
    </Collapse>
  </Navbar>
  
  <slot/>