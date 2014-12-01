<!DOCTYPE html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>EWatchman Login Form</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
  <form method="post" action="ewatchman" class="login">
    <p>
      <label for="login">Email:</label>
     <!-- <input type="text" name="login" id="login" value="name@example.com"> -->
	  <input type="text" value="admin@iiit.com" name="Email" id="Email"
      onblur="if (this.value == '') {this.value = 'admin@iiit.com';}"
      onfocus="if (this.value == 'admin@iiit.com') {this.value = '';}" />
    </p>

    <p>
      <label for="password">Password:</label>
      <!--<input type="password" name="password" id="password" value="4815162342"> -->
	  <input type="password" name="password" id="password" value="adminadmin"
      onblur="if (this.value == '') {this.value = 'adminadmin';}"
      onfocus="if (this.value == 'adminadmin') {this.value = '';}" />
    </p>

    <p class="login-submit">
      <button type="submit" class="login-button">Login</button>
    </p>

  </form>
</body>
</html>
