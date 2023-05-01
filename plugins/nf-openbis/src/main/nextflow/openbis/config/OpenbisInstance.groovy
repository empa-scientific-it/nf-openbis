package nextflow.openbis.config

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Represent an openBIS instance configuration
 * @author Simone Baffelli <simone.baffelli@gmail.com>
*/
@ToString(includePackage = false, includeNames = true)
@EqualsAndHashCode
class OpenbisInstance {


    String url
    String username
    String password
    String token
    OpenbisLoginMethod method


    OpenbisInstance(Map opts){
        this.url = opts.url
        if(opts.containsKey('token')){
            this.token = opts.token
            this.method = OpenbisLoginMethod.TOKEN
        } else if (opts.containsKey('anonymous')){
            this.method = OpenbisLoginMethod.ANONYMOUS
        }else if (opts.containsKey('username') and opts.containsKey('password')){
            this.method = OpenbisLoginMethod.PASSWORD
            this.username = opts.username
            this.password = opts.password
        }


    }

}
