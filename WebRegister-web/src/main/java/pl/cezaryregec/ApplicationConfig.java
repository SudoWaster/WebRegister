package pl.cezaryregec;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
//@ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("pl.cezaryregec.filter");
        packages("pl.cezaryregec.resources");
        
        register(new GuiceFeature());
    }
}
