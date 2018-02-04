package pl.cezaryregec.auth;

import pl.cezaryregec.auth.entities.User;
import pl.cezaryregec.auth.entities.Token;
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
    
    @PersistenceContext(unitName="pl.cezaryregec_WebRegister-ejb_ejb_1.0-SNAPSHOTPU")
    public EntityManager entityManager;
    
    public UserServiceImpl() {
    }
    
    @Override
    public User getUser(String mail) throws NoResultException {
        
        Query q = entityManager.createNamedQuery("User.findByMail");
        q.setParameter("mail", mail);
        
        return (User) q.getSingleResult();
    }
    
    @Override
    public Token registerSession(String password, User user) throws ForbiddenException {
        
        // TODO: hashes
        
        if(user.checkPassword(password)) {
            return createToken(user);
        }
        
        throw new ForbiddenException();
    }
        
        
    private Token createToken(User user) {
        // TODO: expiration config
        Token token = new Token();
        token.setExpiration(0);
        token.setUser(user.getId());

        entityManager.merge(token);
        
        return token;
    }
}
