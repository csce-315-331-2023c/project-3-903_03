<meta charset="UTF-8">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:400,500&display=swap">

<script>
    let textToTranslate = '';
    let translatedText = '';
    let selectedLanguage = 'es'; // Default target language
    let apiKey = 'AIzaSyDyR9n5pyrzNODyh5jo0mwCpHNS-dJZKd8';
  
    const supportedLanguages = [
      { code: 'fr', name: 'Français' },
      { code: 'es', name: 'Español' },
      { code: 'de', name: 'Deutsch' },
      // Add more languages as needed
    ];
  
    async function translateText() {
      try {
        const response = await fetch(
          `https://translation.googleapis.com/language/translate/v2?key=${apiKey}`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              q: textToTranslate,
              source: 'en', // Source language (e.g., English)
              target: selectedLanguage,
            }),
          }
        );
  
        const data = await response.json();
        translatedText = data.data.translations[0].translatedText;
      } catch (error) {
        console.error('Translation error:', error);
      }
    }
  </script>
  
  <main>
    <textarea style="margin-top: 10px; width: 400px" bind:value={textToTranslate}></textarea>
    <label for="language">Select Language:</label>
    <select bind:value={selectedLanguage} id="language">
      {#each supportedLanguages as { code, name }}
        <option value={code}>{name}</option>
      {/each}
    </select>
    <button on:click={translateText}>Translate</button>
    <p>{translatedText}</p>
  </main>
  