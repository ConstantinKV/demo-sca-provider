<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>Add Salt Edge Authenticator</title>

  <!-- CSS  -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <link type="text/css" rel="stylesheet" href="/css/materialize.min.css" media="screen,projection"/>
  <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>
<body>
  <nav class="light-blue lighten-1" role="navigation">
    <div class="nav-wrapper container"><a id="logo-container" href="/" class="brand-logo">Demo SCA Provider</a>
      <ul class="right hide-on-med-and-down">
        <li><a href="/actions">Actions</a></li>
      </ul>
    </div>
  </nav>

  <div class="container">
    <br><br>
    <h1 class="header center brown-text">Connections</h1>
    <div class="row center">
      <h5 class="header col s12 light">Scan qr code with Salt Edge Authenticator to initiate enrollment flow</h5>
    </div>

    <div class="row center">
      <#list connections>
        <table class="striped">
          <thead>
            <tr>
                <th>ID</th>
                <th>Connection Id</th>
                <th>AccessToken</th>
                <th>Revoked</th>
            </tr>
          </thead>

          <tbody>
          <#items as item>
            <tr>
              <td>${item.id}</td>
              <td>${item.connectionId}</td>
              <td>${item.accessToken}</td>
              <td>
              <#if item.revoked>
                <i class="small material-icons">remove_circle</i>
              <#else>
                <p>
              </#if>
              </td>
            </tr>
            </#items>
          </tbody>
        </table>
      <#else>
        <p>No connections</p>
      </#list>
    </div>
    <br><br>
  </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>