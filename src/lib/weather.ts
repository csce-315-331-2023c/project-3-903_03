import { fetchWeatherApi } from 'openmeteo';
	
const params = {
	"latitude": 30.6215,
	"longitude": -96.3403,
	"current": ["temperature_2m", "precipitation", "rain", "weather_code"],
	"minutely_15": "rain",
	"hourly": "temperature_2m",
	"temperature_unit": "fahrenheit",
	"wind_speed_unit": "mph",
	"precipitation_unit": "inch",
	"timezone": "America/Chicago"
};
const url = "https://api.open-meteo.com/v1/forecast";
const responses = await fetchWeatherApi(url, params);

// Helper function to form time ranges
const range = (start: number, stop: number, step: number) =>
	Array.from({ length: (stop - start) / step }, (_, i) => start + i * step);

// Process first location. Add a for-loop for multiple locations or weather models
const response = responses[0];

// Attributes for timezone and location
const utcOffsetSeconds = response.utcOffsetSeconds();
const timezone = response.timezone();
const timezoneAbbreviation = response.timezoneAbbreviation();
const latitude = response.latitude();
const longitude = response.longitude();

const current = response.current()!;
const minutely15 = response.minutely15()!;
const hourly = response.hourly()!;

// Note: The order of weather variables in the URL query and the indices below need to match!
export const weatherData = {
	current: {
		time: new Date((Number(current.time()) + utcOffsetSeconds) * 1000),
		temperature2m: current.variables(0)!.value(),
		precipitation: current.variables(1)!.value(),
		rain: current.variables(2)!.value(),
		weatherCode: current.variables(3)!.value(),
	},
	minutely15: {
		time: range(Number(minutely15.time()), Number(minutely15.timeEnd()), minutely15.interval()).map(
			(t) => new Date((t + utcOffsetSeconds) * 1000)
		),
		rain: minutely15.variables(0)!.valuesArray()!,
	},
	hourly: {
		time: range(Number(hourly.time()), Number(hourly.timeEnd()), hourly.interval()).map(
			(t) => new Date((t + utcOffsetSeconds) * 1000)
		),
		temperature2m: hourly.variables(0)!.valuesArray()!,
	},

};

export function getCurrentConditions(weatherCode) {
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

// `weatherData` now contains a simple structure with arrays for datetime and weather data
// for (let i = 0; i < weatherData.minutely15.time.length; i++) {
// 	console.log(
// 		weatherData.minutely15.time[i].toISOString(),
// 		weatherData.minutely15.rain[i]
// 	);
// }
// for (let i = 0; i < weatherData.hourly.time.length; i++) {
// 	console.log(
// 		weatherData.hourly.time[i].toISOString(),
// 		weatherData.hourly.temperature2m[i]
// 	);
// }