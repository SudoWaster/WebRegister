package pl.cezaryregec.config;

import pl.cezaryregec.Config;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class DefaultConfig implements Config {
    
    private long sessionTime = 600000;
    private String saltPhrase = "WebRegister";
    
    @Override
    public long getSessionTime() {
        return sessionTime;
    }
    
    @Override
    public String getSaltPhrase() {
        return saltPhrase;
    }
    
}
