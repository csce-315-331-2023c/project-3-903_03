<script lang="ts">
	import {
		DropdownItem,
		DropdownMenu,
		DropdownToggle,
		Dropdown,
		Form,
		FormGroup,
		FormText,
		Input,
		Label,
		Button,
		Card
	} from 'sveltestrap';

	let isOpen = false;
	let radioGroup;

	let textToTranslate = '';
	let translatedText = 'Translated text';
	let selectedLanguage = 'es'; // Default target language
	let apiKey = 'AIzaSyDyR9n5pyrzNODyh5jo0mwCpHNS-dJZKd8';

	const supportedLanguages = [
		{ code: 'fr', name: 'Français' },
		{ code: 'es', name: 'Español' },
		{ code: 'de', name: 'Deutsch' }
		// Add more languages as needed
	];

	async function translateText() {
		try {
			const response = await fetch(
				`https://translation.googleapis.com/language/translate/v2?key=${apiKey}`,
				{
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						q: textToTranslate,
						source: 'en', // Source language (e.g., English)
						target: selectedLanguage
					})
				}
			);

			const data = await response.json();
			translatedText = data.data.translations[0].translatedText;
		} catch (error) {
			console.error('Translation error:', error);
		}
	}
</script>

<meta charset="UTF-8" />
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:400,500&display=swap" />

<main>
	<Form>
		<FormGroup>
			<Label for="translateText">Insert text</Label>
			<Input type="textarea" name="text" id="sourceText" bind:value={textToTranslate} />
		</FormGroup>
	</Form>

	<div class="dropdown">
		<Dropdown>
			<DropdownToggle caret>{selectedLanguage}</DropdownToggle>
			<DropdownMenu>
				{#each supportedLanguages as { code, name }}
					<DropdownItem on:click={() => (selectedLanguage = code)}>{name}</DropdownItem>
				{/each}
			</DropdownMenu>
		</Dropdown>
	</div>

	<div class="button">
		<Button color="primary" on:click={translateText}>Translate</Button>
	</div>
	<div class="card">
		<Card class="mb-3">{translatedText}</Card>
	</div>
</main>

<style>
	div {
		/* center the div */
		margin: auto;
		/* width: 12%; */
		padding: 10px;
	}
</style>
