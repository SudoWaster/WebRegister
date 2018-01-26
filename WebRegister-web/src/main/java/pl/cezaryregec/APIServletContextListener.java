package pl.cezaryregec;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class APIServletContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new APIServletModule());
    }
    
}
