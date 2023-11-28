import { sveltekit } from '@sveltejs/kit/vite';

/** @type {import('vite').UserConfig} */
const config = {
	plugins: [sveltekit()],
	test: {
		include: ['src/**/*.{test,spec}.{js,ts}']
	},
	esbuild: {
		supported: {
			'top-level-await': true //browsers can handle top-level-await features
		}
	}
};

export default config;
