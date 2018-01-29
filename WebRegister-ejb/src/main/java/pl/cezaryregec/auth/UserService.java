package pl.cezaryregec.auth;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.transaction.Transactional;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Local
@Remote
public interface UserService {
    
    @Transactional
    public User getUser(String mail);
    
    @Transactional
    public Token registerSession(String password, User user);
}
