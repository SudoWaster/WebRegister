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
    
    @Override
    public long getSessionTime() {
        return sessionTime;
    }
    
}
