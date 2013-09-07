import gangplank.*

class BootStrap {

  def init = { servletContext ->

    def expenses_props = [ [ propname:'Name'], [propname:'Cost Centre'], [propname:'Amount'] ]

    // Just some silly test data
    // Wonder if there is a taxonomy of these classes
    def expenses_schema = Schema.findByName('Finance.Expenses');
    if ( expenses_schema == null ) {
      expenses_schema = new Schema(name:'Finance.Expenses').save();
    }

    expenses_props.each { propdef ->
      def dbprop = Property.findBySchemaAndName(expenses_schema, propdef.propname)
      if ( dbprop == null ) {
        dbprop = new Property(schema:expenses_schema, name:propdef.propname).save();
      }
    }

    
  }

  def destroy = {
  }
}
