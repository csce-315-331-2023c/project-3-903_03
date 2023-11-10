<script lang="ts">
	import { weatherData } from './weather';
</script>

{#if weatherData}
	<h1>Weather</h1>
	<p>Current temperature: {weatherData.current.temperature2m} °F</p>
	<p>Current precipitation: {weatherData.current.precipitation} in</p>
{/if}
