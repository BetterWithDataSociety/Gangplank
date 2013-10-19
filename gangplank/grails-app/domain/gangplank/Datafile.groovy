package gangplank

class Datafile {

  Schema schema
  String guid
  String filename
  RefdataValue status
  RefdataValue category

  static constraints = {
    schema(nullable:true, blank:false)
    status(nullable:true, blank:false)
    category(nullable:true, blank:false)
  }
}
