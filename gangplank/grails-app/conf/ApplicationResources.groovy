modules = {
  application {
    resource url:'js/application.js'
    // resource url:'js/bootstrap-editable.min.js'
  }
  editable {
    // resource url:'css/select2.css'
    // resource url:'js/editable.js.gsp'
    // resource url:'js/moment.min.js'
    // resource url:'js/select2.min.js'
  }
  overrides {
    'bootstrap-css' {
      resource id: 'bootstrap-css', url:'/css/gangplank-bootstrap-2.min.css'
      resource id: 'bootstrap-responsive-css', url:'/css/gangplank-bootstrap-2-responsive.min.css'
    }
  }
}

