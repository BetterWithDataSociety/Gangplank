import gangplank.User

class UserDetailsFilters {

  def springSecurityService

  def filters = {
    setUsetFilter(controller:'*', action:'*') {
      before = {
        // if ( session.sessionPreferences == null ) {
        //   session.sessionPreferences = grailsApplication.config.appDefaultPrefs
        // }
        // else {
        // }
        if ( springSecurityService.principal instanceof String ) {
        }
        else if (springSecurityService.principal?.id != null ) {
          request.user = User.get(springSecurityService.principal.id);
          // request.userOptions = request.user.getUserOptions();
        }
      }
    }
  }


}
