package gangplank

class HomeController {

  def index() { 
    def result=[:]
    result.files = Datafile.findAll()
    result
  }
}
