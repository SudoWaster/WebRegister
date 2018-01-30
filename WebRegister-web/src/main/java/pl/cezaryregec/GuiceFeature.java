package pl.cezaryregec;

import javax.annotation.Priority;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Priority(1)
public class GuiceFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        ServiceLocator locator = ServiceLocatorProvider.getServiceLocator(context);
        
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
        
        GuiceIntoHK2Bridge guiceBridge = locator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(APIServletContextListener.injector);
                
        return true;
    }
} 
