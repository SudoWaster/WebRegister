package pl.cezaryregec.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import pl.cezaryregec.Config;
import pl.cezaryregec.entities.Group;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.UserType;
import pl.cezaryregec.groups.GroupService;

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
    private GroupService groupService;
    
    @Inject
    private HashGenerator hashGenerator;
    
    @Inject
    private Config config;
    
    

    @Override
    @Transactional
    public void createUser(String mail, String password, String firstname, String lastname, UserType type) {
        String hashedPassword = hashGenerator.generateHashedPassword(mail, password);
        
        if(userExists(mail)) {
            throw new ForbiddenException("User mail is taken");
        }
        
        User user = new User();
        user.setMail(mail);
        user.setPassword(hashedPassword);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setType(type);
        
        entityManager.get().merge(user);
    }
    
    
    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = getUser(userId);
        
        List<Group> groups = user.getGroups();
        
        for(Group group : groups) {
            groupService.removeFromGroup(user, group.getId(), true);
        }
        
        entityManager.get().remove(user);
    }
    
    @Override
    @Transactional
    public void setUser(int id, String firstname, String lastname) {
        
        User updatedUser = getUser(id);
        
        updatedUser.setFirstname(firstname != null ? firstname : updatedUser.getFirstname());
        updatedUser.setLastname(lastname != null ? lastname : updatedUser.getLastname());
        
        entityManager.get().merge(updatedUser);
    }
    
    @Override
    @Transactional
    public void setUserCredentials(int id, String oldPassword, String mail, String password, String tokenId) {
        
        User updatedUser = getUser(id);
        
        if(!isUserPermitted(updatedUser, tokenId, oldPassword) 
                || (!updatedUser.getMail().equals(mail)) && userExists(mail)) {
            throw new ForbiddenException();
        }
        
        String hashedPassword = hashGenerator.generateHashedPassword(mail, password);
        
        updatedUser.setMail(mail);
        updatedUser.setPassword(hashedPassword);
        
        entityManager.get().merge(updatedUser);
    }
    
    @Override
    @Transactional
    public User getUserFromToken(String tokenId) throws NoResultException {
        return getToken(tokenId).getUser();
    }
    
    @Override
    @Transactional
    public List<User> getUsers() throws NoResultException {
        
        Query userQuery = entityManager.get().createNamedQuery("User.findAll", User.class);

        return (List<User>) userQuery.getResultList();
    }
    
    @Override
    @Transactional
    public Token getRegisteredToken(String mail, String password, String fingerprint) 
            throws NotAuthorizedException {
        
        User user = getUser(mail);
        
        String hashedPassword = hashGenerator.generateHashedPassword(user.getMail(), password);
                
        if(user.checkPassword(hashedPassword)) {
            return createToken(user, fingerprint);
        }
        
        throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED));
    }
    
    @Override
    @Transactional
    public User getUser(String mail) {
        
        Query userQuery = entityManager.get().createNamedQuery("User.findByMail", User.class);
        userQuery.setParameter("mail", mail);
        
        User result;
        
        try {
            result = (User) userQuery.getSingleResult();
        } catch(NoResultException ex) {
            throw new NotFoundException("User does not exist");
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public User getUser(int id) {
            
        Query userQuery = entityManager.get().createNamedQuery("User.findById", User.class);
        userQuery.setParameter("id", id);
        
        User result;
        
        try {
            result = (User) userQuery.getSingleResult();
        } catch(NoResultException ex) {
            throw new NotFoundException("User does not exist");
        }
        
        return result;
    }
    
    private boolean userExists(String mail) {
        
        try {
            User existing = getUser(mail);
            return existing != null;
            
        } catch(NoResultException ex) {
            return false;
        }
    }
    
    @Override
    @Transactional
    public void removeToken(String tokenId) {
        
        Token token = getToken(tokenId);
        
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
    public void validateToken(String tokenId, HttpServletRequest request) {
        
        if(!isTokenValid(tokenId, getFingerprint(request))) {
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
    }
    
    @Override
    @Transactional
    public boolean isTokenValid(String tokenId, String fingerprint, UserType type) {
        User user;
        try {
            user = getUserFromToken(tokenId);
        } catch(NoResultException ex) {
            return false;
        }
        
        return isTokenValid(tokenId, fingerprint) && user.getType().getInt() >= type.getInt();
    }

    @Override
    @Transactional
    public boolean isTokenValid(String tokenId, String fingerprint) {
        try {
            return isTokenValid(getToken(tokenId), fingerprint);
        } catch(NoResultException ex) {
            return false;
        }
    }
    
    private boolean isTokenValid(Token token, String fingerprint) {
        boolean isValid = token.isValid(fingerprint) && token.getToken().equals(generateTokenId(token.getUser(), fingerprint));
        
        if(token.hasExpired() || token.getUser().getType() == UserType.UNAUTHORIZED) {
            removeToken(token.getToken());
            return false;
        }
        
        if(isValid) {
            refreshToken(token.getToken(), fingerprint);
        }
        
        return isValid;
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
    
    private boolean isUserPermitted(User targetUser, String tokenId, String password) {
        User currentUser;
        
        try {
            currentUser = getUserFromToken(tokenId);
        } catch(NoResultException ex) {
            return false;
        }
        
        return (currentUser.getType() == UserType.ADMIN || isUserLogged(currentUser, targetUser, password));
    }
    
    private boolean isUserLogged(User currentUser, User targetUser, String password) {
        
        String hashedPassword = hashGenerator.generateHashedPassword(currentUser.getMail(), password);
        
        return ( Objects.equals(currentUser.getId(), targetUser.getId()) 
                    && currentUser.checkPassword(hashedPassword) );
    }
    
    private String generateTokenId(User user, String fingerprint) {
        String tokenId = hashGenerator.generateHash(user.getId() + fingerprint + new Date().getTime());
        return tokenId.replaceAll("[^a-zA-Z0-9]+", "");
    }
}
