package pl.cezaryregec.groups;

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
    
    List<Group> getGroups();
    
    List<Group> getOpenGroups();
    
    void createGroup(String name, String description, Integer vacancies);
    
    void setGroup(int id, String name, String description, Integer vacancies);
    
    void deleteGroup(int id);
    
    Group getGroup(int id);
}
