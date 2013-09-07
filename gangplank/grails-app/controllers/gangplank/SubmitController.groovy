package gangplank

import au.com.bytecode.opencsv.CSVReader

class SubmitController {

  // Inject es wrapper service
  def ESWrapperService

  def index() { }

  def processSubmission() {

    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    if ( request.method == 'POST' ) {
      def upload_mime_type = request.getFile("submissionFile")?.contentType
      def upload_filename = request.getFile("submissionFile")?.getOriginalFilename()
      def input_stream = request.getFile("submissionFile")?.inputStream

      def gangplank_schema = 'Finance.Expenses';

      log.debug("Done");

      CSVReader r = null
      r = new CSVReader( new InputStreamReader(input_stream, java.nio.charset.Charset.forName('UTF-8') ), (char)'\t' )

      String[] coldefs
      String[] nl

      coldefs = r.readNext()

      log.debug("Column headings:");
      nl.each { str ->
        log.debug("  -> ${str}");
      }

      def data_rownum = 0;

      while ((nl = r.readNext()) != null) {
        int col=0;
        log.debug(" Row ${data_rownum++}...");
        def record_to_index = [:]

        record_to_index.gangplankDatafileId = 'Some identifier for the datafile in the db'
        record_to_index.gangplankTimestamp = new Date();

        nl.each { str ->
          log.debug("  -> ${str}");
          record_to_index[coldefs[col++]] = str
        }

        log.debug("Record I'm going to throw at ES: ${record_to_index}");
        def future = esclient.index {
          index "gangplank"
          type gangplank_schema
          // id idx_record['_id']
          source record_to_index
        }


      }
    }

  }

  def reset() {
    ESWrapperService.clearDownAndInitES()
    redirect(action:'index');
  }
}
