<script>
	import { Button } from 'sveltestrap';
</script>

<style>
	/* Add your CSS styles here */
	.container {
		text-align: center;
		padding: 20px;
	}
	.extra-text {
		text-align: center;
		padding: 20px;
	}
	.contact {
		text-align: center;
		padding: 20px;
	}
	.socials {
		text-align: center;
		padding: 20px;
	}

	h1 {
		font-size: 36px;
		color: navy;
	}

	p {
		font-size: 18px;
		margin-bottom: 20px;
	}
</style>

<title>Home</title>

<div class="container">
	<h1>Welcome to Tiff's Treats</h1>
	<p>Home of delicious warm cookies delivered to your doorstep!</p>
	<Button href="/guest" 
			style="margin: 10px; width:250px; padding:13px; font-size:19px; box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19" 
			color="primary">
			Place Order
	</Button>
</div>

<div class="extra-text">
	<p>Founded in 1999, Tiff's Treats is the nation's first warm cookie delivery company.</p>
	<p>Our cookies are baked fresh and delivered to your doorstep, warm and gooey.</p>
	<p>Explore our mouthwatering selection of chocolate chip, snickerdoodle, and more!</p>
</div>

<div class="contact">
	<p>Contact Us: Phone - 123-456-7890 | Email - info@tiffstreats.com</p>
</div>

<div class="socials">
	<a href="https://www.facebook.com/tiffstreats">Follow us on Facebook</a>
	<a href="https://twitter.com/tiffstreats">Follow us on Twitter</a>
</div>


