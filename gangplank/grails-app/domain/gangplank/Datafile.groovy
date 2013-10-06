package gangplank

class Datafile {

  Schema schema
  String guid
  String filename
  RefdataValue status
  RefdataValue category

  static constraints = {
    status(nullable:true, blank:false)
    category(nullable:true, blank:false)
  }
}
