package pl.cezaryregec.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.ForbiddenException;
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
    public Provider<EntityManager> entityManager;
    
    @Inject
    public ObjectMapper objectMapper;
    
    @Override
    @Transactional
    public String getUserJson(String mail) throws NoResultException, JsonProcessingException {
        
        User user = getUser(mail);
        
        return objectMapper.writeValueAsString(user);
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
    public String getRegisteredTokenJson(String password, String userJson) throws ForbiddenException, JsonProcessingException, IOException {
        
        User actualUser = matchUser(userJson);
        
        // TODO: hashes 
                
        if(actualUser.checkPassword(password)) {
            return objectMapper.writeValueAsString(createToken(actualUser));
        }
        
        throw new ForbiddenException();
    }
        
    private Token createToken(User user) {
        // TODO: expiration config
        Token token = new Token();
        token.setExpiration(0);
        token.setUser(user.getId());

        entityManager.get().merge(token);
        
        return token;
    }
    
    @Override
    @Transactional
    public void removeToken(String tokenId) {
        
        Query tokenQuery = entityManager.get().createNamedQuery("Token.findById", Token.class);
        tokenQuery.setParameter("id", tokenId);
        
        Token token = (Token) tokenQuery.getSingleResult();
        
        entityManager.get().remove(token);
    }
}
