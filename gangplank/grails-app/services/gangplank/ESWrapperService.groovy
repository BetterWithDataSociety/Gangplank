package gangplank

import org.elasticsearch.groovy.node.GNode
import org.elasticsearch.groovy.node.GNodeBuilder
import static org.elasticsearch.groovy.node.GNodeBuilder.*
import org.codehaus.groovy.grails.commons.ApplicationHolder


class ESWrapperService {

  static transactional = false

  def gNode = null;

  @javax.annotation.PostConstruct
  def init() {
    log.debug("Init");

    def clus_nm = ApplicationHolder.application.config.gangplank.es.cluster ?: "elasticsearch"

    log.debug("Using ${clus_nm} as ES cluster name...");


    def nodeBuilder = new org.elasticsearch.groovy.node.GNodeBuilder()

    log.debug("Construct node settings");

    nodeBuilder.settings {
      node {
        client = true
      }
      cluster {
        name = clus_nm
      }
      http {
        enabled = false
      }
      discovery {
        zen {
          minimum_master_nodes=1
          ping {
            unicast {
              hosts = [ "localhost" ]
            }
          }
        }
      }
    }

    log.debug("Constructing node...");
    gNode = nodeBuilder.node()

    log.debug("Init completed");
  }

  @javax.annotation.PreDestroy
  def destroy() {
    log.debug("Destroy");
    gNode.close()
    log.debug("Destroy completed");
  }

  def getNode() {
    log.debug("getNode()");
    gNode
  }


  def clearDownAndInitES() {
    log.debug("Clear down and init ES");
    org.elasticsearch.groovy.node.GNode esnode = getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    // Get hold of an index admin client
    org.elasticsearch.groovy.client.GIndicesAdminClient index_admin_client = new org.elasticsearch.groovy.client.GIndicesAdminClient(esclient);

    try {
      // Drop any existing kbplus index
      log.debug("Dropping old ES index....");
      def future = index_admin_client.delete {
        indices 'gangplank'
      }
      future.get()
    }
    catch ( Exception e ) {
      log.warn("Problem deleting index...",e);
    }

    // Create an index if none exists
    log.debug("Create new ES index....");
    def future = index_admin_client.create {
      index 'gangplank'
    }
    future.get()
  }

}
