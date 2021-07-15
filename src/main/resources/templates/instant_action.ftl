<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>Instant Action test</title>

  <!-- CSS  -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <link type="text/css" rel="stylesheet" href="/css/materialize.min.css" media="screen,projection"/>
  <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
  <script src="https://code.jquery.com/jquery-3.4.1.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>
  <style>
    .row .col{
      float: none !important;
      margin-left: auto;
      margin-right: auto;
    }
  </style>

  <script language="javascript">
      function refreshStatus() {
          <#if action_id??>
              var action_id = "${action_id}"
          <#else>
              clearTimeout(poll);
              return;
          </#if>

          var token = $('input[name="csrfToken"]').attr('value');

          $.ajax({
              url: "/instant_action/status?action_id=" + action_id,
              method: "get",
              dataType: "json",
              headers: {
                  'X-CSRF-Token': token
              },
              success: function(data) {
                  var status = data["status"]
                  document.getElementById("action_status").textContent = status;
                  polling();
              },
              fail: function(data) {
                  console.log(data);  debugger;
              }
          })
      }

      function polling() {
          clearTimeout(poll)
          var poll = setTimeout(refreshStatus, 3000)
      }

      <#if action_id??>
          polling()
      </#if>
  </script>
</head>
<body>
  <nav class="light-blue lighten-1" role="navigation">
    <div class="nav-wrapper">
      <a id="logo-container" href="/" class="brand-logo center">Demo Provider</a>
    </div>
  </nav>

  <div class="container center">
      <h3 class="header center brown-text">Instant Action test</h3>

      <div class="section center">
          <div class="row center-align">
            <div class="col s6 center center-align">
                <img class="center" src="${qr_link}" width="256" height="256" alt="Instant Action">
                <a class="center" href="${app_link}"><h5 class="center">App link for authenticator</h5></a>
                <h5>Action [${action_id}] status: <span id="action_status"></span></h5>
          </div>
      </div>
    </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>