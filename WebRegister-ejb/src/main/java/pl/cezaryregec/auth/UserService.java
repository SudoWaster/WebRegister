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
    
    public void createUser(String mail, String password, String firstname, String lastname, UserType type);
    
    public void setUser(String updatedUserJson, String password, String tokenId)
            throws IOException;
    
    public String getUserJson(String mail) 
            throws JsonProcessingException;
    
    public String getUserJson(int id) 
            throws JsonProcessingException;
    
    public String getUserJsonFromToken(String tokenId) 
            throws JsonProcessingException;
    
    public String getUsersJson()
            throws JsonProcessingException;
    
    public String getRegisteredTokenJson(String password, String userJson, String fingerprint) 
            throws IOException;
    
    public void removeToken(String tokenId);
    
    public void refreshToken(String tokenId, String fingerprint);
    
    public boolean isTokenValid(String tokenId, String fingerprint);
    
    public String getFingerprint(HttpServletRequest request);
}
