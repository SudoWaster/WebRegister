package pl.cezaryregec;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    }
    
    @Provides
    public EntityManager getEntityManager() {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "pl.cezaryregec_WebRegister-web_war_1.0" );
        EntityManager em = emfactory.createEntityManager();
        
        return em;
    }
    
    @Provides
    public UserService getUserService() {
        return new UserServiceImpl();
    }

}
