package pl.cezaryregec.config;

import pl.cezaryregec.Config;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class DefaultConfig implements Config {
    
    private long sessionTime = 600000;
    
    @Override
    public long getSessionTime() {
        return sessionTime;
    }
    
}
