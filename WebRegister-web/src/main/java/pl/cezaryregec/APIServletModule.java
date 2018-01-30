package pl.cezaryregec;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.auth.UserServiceImpl;
import pl.cezaryregec.resources.AuthResource;


/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class APIServletModule extends ServletModule {
    
    @Override
    public void configureServlets() {
        //bind(AuthResource.class);
        bind(UserService.class).to(UserServiceImpl.class);
    }

}
