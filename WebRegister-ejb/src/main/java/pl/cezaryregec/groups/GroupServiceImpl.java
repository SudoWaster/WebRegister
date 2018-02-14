package pl.cezaryregec.groups;

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
    
    @Override
    @Transactional
    public void createGroup(String name, String description, Integer vacancies) {
        Group group = new Group();
        
        group.setName(name);
        group.setDescription(description);
        group.setVacancies(vacancies);
        
        entityManager.get().merge(group);
    }
    
    @Override
    @Transactional
    public void setGroup(int id, String name, String description, Integer vacancies) {
        Group group = getGroup(id);
        
        group.setName(name);
        group.setDescription(description);
        group.setVacancies(vacancies);
        
        entityManager.get().merge(group);
    }
    
    @Override
    @Transactional
    public void deleteGroup(int id) {
        Group group = getGroup(id);
        
        entityManager.get().remove(group);
    }
    
    @Override
    @Transactional
    public Group getGroup(int id) {
        Query groupQuery = entityManager.get().createNamedQuery("Group.findById", Group.class);
        groupQuery.setParameter("id", id);
        
        return (Group) groupQuery.getSingleResult();
    }
}
