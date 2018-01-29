package pl.cezaryregec;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class APIServletContextListener extends GuiceServletContextListener {

    public static Injector injector;
    
    @Override
    protected Injector getInjector() {
        injector = Guice.createInjector(new APIServletModule());
        return injector;
    }
    
}
