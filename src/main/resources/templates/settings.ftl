<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>Settings</title>

  <!-- CSS  -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <link type="text/css" rel="stylesheet" href="/css/materialize.min.css" media="screen,projection"/>
  <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>
<body>
  <nav class="light-blue lighten-1" role="navigation">
    <div class="nav-wrapper container"><a id="logo-container" href="/" class="brand-logo center">Demo SCA Provider</a>
    </div>
  </nav>

  <div class="container">
      <div class="section">
        <form action="#" method="post">
          <div class="row">
            <div class="input-field col s6">
              <input value="${sca_service_url}" id="sca_service_url" name="sca_service_url" type="text" class="validate">
              <label class="active" for="sca_service_url">SCA Service URL</label>
            </div>
          </div>

          <div class="row">
            <div class="input-field col s6">
              <input value="${provider_id}" id="provider_id" name="provider_id" type="text" class="validate">
              <label class="active" for="provider_id">Provider ID</label>
            </div>
          </div>

          <input type="submit" class ="btn waves-effect waves-light" value = "Submit"/>
        <form>
      </div>
      <br><br>
    </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>