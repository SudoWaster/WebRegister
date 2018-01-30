package pl.cezaryregec.auth;

import javax.ejb.Local;
import pl.cezaryregec.auth.entities.User;
import pl.cezaryregec.auth.entities.Token;
import javax.ejb.LocalBean;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.Transactional;

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
