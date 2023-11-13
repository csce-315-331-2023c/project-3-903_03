<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Retrieve the value from the form
    $username = $_POST["uname"];

    // Process the data (for simplicity, just echoing the username)
    echo "Hello, $username! Your form has been submitted.";
}
?>
