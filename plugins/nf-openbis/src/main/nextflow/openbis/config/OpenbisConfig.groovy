package nextflow.openbis.config

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import nextflow.openbis.config.OpenbisInstance

/**
 * This class allows model an specific configuration, extracting values from a map and converting
 *
 * In this plugin, the user can configure how the messages are prefixed with a String, i.e.
 * due a nextflow.config
 *
 * hello {
 *     prefix = '>>'
 * }
 *
 * when the plugin reverse a String it will append '>>' at the beginning instead the default 'Mr.'
 *
 * We anotate this class as @PackageScope to restrict the access of their methods only to class in the
 * same package
 *
 * @author : jorge <jorge.aguilera@seqera.io>
 *
 */
@ToString(includePackage = false, includeNames = true)
@EqualsAndHashCode
@Slf4j
public class OpenbisConfig {

    private Map<String, OpenbisInstance> openbisInstances

    OpenbisConfig(Map<String, Map> config){
        Map<String, OpenbisInstance> res = config.collectEntries {k, v -> [(k): new OpenbisInstance(v)]}
        this.openbisInstances = res
    }

    OpenbisInstance getInstance(String instance){
        return openbisInstances[instance]
    }


}
