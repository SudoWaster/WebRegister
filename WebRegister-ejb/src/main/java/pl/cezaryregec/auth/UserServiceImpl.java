package pl.cezaryregec.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.Token;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.ForbiddenException;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Stateless
@Default
public class UserServiceImpl implements UserService {
    
    //@PersistenceContext(unitName="pl.cezaryregec_WebRegister-ejb_ejb_1.0-SNAPSHOTPU")
    @Inject
    public Provider<EntityManager> entityManager;
    
    @Override
    @Transactional
    public User getUser(String mail) throws NoResultException {
        
        Query q = entityManager.get().createNamedQuery("User.findByMail");
        q.setParameter("mail", mail);
        
        return (User) q.getSingleResult();
    }
    
    @Override
    @Transactional
    public Token registerSession(String password, User user) throws ForbiddenException {
        
        // TODO: hashes
        
        if(user.checkPassword(password)) {
            return createToken(user);
        }
        
        throw new ForbiddenException();
    }
        
        
    @Transactional
    private Token createToken(User user) {
        // TODO: expiration config
        Token token = new Token();
        token.setExpiration(0);
        token.setUser(user.getId());

        entityManager.get().merge(token);
        
        return token;
    }
}
