package pl.cezaryregec.auth;

import javax.servlet.http.HttpServletRequest;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public interface TokenService {
    Token getRegisteredToken(String mail, String password, String fingerprint);
    
    void removeToken(String tokenId);
    
    void refreshToken(String tokenId, String fingerprint);
    
    void refreshToken(Token token);
    
    Token getToken(String tokenId);
    
    void validateToken(String tokenId, HttpServletRequest request);
    
    boolean isTokenValid(String tokenId, String fingerprint, UserType type);
    
    boolean isTokenValid(String tokenId, String fingerprint);
    
    String getFingerprint(HttpServletRequest request);
}
