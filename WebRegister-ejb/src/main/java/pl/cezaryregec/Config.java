package pl.cezaryregec;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public interface Config {
    
    final long sessionTime = 900000;
    final String saltPhrase = "WebRegister";
    
    long getSessionTime();
    String getSaltPhrase();
    
}
