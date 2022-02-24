<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>Demo SCA Service</title>

  <!-- CSS  -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <link type="text/css" rel="stylesheet" href="/css/materialize.min.css" media="screen,projection"/>
  <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>
<body>
  <nav class="light-blue lighten-1" role="navigation">
    <div class="nav-wrapper">
      <a id="logo-container" href="/" class="brand-logo" style="padding-left: 12px !important;">Demo Provider</a>
      <ul class="right hide-on-med-and-down">
        <li><a href="/connections">Connections</a></li>
        <li><a href="/actions">Actions</a></li>
      </ul>
    </div>
  </nav>

  <div class="container">
    <br><br>
    <h2 class="header center brown-text">Consents</h2>

    <div class="row center">
      <#list consents>
        <table class="striped">
          <thead>
            <tr>
                <th>ID</th>
                <th class="center">User ID</th>
                <th class="center">Type</th>
                <th class="center">TPP</th>
                <th class="center">Revoke</th>
            </tr>
          </thead>

          <tbody>
          <#items as item>
            <tr>
              <td>${item.id}</td>
              <td class="center">${item.userId}</td>
              <td class="center">${item.consentType}</td>
              <td class="center">${item.tppName}</td>
              <td class="center">
                <#if item.revoked>
                  <b>revoked</b>
                <#else>
                  <a href="/consents/${item.id}/revoke"><i class="small material-icons red-text">clear</i></a>
                </#if>
              </td>
            </tr>
            </#items>
          </tbody>
        </table>
      <#else>
        <p>No consents</p>
      </#list>
    </div>
    <br><br>
  </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>