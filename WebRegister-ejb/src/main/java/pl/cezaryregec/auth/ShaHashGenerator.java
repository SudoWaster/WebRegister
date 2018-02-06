package pl.cezaryregec.auth;

import com.google.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.cezaryregec.Config;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class ShaHashGenerator implements HashGenerator {
    
    @Override
    public String getHash(String... input) {
        
        String concatenatedString = "";
        
        for(String text : input) {
            concatenatedString += text;
        }
            
        return getHash(concatenatedString);
    }
    
    @Override
    public String getSaltHash(String input, String salt) {
        
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
        
        return getHash(result);
    }

    @Override
    public String getHash(String input) {
        byte[] hash = getHash(input.getBytes(StandardCharsets.UTF_8));
        
        return Base64.getEncoder().encodeToString(hash);
    }
    
    @Override
    public byte[] getHash(byte[] input) {
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
    
}
