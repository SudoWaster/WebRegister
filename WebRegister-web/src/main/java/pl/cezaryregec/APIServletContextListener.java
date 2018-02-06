package pl.cezaryregec;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import javax.servlet.ServletContextEvent;
/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class APIServletContextListener extends GuiceServletContextListener {

    public static Injector injector;
    public static PersistService persistService;
    
    @Override
    protected Injector getInjector() {
        injector = Guice.createInjector(new APIServletModule(), new JpaPersistModule("pl.cezaryregec_WebRegister-ejb_ejb_1.0-SNAPSHOTPU"));
        persistService = injector.getInstance(PersistService.class);
        persistService.start();
        return injector;
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        persistService.stop();
    }
    
}
