package pl.cezaryregec.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import pl.cezaryregec.Config;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.User;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class TokenServiceImpl implements TokenService {

    @Inject
    private Provider<EntityManager> entityManager;
    
    @Inject
    private UserService userService;
    
    @Inject
    private Config config;
    
    @Inject
    private HashGenerator hashGenerator;
    
    @Override
    @Transactional
    public Token getRegisteredToken(String mail, String password, String fingerprint) 
            throws NotAuthorizedException {
        
        User user = userService.getUser(mail);
        
        String hashedPassword = hashGenerator.generateHashedPassword(user.getMail(), password);
                
        if(user.checkPassword(hashedPassword)) {
            return createToken(user, fingerprint);
        }
        
        throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED));
    }
    
    @Override
    @Transactional
    public void removeToken(String tokenId) {
        
        Token token = getToken(tokenId);
        token.setUser(null);
        
        entityManager.get().merge(token);
        entityManager.get().remove(token);
    }
    
    @Override
    @Transactional
    public void refreshToken(String tokenId, String fingerprint) {
        Token token = getToken(tokenId);
        
        token.setExpiration(config.getSessionTime());
        
        entityManager.get().merge(token);
    }
    
    @Override
    @Transactional
    public void refreshToken(Token token) {
        token.setExpiration(config.getSessionTime());
        
        entityManager.get().merge(token);
    }
    
    @Override
    @Transactional
    public void validateToken(String tokenId, HttpServletRequest request) {
        
        if(!isTokenValid(tokenId, getFingerprint(request))) {
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        
        refreshToken(tokenId, getFingerprint(request));
    }
    
    @Override
    @Transactional
    public boolean isTokenValid(String tokenId, String fingerprint) {
        try {
            TokenValidator validator = new TokenValidator(getToken(tokenId));
            
            if(validator.hasTokenExpired()) {
                removeToken(tokenId);
                return false;
            }
            
            return validator.isTokenValid(fingerprint);
        } catch(NotFoundException ex) {
            return false;
        }
    }
    
    private Token createToken(User user, String fingerprint) {
        Token token = new Token();
        token.setExpiration(config.getSessionTime());
        token.setUser(user);
        token.setFingerprint(fingerprint);
        token.setToken(generateTokenId(user, fingerprint));
        
        entityManager.get().merge(token);
        
        return token;
    }
    
    @Override
    @Transactional
    public Token getToken(String tokenId) {
        Query tokenQuery = entityManager.get().createNamedQuery("Token.findById", Token.class);
        tokenQuery.setParameter("id", tokenId);
        
        try {
            return (Token) tokenQuery.getSingleResult();
        } catch(NoResultException ex) {
            throw new NotFoundException("Token does not exist");
        }
    }

    @Override
    public String getFingerprint(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String remoteAddress = request.getHeader("HTTP_X_FORWARDED_FOR");

        if (remoteAddress == null) {
            remoteAddress = request.getRemoteAddr();
        }
        
        return userAgent + "\n" + remoteAddress;
    }
    
    private String generateTokenId(User user, String fingerprint) {
        String tokenId = hashGenerator.generateHash(user.getId() + fingerprint + new Date().getTime());
        return tokenId.replaceAll("[^a-zA-Z0-9]+", "-");
    }
}
