package gangplank

class SchemaController {

  def index() { 
    def result = [:]
    result.schemas = Schema.findAll()
    result
  }

  def show() {
    def result = [:]
    result.schema = Schema.get(params.id)
    result
  }
}
