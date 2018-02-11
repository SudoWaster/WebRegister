package pl.cezaryregec.auth;

import com.google.inject.persist.Transactional;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import pl.cezaryregec.entities.Group;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Local
@Remote
@Transactional
public interface GroupService {
    
    public List<Group> getGroups();
    
    public List<Group> getOpenGroups();
}
