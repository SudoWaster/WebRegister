package pl.cezaryregec;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import org.glassfish.jersey.servlet.ServletContainer;
import pl.cezaryregec.services.AuthService;
import pl.cezaryregec.rest.RestBuilder;
import pl.cezaryregec.rest.RestBuilderImpl;


/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class APIServletModule extends ServletModule {
    
    @Override
    public void configureServlets() {
        RestBuilder app = new RestBuilderImpl();
        
        bind(ServletContainer.class).in(Scopes.SINGLETON);
        serve("/*").with(ServletContainer.class, app.buildParams("pl.cezaryregec.services"));
        
        bind(AuthService.class).in(Scopes.SINGLETON);
    }
}
