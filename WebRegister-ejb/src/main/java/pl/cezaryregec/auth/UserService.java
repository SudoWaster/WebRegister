package pl.cezaryregec.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Local
@Remote
@Transactional
public interface UserService {
    
    public String getUserJson(String mail) 
            throws JsonProcessingException;
    
    public String getRegisteredTokenJson(String password, String userJson, String fingerprint) 
            throws IOException, JsonProcessingException;
    
    public void removeToken(String tokenId);
    
    public boolean isTokenValid(String tokenId, String fingerprint);
    
    public String getFingerprint(HttpServletRequest request);
}
