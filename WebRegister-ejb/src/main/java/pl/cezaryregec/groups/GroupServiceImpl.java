package pl.cezaryregec.groups;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import pl.cezaryregec.entities.Group;
import pl.cezaryregec.entities.GroupAssignment;
import pl.cezaryregec.entities.GroupRole;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.UserType;

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
        
        for(User user : group.getMembers()) {
            group.removeMember(user);
        }
        
        entityManager.get().remove(group);
    }
    
    @Override
    @Transactional
    public Group getGroup(int id) {
        Query groupQuery = entityManager.get().createNamedQuery("Group.findById", Group.class);
        groupQuery.setParameter("id", id);
        
        Group group;
        
        try {
            group = (Group) groupQuery.getSingleResult();
        } catch(NoResultException ex) {
            throw new NotFoundException("Group does not exist");
        }
        
        return group;
    }
    
    @Override
    @Transactional
    public boolean isInGroup(User user, int groupId) {
        Group group = getGroup(groupId);
        
        return !group.getMembers().isEmpty() && group.getMembers().contains(user);
    }
    
    @Override
    @Transactional
    public boolean isPriviledgedInGroup(User user, int groupId) {
        Group group = getGroup(groupId);
        
        return user.getType() == UserType.ADMIN || group.getMembers(GroupRole.PRIVILEDGED).contains(user);
    }
    
    @Override
    @Transactional
    public void addToGroup(User user, int groupId, boolean updateVacancies) {
        Group group = getGroup(groupId);
        
        if(isInGroup(user, groupId)) {
            throw new ForbiddenException("Already in group");
        }
        
        GroupAssignment assignment = new GroupAssignment();
        assignment.setUser(user);
        assignment.setGroup(group);
        assignment.setRole(GroupRole.STUDENT);
        
        if(updateVacancies) {
            group.setVacancies(group.getVacancies() - 1);
        }
        
        group.addMember(user, GroupRole.STUDENT);
        
        entityManager.get().merge(group);
        entityManager.get().merge(user);
    }

    @Override
    @Transactional
    public void setRole(User user, int groupId, GroupRole role) {
        if(!isInGroup(user, groupId)) {
            throw new NotFoundException("Not in group");
        }
        
        Query assignmentQuery = entityManager.get().createNamedQuery("GroupAssignment.findUserInGroup", GroupAssignment.class);
        assignmentQuery.setParameter("uid", user.getId());
        assignmentQuery.setParameter("gid", groupId);
        
        GroupAssignment assignment = (GroupAssignment) assignmentQuery.getSingleResult();
        assignment.setRole(role);
        
        entityManager.get().merge(assignment);
    }

    @Override
    @Transactional
    public void deleteFromGroup(User user, int groupId, boolean updateVacancies) {
        if(!isInGroup(user, groupId)) {
            throw new NotFoundException("Not in group");
        }
        
        Group group = getGroup(groupId);
        
        group.removeMember(user);
        
        if(updateVacancies){
            group.setVacancies(group.getVacancies() + 1);
        }
        
        entityManager.get().merge(group);
        entityManager.get().merge(user);
    }
}
