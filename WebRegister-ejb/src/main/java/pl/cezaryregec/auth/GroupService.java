package pl.cezaryregec.auth;

import com.google.inject.persist.Transactional;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Local
@Remote
@Transactional
public interface GroupService {
    
}
