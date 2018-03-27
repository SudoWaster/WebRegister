package pl.cezaryregec.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
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
    private TokenService tokenService;
    
    @Inject
    private Config config;
    
    @Inject
    private HashGenerator hashGenerator;

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
        
        for(Group group : user.getGroups()) {
            groupService.removeFromGroup(user, group.getId(), true);
        }
        for(Token token : user.getSessions()) {
            entityManager.get().remove(token);
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
    public List<User> getUsers() throws NoResultException {
        
        Query userQuery = entityManager.get().createNamedQuery("User.findAll", User.class);

        return (List<User>) userQuery.getResultList();
    }
    
    @Override
    @Transactional
    public User getUser(String mail) {
        
        Query userQuery = entityManager.get().createNamedQuery("User.findByMail", User.class);
        userQuery.setParameter("mail", mail);
        
        User user;
        
        try {
            user = (User) userQuery.getSingleResult();
        } catch(NoResultException ex) {
            throw new NotFoundException("User does not exist");
        }
        
        return user;
    }
    
    @Override
    @Transactional
    public User getUser(int id) {
            
        Query userQuery = entityManager.get().createNamedQuery("User.findById", User.class);
        userQuery.setParameter("id", id);
        
        User user;
        
        try {
            user = (User) userQuery.getSingleResult();
        } catch(NoResultException ex) {
            throw new NotFoundException("User does not exist");
        }
        
        return user;
    }
    
    private boolean userExists(String mail) {
        try {
            User existing = getUser(mail);
            return existing != null;
            
        } catch(NotFoundException ex) {
            return false;
        }
    }    
    
    private boolean isUserPermitted(User targetUser, String tokenId, String password) {
        User currentUser;
        
        try {
            currentUser = tokenService.getToken(tokenId).getUser();
        } catch(NotFoundException ex) {
            return false;
        }
        
        return (currentUser.getType() == UserType.ADMIN || isUserLogged(currentUser, targetUser, password));
    }
    
    private boolean isUserLogged(User currentUser, User targetUser, String password) {
        
        String hashedPassword = hashGenerator.generateHashedPassword(currentUser.getMail(), password);
        Boolean isThisTargetUser = Objects.equals(currentUser, targetUser);
        Boolean isAuthenticated = currentUser.checkPassword(hashedPassword);
        
        return isThisTargetUser && isAuthenticated;
    }
    
}
