package pl.cezaryregec.auth;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public interface HashGenerator {
    
    String generateHash(String... input);
    
    String generateHash(String input);
    
    String encodeToString(byte[] input);
    
    String generateHashedPassword(String mail, String password);
    
    String generateSaltHash(String input, String salt);
    
    byte[] generateHash(byte[] input);
    
}
