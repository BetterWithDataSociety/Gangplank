import org.codehaus.groovy.grails.commons.ApplicationHolder

modules = {
  gangplank {
    dependsOn 'bootstrap'
    dependsOn 'bootstrap-popover'
    // resource url: 'css/scaffolding.css'
    // resource url: 'css/bootstrap.css'
    resource url: 'css/style.css'
    // resource url: 'css/bootstrap-editable.css'
    // resource url: "css/${ApplicationHolder.application.config.defaultCssSkin?:'live.css'}"
  }
}
