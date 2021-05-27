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
    <div class="nav-wrapper">
      <a id="logo-container" href="/" class="brand-logo">Demo Provider</a>
      <ul class="right hide-on-med-and-down">
        <li><a href="/connections">SCA Connections</a></li>
      </ul>
    </div>
  </nav>

  <div class="container">
    <br><br>
    <h1 class="header center brown-text">Actions</h1>
    <div class="row center">
      <a href="/actions/create" id="create-button" class="btn-large waves-effect waves-light red ${disabled}">Create new action</a>
    </div>

    <div class="row center">
      <#list actions>
        <table class="striped">
          <thead>
            <tr>
                <th>ID</th>
                <th>Code</th>
                <th>Status</th>
                <th>Close</th>
            </tr>
          </thead>

          <tbody>
          <#items as item>
            <tr>
              <td>${item.id}</td>
              <td>${item.code}</td>
              <td>${item.status}</td>
              <td>
              <#if item.closed>
                <i class="small material-icons">remove_circle</i>
              <#else>
                <a href="/actions/${item.id}/close"><i class="small material-icons red-text">clear</i></a>
              </#if>
              </td>
            </tr>
            </#items>
          </tbody>
        </table>
      <#else>
        <p>No actions</p>
      </#list>
    </div>
    <br><br>
  </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>