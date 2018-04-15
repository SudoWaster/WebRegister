package pl.cezaryregec.groups;

import com.google.inject.persist.Transactional;
import java.sql.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import pl.cezaryregec.entities.Group;
import pl.cezaryregec.entities.GroupRole;
import pl.cezaryregec.entities.User;

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
    
    Integer createGroup(String name, String description, Integer vacancies);
    
    void setGroup(int id, String name, String description, Integer vacancies);
    
    void deleteGroup(int id);
    
    Group getGroup(int id);
    
    boolean isInGroup(User user, int groupId);
    
    boolean isPriviledgedInGroup(User user, int groupId);
    
    void addToGroup(User user, int groupId, boolean updateVacancies);
    
    void setRole(User user, int groupId, GroupRole role);
    
    void removeFromGroup(User user, int groupId, boolean updateVacancies);
    
    void setPresence(User user, Date date, int groupId, boolean presence);
    
    void removePresence(User user, Date date, int groupId);
}
