<script lang="ts">
    import { signIn } from '@auth/sveltekit/client';
    import { signOut } from '@auth/sveltekit/client';
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
  
    let isOpen = false;
  
    function handleUpdate(event) {
      isOpen = event.detail.isOpen;
    }
  </script>
  
  <Navbar color="light" light expand="md">
    <NavbarBrand href="/">Tiff's</NavbarBrand>
    <NavbarToggler on:click={() => (isOpen = !isOpen)} />
    <Collapse {isOpen} navbar expand="md" on:update={handleUpdate}>
      <Nav class="ms-auto" navbar>
        <NavItem>
            <NavLink href="/">Home</NavLink>
          </NavItem>
        <NavItem>
          <NavLink href="./menu">Menu</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="./about">About</NavLink>
        </NavItem>
        <NavItem>
          <NavLink href="./login">Login</NavLink>
          <!--
            <NavLink on:click={() => signIn(
              'auth0', {
                redirect: false,
                callbackUrl: '/'
              },
              {
                scope: 'api openid profile email'
              }
            )}>Login</NavLink>
          -->
        </NavItem>
        <NavItem>
          <NavLink on:click={() => signOut({
            redirect: true,
            callbackUrl: '/'
            })}>Sign Out
          </NavLink>
        </NavItem>
      </Nav>
    </Collapse>
  </Navbar>