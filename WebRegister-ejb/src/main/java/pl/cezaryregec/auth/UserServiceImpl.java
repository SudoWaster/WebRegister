package pl.cezaryregec.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import pl.cezaryregec.Config;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.User;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Stateless
public class UserServiceImpl implements UserService {
    
    //@PersistenceContext(unitName="pl.cezaryregec_WebRegister-ejb_ejb_1.0-SNAPSHOTPU")
    @Inject
    private Provider<EntityManager> entityManager;
    
    @Inject
    private HashGenerator hashGenerator;
    
    @Inject
    private ObjectMapper objectMapper;
    
    @Inject
    private Config config;
    
    
    
    @Override
    @Transactional
    public String getUserJson(String mail) throws NoResultException, JsonProcessingException {
        
        User user = getUser(mail);
        
        return objectMapper.writeValueAsString(user);
    }
    
    @Override
    @Transactional
    public String getRegisteredTokenJson(String userJson, String password, String fingerprint) 
            throws NotAuthorizedException, JsonProcessingException, IOException {
        
        User actualUser = matchUser(userJson);
        
        String hashedPassword = hashGenerator.getSaltHash(getFormatedForHash(actualUser.getMail(), password), config.getSaltPhrase());
                
        if(actualUser.checkPassword(hashedPassword)) {
            return objectMapper.writeValueAsString(createToken(actualUser, fingerprint));
        }
        
        throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED));
    }
    
    private User getUser(String mail) throws NoResultException {
        
        Query userQuery = entityManager.get().createNamedQuery("User.findByMail");
        userQuery.setParameter("mail", mail);
        
        return (User) userQuery.getSingleResult();
    }
    
    
    private User matchUser(String userJson) throws IOException {
        User passedUser = objectMapper.readValue(userJson, User.class);
        
        Query userQuery = entityManager.get().createNamedQuery("User.match");
        userQuery.setParameter("id", passedUser.getId());
        userQuery.setParameter("mail", passedUser.getMail());
        
        return (User) userQuery.getSingleResult();
    }
    
    @Override
    @Transactional
    public void removeToken(String tokenId) {
        
        Token token = getToken(tokenId);
        
        entityManager.get().remove(token);
    }

    @Override
    @Transactional
    public boolean isTokenValid(String tokenId, String fingerprint) {
        Token token = getToken(tokenId);
        boolean isValid = token.isValid(fingerprint);
        
        if(!token.hasExpired()) {
            removeToken(tokenId);
        }
        
        return isValid;
    }
    
    private Token createToken(User user, String fingerprint) {
        
        Token token = new Token();
        token.setExpiration(config.getSessionTime());
        token.setUser(user.getId());
        token.setFingerprint(fingerprint);

        entityManager.get().merge(token);
        
        return token;
    }
    
    private Token getToken(String tokenId) {
        Query tokenQuery = entityManager.get().createNamedQuery("Token.findById", Token.class);
        tokenQuery.setParameter("id", tokenId);
        
        return (Token) tokenQuery.getSingleResult();
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
    
    private String getFormatedForHash(String mail, String password) {
        return mail + password;
    }
}
