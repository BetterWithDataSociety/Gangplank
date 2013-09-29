package gangplank

class BrowseController {

  def ESWrapperService

  def index() { 

    def result = [:]

    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    params.offset = params.offset ? params.int('offset') : 0

    //def params_set=params.entrySet()

    result.schemaInfo = Schema.findByName(params.schema);

    def query_str = params.q
    log.debug("query: ${query_str}");

    def search = esclient.search{
      indices "gangplank"
      source {
        from = params.offset
        size = params.max
        query {
          query_string (query: query_str)
        }
        sort = [
          '_id' : [ 'order' : 'asc' ]
        ]

      }
    }

    if ( search?.response ) {
      result.hits = search.response.hits
      result.resultsTotal = search.response.hits.totalHits
      log.debug("Got ${result.resultsTotal} hits");
    }
    else {
      log.debug("No search response");
    }

    result
  }
}
