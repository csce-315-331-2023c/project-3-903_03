<script>
	import { Button } from 'sveltestrap';
</script>

<style>
	/* Add your CSS styles here */
	:global(body) {
		background-color: #add8e6; /* Set a background color for the body */
		color: #001f3f; /* Set the text color to a dark shade of blue */
	}

  	h1, h2, h3, h4, p {
    	font-weight: bold; /* Increase font weight for better visibility */
  	}

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
		color: #0074cc; /* Set the heading color to a shade of blue */
	}

	p {
		font-size: 18px;
		margin-bottom: 20px;
	}

	Button {
		background-color: #0074cc; /* Set the button background color to a shade of blue */
		color: #fff; /* Set the button text color to white for better contrast */
		border: 1px solid #0074cc; /* Add a border to the button for better visibility */
	}

	Button:hover {
		background-color: #0056b3; /* Darker shade on hover */
		border-color: #0056b3;
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
