package gangplank

class SubmitController {

    def index() { }

  def processSubmission() {
    if ( request.method == 'POST' ) {
      def upload_mime_type = request.getFile("submissionFile")?.contentType
      def upload_filename = request.getFile("submissionFile")?.getOriginalFilename()
      def input_stream = request.getFile("submissionFile")?.inputStream
      log.debug("Done");
    }
  }

}
