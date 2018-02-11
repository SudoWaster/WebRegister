package pl.cezaryregec.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import pl.cezaryregec.entities.Group;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class GroupServiceImpl implements GroupService {
    
    @Inject
    Provider<EntityManager> entityManager;

    @Override
    @Transactional
    public List<Group> getGroups() {
        Query groupQuery = entityManager.get().createNamedQuery("Group.findAll", Group.class);
        
        return (List<Group>) groupQuery.getResultList();
    }

    @Override
    @Transactional
    public List<Group> getOpenGroups() {
        Query groupQuery = entityManager.get().createNamedQuery("Group.findOpen", Group.class);
        
        return (List<Group>) groupQuery.getResultList();
    }
    
}
