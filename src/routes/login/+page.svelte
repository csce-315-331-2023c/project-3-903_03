<head>
<script>
    let username = '';
    let password = '';
    
    async function handleLogin() {
    
    const response = await fetch('/api/posts', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });
    
      if (response.status === 200) {
        print("success");
      } else {
        print("error");
      }
    }
</script>
</head>

<img src="https://consultancy.innotecuk.com/wp-content/uploads/2017/10/cookies-banner.jpg" alt="jpg"style="width:100%" height="175">
<div style="width: 100%; height: 100%; background: #D9D9D9">
    <p style="font-size: 40px; text-align: center; margin-bottom: 0 auto" >Welcome to Tiff's Treats! <br></p>
    <form style="text-align: center" action="/action_page.php">
        <label for="uname">username:</label>
        <input type="text" id="uname" name="username"><br><br>
        <label for="pword">password:</label>
        <input type="password" id="pword" name="password"><br><br>
        <input type="submit" value="Login" style="margin-bottom: 20px; background:white" onclick="handleLogin()"> 
        <input type="submit" value="Create New Account" style="background:#63C5DA">
    </form>
</div>

<nav>
    <a href="/">home</a>
    <a href="/login">login</a>
    <!-- Work on other navigation screens -->
</nav>

