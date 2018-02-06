package pl.cezaryregec.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import pl.cezaryregec.Config;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class PropertiesConfig implements Config {

    @Inject
    @Named("sessionTime")
    private long sessionTime;
    
    @Inject
    @Named("saltPhrase")
    private String saltPhrase;
    
    @Override
    public long getSessionTime() {
        return sessionTime;
    }
    
    @Override
    public String getSaltPhrase() {
        return saltPhrase;
    }
    
}
