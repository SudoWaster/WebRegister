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
    private Long sessionTimeProp;
    
    @Inject
    @Named("saltPhrase")
    private String saltPhraseProp;
    
    @Override
    public long getSessionTime() {
        return sessionTimeProp != null ? sessionTimeProp : sessionTime;
    }
    
    @Override
    public String getSaltPhrase() {
        return saltPhraseProp != null ? saltPhraseProp : saltPhrase;
    }
    
}
