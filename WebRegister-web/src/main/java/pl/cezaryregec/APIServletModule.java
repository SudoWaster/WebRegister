package pl.cezaryregec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.auth.UserServiceImpl;


/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class APIServletModule extends ServletModule {
    
    @Override
    public void configureServlets() {
        //bind(AuthResource.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(ObjectMapper.class);
    }
}
