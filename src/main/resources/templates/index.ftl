<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>Demo SCA Provider</title>

  <!-- CSS  -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <link type="text/css" rel="stylesheet" href="/css/materialize.min.css" media="screen,projection"/>
  <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>
<body>
  <nav class="light-blue lighten-1" role="navigation">
    <div class="nav-wrapper">
      <a id="logo-container" href="/" class="brand-logo center">Demo Provider</a>
    </div>
  </nav>

  <div class="container">
      <div class="section">

        <!--   Icon Section   -->
        <div class="row">
          <div class="col s12 m4">
            <a class="match-parent" href="/settings">
              <div class="icon-block">
                <h2 class="center light-blue-text"><i class="material-icons">settings</i></h2>
                <h5 class="center black-text">SCA Service settings</h5>
              </div>
            </a>
          </div>

          <div class="col s12 m4">
            <a class="match-parent" href="/connections">
              <div class="icon-block">
                <h2 class="center light-blue-text"><i class="material-icons">link</i></h2>
                <h5 class="center black-text">SCA Connections</h5>
              </div>
            </a>
          </div>
          <div class="col s12 m4">
            <div class="icon-block center">
              <img src="/connections/qr" width="128" height="128" alt="Add Authenticator">
              <h5 class="center black-text">Add Salt Edge Authenticator</h5>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col s12 m4">
            <a class="match-parent" href="/actions">
              <div class="icon-block">
                <h2 class="center light-blue-text"><i class="material-icons">payment</i></h2>
                <h5 class="center black-text">Create an Action</h5>
              </div>
            </a>
          </div>
          <div class="col s12 m4">
            <a class="match-parent" href="/instant_action">
              <div class="icon-block">
                <h2 class="center light-blue-text"><i class="material-icons">flash_on</i></h2>
                <h5 class="center black-text">Instant Action test</h5>
              </div>
            </a>
          </div>
          <div class="col s12 m4">
            <a class="match-parent" href="/consents">
              <div class="icon-block">
                <h2 class="center light-blue-text"><i class="material-icons">assignment_turned_in</i></h2>
                <h5 class="center black-text">Consents</h5>
              </div>
            </a>
          </div>
        </div>
      </div>
      <br><br>
    </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>