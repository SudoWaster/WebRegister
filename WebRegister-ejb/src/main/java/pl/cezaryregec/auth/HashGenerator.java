package pl.cezaryregec.auth;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public interface HashGenerator {
    
    public String getHash(String... input);
    
    public String getHash(String input);
    
    public String getSaltHash(String input, String salt);
    
    public byte[] getHash(byte[] input);
    
}
