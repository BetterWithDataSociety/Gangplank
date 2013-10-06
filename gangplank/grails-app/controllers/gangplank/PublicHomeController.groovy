package gangplank

class PublicHomeController {

  def index() { 
    def result=[:]
    result.files = Datafile.findAll()
    result
  }

}
