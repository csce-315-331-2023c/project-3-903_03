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

	let isOpen = false;

	function handleUpdate(event) {
		isOpen = event.detail.isOpen;
	}

	import { weatherData } from './weather';

	const current = weatherData.current;
	const minutely15 = weatherData.minutely15;
	const currentTemp = Math.round(current.temperature2m);

	function getCurrentConditions(weatherCode) {
		// returns weather code as string
		// 0	Clear sky
		// 1, 2, 3	Mainly clear, partly cloudy, and overcast
		// 45, 48	Fog and depositing rime fog
		// 51, 53, 55	Drizzle: Light, moderate, and dense intensity
		// 56, 57	Freezing Drizzle: Light and dense intensity
		// 61, 63, 65	Rain: Slight, moderate and heavy intensity
		// 66, 67	Freezing Rain: Light and heavy intensity
		// 71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
		// 77	Snow grains
		// 80, 81, 82	Rain showers: Slight, moderate, and violent
		// 85, 86	Snow showers slight and heavy
		// 95 *	Thunderstorm: Slight or moderate
		// 96, 99 *	Thunderstorm with slight and heavy hail
		switch (weatherCode) {
			case 0:
				return 'Clear sky';
			case 1:
				return 'Mainly clear';
			case 2:
				return 'Partly cloudy';
			case 3:
				return 'Overcast';
			case 45:
				return 'Fog';
			case 48:
				return 'Depositing rime fog';
			case 51:
				return 'Light drizzle';
			case 53:
				return 'Moderate drizzle';
			case 55:
				return 'Dense drizzle';
			case 56:
				return 'Freezing light drizzle';
			case 57:
				return 'Freezing dense drizzle';
			case 61:
				return 'Light rain';
			case 63:
				return 'Moderate rain';
			case 65:
				return 'Heavy rain';
			case 66:
				return 'Freezing light rain';
			case 67:
				return 'Freezing heavy rain';
			case 71:
				return 'Light snow';
			case 73:
				return 'Moderate snow';
			case 75:
				return 'Heavy snow';
			case 77:
				return 'Snow grains';
			case 80:
				return 'Slight rain showers';
			case 81:
				return 'Moderate rain showers';
			case 82:
				return 'Violent rain showers';
			case 85:
				return 'Slight snow showers';
			case 86:
				return 'Heavy snow showers';
			case 95:
			case 96:
			case 99:
				return 'Thunderstorm';
			default:
				return 'Unknown';
		}
	}

	const currentConditions = getCurrentConditions(current.weatherCode);
</script>

<Navbar color="light" light expand="md">
	<NavbarBrand href="/">Tiff's</NavbarBrand>
	<NavbarToggler on:click={() => (isOpen = !isOpen)} />
	<Collapse {isOpen} navbar expand="md" on:update={handleUpdate}>
		<Nav class="ms-auto" navbar>
			<NavItem>
				<NavLink href="https://open-meteo.com/en/docs">{currentTemp}°F | {currentConditions}</NavLink>
			</NavItem>
			<NavItem>
				<NavLink href="/">Home</NavLink>
			</NavItem>
			<NavItem>
				<NavLink href="/menu">Menu</NavLink>
			</NavItem>
			<NavItem>
				<NavLink href="/about">About</NavLink>
			</NavItem>
			<NavItem>
				<NavLink href="/login">Login</NavLink>
			</NavItem>
		</Nav>
	</Collapse>
</Navbar>
