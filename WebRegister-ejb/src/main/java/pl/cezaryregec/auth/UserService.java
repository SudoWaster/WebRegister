package pl.cezaryregec.auth;

import com.google.inject.persist.Transactional;
import javax.ejb.Local;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.Token;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Local
//@Remote
@Transactional
public interface UserService {
    
    public User getUser(String mail);
    public Token registerSession(String password, User user);
}
