package nextflow.openbis

import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import groovyx.gpars.dataflow.DataflowWriteChannel
import nextflow.Channel
import nextflow.openbis.config.OpenbisInstance
import nextflow.openbis.config.OpenbisLoginMethod
import groovy.util.logging.Slf4j

@Slf4j
class OpenbisCollectionHandler {
    private OpenBIS instance
    private DataflowWriteChannel target
    private String collection

    OpenbisCollectionHandler withInstance(OpenbisInstance config){
        def instance = new OpenBIS(config.url)
        switch (config.method){
            case OpenbisLoginMethod.ANONYMOUS:
                instance.loginAsAnonymousUser()
                break
            case OpenbisLoginMethod.TOKEN:
                instance.setSessionToken(config.token)
                break
            case OpenbisLoginMethod.PASSWORD:
                instance.login(config.username, config.password)
        }
        this.instance = instance
        return this
    }
    OpenbisCollectionHandler withCollection(String collection){
        this.collection = collection
        return  this
    }

    OpenbisCollectionHandler withTarget(DataflowWriteChannel channel) {
        this.target = channel
        return this
    }

    void emitObjects(){
        def sc = new ExperimentSearchCriteria()
        sc.withAndOperator().withIdentifier().thatEquals(collection)
        def fo = new ExperimentFetchOptions()
        fo.withSamples()
        def ids = instance.searchExperiments(sc, fo)
        if(ids.totalCount > 0){
            Experiment exp = ids.objects[0]
            exp.samples.forEach {it -> target.bind(it)}
            target.bind(Channel.STOP)
        }

    }

}
