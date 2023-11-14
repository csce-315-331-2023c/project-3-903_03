<script lang="ts">
	import { weatherData } from './weather';

	const current = weatherData.current;
	const minutely15 = weatherData.minutely15;
</script>

{#if weatherData}
	<h1>Weather</h1>
	<p>Current temperature: {current.temperature2m}°F</p>
	<p>Current precipitation: {current.precipitation} in</p>

	{#each Array(weatherData.minutely15.time) as minute}
		<p>Time: {minute} | Precipitation: {minutely15.rain} in</p>
	{/each}
{/if}
