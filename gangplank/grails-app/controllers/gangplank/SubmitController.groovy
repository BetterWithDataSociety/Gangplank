package gangplank

import au.com.bytecode.opencsv.CSVReader

class SubmitController {

  def index() { }

  def processSubmission() {
    if ( request.method == 'POST' ) {
      def upload_mime_type = request.getFile("submissionFile")?.contentType
      def upload_filename = request.getFile("submissionFile")?.getOriginalFilename()
      def input_stream = request.getFile("submissionFile")?.inputStream
      log.debug("Done");

      CSVReader r = null
      r = new CSVReader( new InputStreamReader(input_stream, java.nio.charset.Charset.forName('UTF-8') ), (char)'\t' )

  
      String[] nl

      nl = r.readNext()

      log.debug("Column headings:");
      nl.each { str ->
        log.debug("  -> ${str}");
      }

      def data_rownum = 0;

      while ((nl = r.readNext()) != null) {
        log.debug(" Row ${data_rownum++}...");
        nl.each { str ->
          log.debug("  -> ${str}");
        }
      }
    }

  }

}
