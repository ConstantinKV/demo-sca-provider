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
      <a id="logo-container" href="/" class="brand-logo center" style="padding-left: 12px !important;">Demo Provider Settings</a>
    </div>
  </nav>

  <div class="container">
      <div class="section">
        <form action="#" method="post">
          <div class="row">
            <div class="col s6">
              <div class="row">
                <div class="input-field">
                  <input value="${sca_url}" id="sca_url" name="sca_url" type="text" class="validate">
                  <label class="active" for="sca_url">SCA Service URL</label>
                </div>
              </div>
              <div class="row">
                <div class="input-field">
                  <input value="${provider_id}" id="provider_id" name="provider_id" type="text" class="validate">
                  <label class="active" for="provider_id">Provider ID</label>
                </div>
              </div>
            </div>
            <div class="input-field col s6">
              <textarea id="sca_rsa_key" class="materialize-textarea" name="sca_rsa_key" type="text">${sca_rsa_key}</textarea>
              <label class="active" for="sca_rsa_key">SCA RSA Public Key</label>
            </div>
          </div>

          <div class="row">
            <input type="submit" class ="btn waves-effect waves-light center" value = "Submit"/>
          </div>
        <form>

          <br>
          <div class="row">
            <label class="active" for="app_url">App URL</label>
            <p id="app_url">${app_url}</p>
            <label class="active" for="sca_key_file">SCA Key file</label>
            <p id="sca_key_file">${sca_key_file}</p>
          </div>
      </div>
      <br><br>
    </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>