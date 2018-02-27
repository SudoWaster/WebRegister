package pl.cezaryregec.auth;

import com.google.inject.persist.Transactional;
import java.io.IOException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.User;
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
    
    void setUser(int id, String firstname, String lastname);
    
    void setUserCredentials(int id, String oldPassword, String mail, String password, String tokenId);
    
    void deleteUser(Integer userId);
    
    User getUser(String mail);
    
    User getUser(int id);
    
    User getUserFromToken(String tokenId);
    
    List<User> getUsers();
    
    Token getRegisteredToken(String mail, String password, String fingerprint);
    
    void removeToken(String tokenId);
    
    void refreshToken(String tokenId, String fingerprint);
    
    void validateToken(String tokenId, HttpServletRequest request);
    
    boolean isTokenValid(String tokenId, String fingerprint, UserType type);
    
    boolean isTokenValid(String tokenId, String fingerprint);
    
    String getFingerprint(HttpServletRequest request);
}
