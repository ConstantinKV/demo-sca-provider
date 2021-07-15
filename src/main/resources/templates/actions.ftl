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
        <li><a href="/consents">Consents</a></li>
      </ul>
    </div>
  </nav>

  <div class="container">
    <br><br>
    <h2 class="header center brown-text">Actions to authorize</h2>
    <div class="row center">
      <a href="/actions/create/text" id="create-button" class="btn-large waves-effect waves-light blue ${disabled}">Create new TEXT action</a>
      <a href="/actions/create/html" id="create-button" class="btn-large waves-effect waves-light yellow blue-text ${disabled}">Create new HTML action</a>
      <a href="/actions/create/json" id="create-button" class="btn-large waves-effect waves-light red ${disabled}">Create new JSON action</a>
    </div>

    <div class="row center">
      <#list actions>
        <table class="striped">
          <thead>
            <tr>
                <th>ID</th>
                <th>Authorization Code</th>
                <th>Content type</th>
                <th>Status</th>
                <th>Created At</th>
                <th>Close</th>
            </tr>
          </thead>

          <tbody>
          <#items as item>
            <tr>
              <td>${item.id}</td>
              <td>${item.code}</td>
              <td class="center">${item.descriptionType!"text"}</td>
              <td>${item.status}</td>
              <td>${item.createdAtDescription}</td>
              <td>
              <#if item.closed>
                <b>closed</b>
              <#elseif item.expired>
                <b>expired</b>
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