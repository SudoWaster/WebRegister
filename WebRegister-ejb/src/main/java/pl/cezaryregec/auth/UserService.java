package pl.cezaryregec.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Local
@Remote
@Transactional
public interface UserService {
    
    void createUser(String mail, String password, String firstname, String lastname, UserType type);
    
    void setUser(String updatedUserJson, String password, String tokenId)
            throws IOException;
    
    String getUserJson(String mail) 
            throws JsonProcessingException;
    
    String getUserJson(int id) 
            throws JsonProcessingException;
    
    String getUserJsonFromToken(String tokenId) 
            throws JsonProcessingException;
    
    String getUsersJson()
            throws JsonProcessingException;
    
    String getRegisteredTokenJson(String password, String userJson, String fingerprint) 
            throws IOException;
    
    void removeToken(String tokenId);
    
    void refreshToken(String tokenId, String fingerprint);
    
    boolean isTokenValid(String tokenId, String fingerprint);
    
    String getFingerprint(HttpServletRequest request);
}
