import gangplank.*

class BootStrap {

  def grailsApplication

  def init = { servletContext ->

   // Global System Roles
    def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER', roleType:'global').save(failOnError: true)
    def editorRole = Role.findByAuthority('ROLE_EDITOR') ?: new Role(authority: 'ROLE_EDITOR', roleType:'global').save(failOnError: true)
    def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN', roleType:'global').save(failOnError: true)

    log.debug("Create admin user...");
    def adminUser = User.findByUsername('admin')
    if ( ! adminUser ) {
      log.error("No admin user found, create");
      adminUser = new User(
                        username: 'admin',
                        password: 'admin',
                        display: 'Admin',
                        email: 'admin@localhost',
                        enabled: true).save(failOnError: true)
    }

    if (!adminUser.authorities.contains(adminRole)) {
      UserRole.create adminUser, adminRole
    }

    if (!adminUser.authorities.contains(userRole)) {
      UserRole.create adminUser, userRole
    }

    String fs = grailsApplication.config.project_dir ?: 'gp_projects'

    log.debug("Make sure project files directory exists, config says it's at ${fs}");
    File f = new File(fs)
    if ( ! f.exists() ) {
      log.debug("Creating upload directory path.")
      f.mkdirs()
    }

    RefdataCategory.lookupOrCreate('DatafileStatus', 'Pending Compliance Review')
    RefdataCategory.lookupOrCreate('DatafileStatus', 'Publication Denied')
    RefdataCategory.lookupOrCreate('DatafileStatus', 'Published')

    RefdataCategory.lookupOrCreate('Category', 'Adult Social Services')
    RefdataCategory.lookupOrCreate('Category', 'Children, Young People and Education')
    RefdataCategory.lookupOrCreate('Category', 'Council Information')
    RefdataCategory.lookupOrCreate('Category', 'Democracy')
    RefdataCategory.lookupOrCreate('Category', 'Employees')
    RefdataCategory.lookupOrCreate('Category', 'Environment')
    RefdataCategory.lookupOrCreate('Category', 'Finance')
    RefdataCategory.lookupOrCreate('Category', 'Housing')
    RefdataCategory.lookupOrCreate('Category', 'Planning and Land')
    RefdataCategory.lookupOrCreate('Category', 'Property')

    def expenses_props = [ [ label:'Name', propname:'name'], 
                           [ label:'Cost Centre', propname:'costcentre'], 
                           [ label:'Amount', propname:'amount'] ]

    // Just some silly test data
    // Wonder if there is a taxonomy of these classes
    def expenses_schema = Schema.findByName('Finance.Expenses');
    if ( expenses_schema == null ) {
      expenses_schema = new Schema(name:'Finance.Expenses').save();
    }

    expenses_props.each { propdef ->
      def dbprop = Property.findBySchemaAndName(expenses_schema, propdef.propname)
      if ( dbprop == null ) {
        dbprop = new Property(
                              schema:expenses_schema, 
                              name:propdef.propname, 
                              label:propdef.label, 
                              mandatory:true).save();
      }
    }

    
  }

  def destroy = {
  }
}
