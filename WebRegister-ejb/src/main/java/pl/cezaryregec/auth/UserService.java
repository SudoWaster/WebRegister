package pl.cezaryregec.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import javax.ejb.Local;
import javax.ejb.Remote;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.Token;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Local
@Remote
@Transactional
public interface UserService {
    
    public String getUserJson(String mail) throws JsonProcessingException;
    public String getRegisteredTokenJson(String password, String userJson) throws IOException, JsonProcessingException;
}
