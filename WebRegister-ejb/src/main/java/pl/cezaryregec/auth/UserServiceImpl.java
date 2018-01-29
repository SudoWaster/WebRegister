package pl.cezaryregec.auth;

import javax.ejb.Stateless;
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
public class UserServiceImpl implements UserService {
    
    @PersistenceContext(unitName="pl.cezaryregec_WebRegister-ejb")
    protected EntityManager entitymanager;
    
    @Override
    public User getUser(String mail) throws NoResultException {
        
        Query q = entitymanager.createNamedQuery("User.findByMail");
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
        entitymanager.getTransaction().begin();

        // TODO: expiration config
        Token token = new Token();
        token.setExpiration(0);
        token.setUser(user.getId());

        entitymanager.persist(token);
        entitymanager.getTransaction().commit();
        
        return token;
    }
}
