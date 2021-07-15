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
      </div>
    </nav>

  <div class="container">
      <h2 class="header center brown-text">Add Consent</h2>

      <div class="section">
        <form action="#" method="post">
          <div class="row">
            <div class="col s6">

              <div class="row">
                <div class="input-field">
                  <input id="tpp_name" name="tpp_name" type="text" class="validate">
                  <label class="active" for="sca_service_url">TPP Name</label>
                </div>
              </div>

              <div class="row">
                <h5>Consent Type</h5>
                <p>
                  <label for="aisp">
                    <input id="aisp" value="aisp" name="type_group" type="radio" checked />
                    <span>AISP</span>
                  </label>
                </p>
                <p>
                  <label for="pisp_future">
                    <input id="pisp_future" value="pisp_future" name="type_group" type="radio" />
                    <span>PISP FUTURE</span>
                  </label>
                </p>
                <p>
                  <label for="pisp_recurring">
                    <input id="pisp_recurring" value="pisp_recurring" name="type_group" type="radio"  />
                    <span>PISP RECURRING</span>
                  </label>
                </p>
              </div>

              <div class="row">
                <h5>Shared data</h5>
                <label>
                  <input name="balance" type="checkbox" />
                  <span>Balance</span>
                </label>
                <span style="display:inline-block; width: 24px;"></span>
                <label>
                  <input name="transactions" type="checkbox" />
                  <span>Transactions</span>
                </label>
              </div>

            </div>
          </div>

          <div class="row">
            <#if active>
              <input type="submit" class ="btn waves-effect waves-light center" value = "Submit"/>
            <#else>
              <p>Invalid connection ID.</p>
            </#if>
          </div>
        <form>
      </div>
      <br><br>
    </div>

    <!--  Scripts-->
    <script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>