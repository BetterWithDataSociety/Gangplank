package gangplank

class Schema {

  String name

  static hasMany = [ props : Property ]
  static mappedBy = [ props : 'schema' ]

  static constraints = {
  }


}
