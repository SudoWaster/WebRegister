package pl.cezaryregec;

import javax.ws.rs.ApplicationPath;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {
    
    public ApplicationConfig() {
        register(new ContainerLifecycleListener() {
            @Override
            public void onStartup(Container cntnr) {
                
            }

            @Override
            public void onReload(Container cntnr) {
                
            }

            @Override
            public void onShutdown(Container cntnr) {
               
            }
            
        });
    }
}
