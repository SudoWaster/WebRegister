package pl.cezaryregec;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.spi.PersistenceUnitTransactionType;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetServer;
import pl.cezaryregec.auth.HashGenerator;
import pl.cezaryregec.auth.ShaHashGenerator;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.auth.UserServiceImpl;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class DefaultModule extends AbstractModule {

    @Override
    protected void configure() {
        JpaPersistModule jpaModule = new JpaPersistModule("pl.cezaryregec_WebRegister_ejb_local");
        jpaModule.properties(getJPA());
        
        install(jpaModule);
        
        bind(JPAInitializer.class).asEagerSingleton();
        bind(Config.class).to(DefaultConfig.class);
        bind(HashGenerator.class).to(ShaHashGenerator.class);
        bind(UserService.class).to(UserServiceImpl.class);
    }
    
    private Map getJPA() {
        Map properties = new HashMap();
        
        properties.put(PersistenceUnitProperties.TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
        
        properties.put(PersistenceUnitProperties.JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://localhost:5432/register");
        properties.put(PersistenceUnitProperties.JDBC_USER, "test");
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "test");
        
        properties.put(PersistenceUnitProperties.LOGGING_LEVEL, "FINE");
        properties.put(PersistenceUnitProperties.LOGGING_TIMESTAMP, "false");
        properties.put(PersistenceUnitProperties.LOGGING_THREAD, "false");
        properties.put(PersistenceUnitProperties.LOGGING_THREAD, "false");
        properties.put(PersistenceUnitProperties.LOGGING_SESSION, "false");
        
        properties.put(PersistenceUnitProperties.TARGET_SERVER, TargetServer.None);
        
        return properties;
    }
    

    @Singleton
    public static class JPAInitializer {
        @Inject
        public JPAInitializer(final PersistService service) {
            service.start();
        }	
    }
    

}