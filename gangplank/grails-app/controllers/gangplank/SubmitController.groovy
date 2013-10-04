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

      log.debug("Done");
      CSVReader r = null
      if ( params.type=="csv" ) {
        r = new CSVReader( new InputStreamReader(input_stream, java.nio.charset.Charset.forName('UTF-8') ), (char)',' )
      }
      else {
        r = new CSVReader( new InputStreamReader(input_stream, java.nio.charset.Charset.forName('UTF-8') ), (char)'\t' )
      }

      String[] coldefs
      String[] nl
      coldefs = r.readNext()



      def schema = null
      if ( params.schemaid == '' ) {
        log.debug("Generate a new schema based on content");
        schema = new Schema(name:"Auto generated from ${upload_filename}").save();
        coldefs.each { col ->
          def dbprop = new Property(
                              schema:schema,
                              name:col.toLowerCase(),
                              label:col,
                              mandatory:false).save();

        }
      }
      else {
        log.debug("Lookup schema with id ${params.schemaid}");
        schema = Schema.get(params.schemaid);
      }

      def new_datafile = new Datafile(schema:schema,
                                      guid:java.util.UUID.randomUUID().toString(),
                                      filename:upload_filename,
                                      status:RefdataCategory.lookupOrCreate('DatafileStatus', 'Pending Compliance Review'),
                                      category:RefdataValue.get(params.catid)).save()

      def gangplank_schema = schema.name


      def data_rownum = 0;

      while ((nl = r.readNext()) != null) {
        int col=0;
        log.debug(" Row ${data_rownum++}...");
        def record_to_index = [:]

        record_to_index.gangplankTimestamp = new Date();
        record_to_index.sourceFile = new_datafile.guid

        boolean row_has_at_least_one_value = false

        nl.each { str ->
          if ( col <= coldefs.length ) {
            record_to_index[coldefs[col++].toLowerCase()] = str
          }
          else {
            record_to_index["indefined_col_${col++}"]=str
          }

          if ( ( str != null ) && ( str.length() > 0 ) )
            row_has_at_least_one_value = true;
        }

        // This if emulates the schema validation rules.. here I'm pretending that the rule is
        // name must not be null.
        if ( row_has_at_least_one_value ) {
          log.debug("Record I'm going to throw at ES: ${record_to_index}");
          def future = esclient.index {
            index "gangplank"
            type "${schema.id}"
            // id idx_record['_id']
            source record_to_index
          }
          future.get()
        }
      }

      redirect(controller:'browse', 
               action:'index', 
               params:[file:new_datafile.guid, schema:gangplank_schema, q:"sourceFile:\"${new_datafile.guid}\""]);
    }

  }

  def reset() {
    ESWrapperService.clearDownAndInitES()
    redirect(action:'index');
  }
}
