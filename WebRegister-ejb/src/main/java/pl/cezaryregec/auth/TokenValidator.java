package pl.cezaryregec.auth;

import java.sql.Timestamp;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class TokenValidator {
    
    private final Token token;
   
    public TokenValidator(Token token) {
        this.token = token;
    }
    
    public boolean hasTokenExpired() {
        return token.getExpiration().before(new Timestamp(System.currentTimeMillis()));
    }
    
    public boolean isTokenValid(String fingerprint) {
        
        Boolean isUnauthorized = token.getUser() == null || token.getUser().getType().equals(UserType.UNAUTHORIZED);
        
        return !hasTokenExpired() && !isUnauthorized && token.getFingerprint().equals(fingerprint);
    }
}
