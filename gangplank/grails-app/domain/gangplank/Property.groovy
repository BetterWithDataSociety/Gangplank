package gangplank

class Property {

  static belongsTo = [
    schema:Schema
  ]

  String label
  String name
  Boolean mandatory

  static constraints = {
  }
}
