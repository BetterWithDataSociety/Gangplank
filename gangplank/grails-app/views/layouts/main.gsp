<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="GoKB"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

    <g:layoutHead/>
    <r:layoutResources />

  </head>

  <body>

    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <g:link controller="home" action="index" class="brand">GangPlank</g:link>
          <div class="nav-collapse">
            <ul class="nav">
              <sec:ifLoggedIn>
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                  <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Admin</a>
                    <ul class="dropdown-menu">
                      <li><g:link controller="submit" action="reset">Reset</g:link></li>
                      <li><g:link controller="schema" action="index">Schemas</g:link></li>
                      <li><g:link controller="submit" action="index">Upload</g:link></li>
                    </ul>
                  </li>
                </sec:ifAnyGranted>
              </sec:ifLoggedIn>
            </ul>
            <ul class="nav pull-right">
              <sec:ifLoggedIn>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">${request.user?.username} <b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <li><g:link controller="logout">Logout</g:link></li>
                  </ul>
                  <li><g:link controller="settings" action="index"><i class="icon-wrench icon-white"></i></g:link></li>
                </li>
              </sec:ifLoggedIn>
              <sec:ifNotLoggedIn>
                <li><g:link controller="register" action="index">Register</g:link></li>
                <li><g:link controller="login" action="index">LogIn</g:link></li>
              </sec:ifNotLoggedIn>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div class="navbar-push"></div>

    <g:layoutBody/>

    <div id="Footer">
      <div class="navbar navbar-footer">
          <div class="navbar-inner">
              <div class="container">
                  <div>
                      <ul class="footer-sublinks nav">
                        <li><g:link controller="home" action="about">GangPlank <g:meta name="app.version"/> / build <g:meta name="app.buildNumber"/></g:link></li>
                      </ul>
                  </div>

                  <div class="pull-right">
                      <div class="nav-collapse">
                          <ul class="nav">
                              <li class="dropdown">
                                  <a href="#"
                                     class="dropdown-toggle"
                                     data-toggle="dropdown">
                                      Tools
                                      <b class="caret"></b>
                                  </a>
                                  <ul class="dropdown-menu">
                                      <li>test</li>
                                  </ul>
                              </li>
                          </ul>
                      </div>
                  </div>
              </div>
          </div>
      </div>

    <g:javascript library="application"/>
    <r:layoutResources />
  </body>
</html>
