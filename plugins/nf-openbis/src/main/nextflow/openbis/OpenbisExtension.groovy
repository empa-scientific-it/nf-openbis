package nextflow.openbis


import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovyx.gpars.dataflow.DataflowReadChannel
import groovyx.gpars.dataflow.DataflowWriteChannel
import nextflow.Channel
import nextflow.Session
import nextflow.extension.CH
import nextflow.extension.DataflowHelper
import nextflow.openbis.config.OpenbisConfig
import nextflow.plugin.extension.Factory
import nextflow.plugin.extension.Function
import nextflow.plugin.extension.Operator
import nextflow.plugin.extension.PluginExtensionPoint

import nextflow.openbis.config.OpenbisConfig

/**
 * plugin extension implementing an openBIS input/output channel
 *
 * @author : Simone Baffelli <simone.baffelli@gmail.com>
 *
 */
@Slf4j
@CompileStatic
class OpenbisExtension extends PluginExtensionPoint {

    /*
     * A session hold information about current execution of the script
     */
    private Session session

    /*
     * A Custom config extracted from nextflow.config under hello tag
     * nextflow.config
     * ---------------
     * docker{
     *   enabled = true
     * }
     * ...
     * openbis{
     *    test {
     *          url = something
     *          password = something else
*          }
     * }
     */
     private OpenbisConfig config

    /*
     * nf-core initializes the plugin once loaded and session is ready
     * @param session
     */
    @Override
    protected void init(Session session) {
        this.session = session
        this.config = new OpenbisConfig(session.config.navigate('openbis') as Map)
        log.debug(config.toString())
    }

    /*
     * {@code fromCollection} is a `producer` method and will be available to the script because:
     *
     * - it's public
     * - it returns a DataflowWriteChannel
     * - it's marked with the @Factory annotation
     *
     * The method can require arguments but it's not mandatory, it depends of the business logic of the method.
     *
     */
    @Factory
    DataflowWriteChannel fromCollection(String identifier, String instance) {
        final channel = CH.create()
        def handler = new OpenbisCollectionHandler().withCollection(identifier).withInstance(config.getInstance(instance)).withTarget(channel)
        session.addIgniter((action) -> handler.emitObjects())
        return channel
    }





}
