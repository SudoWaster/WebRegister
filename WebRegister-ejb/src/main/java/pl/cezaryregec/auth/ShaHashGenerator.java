package pl.cezaryregec.auth;

import com.google.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.cezaryregec.Config;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class ShaHashGenerator implements HashGenerator {
    
    @Inject
    private Config config;
    
    @Override
    public String generateHash(String... input) {
        
        String concatenatedString = "";
        
        for(String text : input) {
            concatenatedString += text;
        }
            
        return generateHash(concatenatedString);
    }
    
    @Override
    public String generateHashedPassword(String mail, String password) {
        return generateSaltHash(getFormatedForHash(mail, password), config.getSaltPhrase());
    }
    
    @Override
    public String generateSaltHash(String input, String salt) {
        
        String result = input;
        int j = 0;
        
        for(int i = 0; i < result.length(); i++) {
            if(i % (int) Math.sqrt(input.length()) == 0) {
                result = result.substring(0, i) + salt.charAt(j) + result.substring(i + 1);
            }
        }
        
        if(j < salt.length() - 1) {
            result += salt.substring(j);
        }
        
        return generateHash(result);
    }

    @Override
    public String generateHash(String input) {
        byte[] hash = generateHash(input.getBytes(StandardCharsets.UTF_8));
        
        return encodeToString(hash);
    }
    
    @Override
    public byte[] generateHash(byte[] input) {
        MessageDigest digest;
        byte[] result = null;
        
        try {
            digest = MessageDigest.getInstance("SHA-256");
            result = digest.digest(input);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ShaHashGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    @Override
    public String encodeToString(byte[] input) {
        String result = "";
        
        for(byte b : input) {
            result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        }
        
        return result;
    }
    
    private String getFormatedForHash(String mail, String password) {
        return mail + password;
    }
    
}
