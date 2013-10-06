package gangplank

import grails.converters.*
import grails.plugins.springsecurity.Secured
import grails.converters.*

class SettingsController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
  }
}
