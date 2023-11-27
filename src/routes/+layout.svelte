<script lang="ts">
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

    import { weatherData, getCurrentConditions } from '../lib/weather';
  
    let isOpen = false;
  
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
    <Collapse {isOpen} navbar expand="md" on:update={handleUpdate}>
      <Nav class="ms-auto" navbar>
        <NavItem>
          <NavLink href="/">Home</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="/menu">Menu</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="/login">Login</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="/about">About</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="https://open-meteo.com/en/docs">{currentTemp}°F | {currentConditions}</NavLink>
        </NavItem>
      </Nav>
    </Collapse>
  </Navbar>

  <slot/>