package pl.cezaryregec.auth;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public interface HashGenerator {
    
    String generateHash(String... input);
    
    String generateHash(String input);
    
    String generateSaltHash(String input, String salt);
    
    byte[] generateHash(byte[] input);
    
}
