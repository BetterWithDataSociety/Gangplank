package gangplank

import au.com.bytecode.opencsv.CSVReader
import java.security.MessageDigest
import grails.plugins.springsecurity.Secured

class SubmitController {

  // Inject es wrapper service
  def ESWrapperService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def processSubmission() {
    if ( request.method == 'POST' ) {
      try {
        def upload_mime_type = request.getFile("submissionFile")?.contentType
        def upload_filename = request.getFile("submissionFile")?.getOriginalFilename()
        // def input_stream = request.getFile("submissionFile")?.inputStream

        // store input stream locally
        def temp_file = copyUploadedFile(request.getFile("submissionFile"));
  
        // validate syntax
        def validation_result = validateSyntax(temp_file)
        
        // analyze
        if ( validation_result.status == true ) {

          def analysis = analyze(temp_file, validation_result);
          def process_result = process(temp_file, upload_filename, upload_mime_type)

          redirect(controller:'browse', action:'datafile', id:process_result.datafile.guid);
          // render(view:'validDatafile', model:[:]);
        }
        else {
          render(view:'invalidDatafile', model:[:]);
        }

      }
      finally {
      }
    }
  }

  def createDatafile(schema,upload_filename) {
    def new_datafile = new Datafile(schema:schema,
                                    guid:java.util.UUID.randomUUID().toString(),
                                    filename:upload_filename,
                                    status:RefdataCategory.lookupOrCreate('DatafileStatus', 'Pending Compliance Review'),
                                    category:RefdataValue.get(params.catid))
    if ( new_datafile.save(flush:true) ) {
      log.debug("Datafile saved ok");
    }
    else {
      log.error("Problem creating datafile: ${new_datafile.errors}");
    }
    new_datafile
  }

  def copyUploadedFile(inputfile) {
    log.debug("copyUploadedFile...");
    def deposit_token = java.util.UUID.randomUUID().toString();
    validateUploadDir('./filestore');
    def temp_file_name = "./filestore/${deposit_token}";
    def temp_file = new File(temp_file_name);
  
    // Copy the upload file to a temporary space
    inputfile.transferTo(temp_file);

    temp_file
  }

  def validateSyntax(inputfile) {
    log.debug("validateSyntax...");
    def result = [:];
    result.status = false;
    try {
      def input_stream = new FileInputStream(inputfile);
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
      while ((nl = r.readNext()) != null) {
      }

      result.status = true
    }
    catch ( Exception e ) {
      log.error("Problem",e);
    }
    result
  }

  def analyze(temp_file, validation_result) {
    log.debug("analyze...");

    // Create a checksum for the file..
    MessageDigest md5_digest = MessageDigest.getInstance("MD5");
    InputStream md5_is = new FileInputStream(temp_file);
    byte[] md5_buffer = new byte[8192];
    int md5_read = 0;
    while( (md5_read = md5_is.read(md5_buffer)) >= 0) {
      md5_digest.update(md5_buffer, 0, md5_read);
    }
    md5_is.close();
    byte[] md5sum = md5_digest.digest();
    String md5sumHex = new BigInteger(1, md5sum).toString(16);

    log.debug("MD5 is ${md5sumHex}");
  }

  def process(inputfile, upload_filename, upload_mime_type) {

    def result = [:]

    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()


    def input_stream = new FileInputStream(inputfile);
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

    def gangplank_schema = schema.name
    result.datafile = createDatafile(schema,upload_filename)

    log.debug("result.datafile = ${result.datafile}");

    def data_rownum = 0;

    while ((nl = r.readNext()) != null) {
      int col=0;
      log.debug(" Row ${data_rownum++}...");
      def record_to_index = [:]

      record_to_index.gangplankTimestamp = new Date();
      record_to_index.sourceFile = result.datafile.guid

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

    result
  }

  def oldProcessSubmission() {

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
        // The schema defines the columns, but the order might be different (Or there may be unknown columns)
        // Sort out the input schema here
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
          else
            log.debug("Omitting row with no values");
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

  private def validateUploadDir(path) {
    File f = new File(path);
    if ( ! f.exists() ) {
      log.debug("Creating upload directory path")
      f.mkdirs();
    }
  }
}
