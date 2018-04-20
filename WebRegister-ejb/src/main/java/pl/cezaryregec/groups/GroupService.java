package pl.cezaryregec.groups;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.sql.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import pl.cezaryregec.entities.Group;
import pl.cezaryregec.entities.GroupAssignment;
import pl.cezaryregec.entities.GroupRole;
import pl.cezaryregec.entities.Presence;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Transactional
public class GroupService {
    
    @Inject
    private Provider<EntityManager> entityManager;

    public List<Group> getGroups() {
        Query groupQuery = entityManager.get().createNamedQuery("Group.findAll", Group.class);
        
        return (List<Group>) groupQuery.getResultList();
    }

    public List<Group> getOpenGroups() {
        Query groupQuery = entityManager.get().createNamedQuery("Group.findOpen", Group.class);
        
        return (List<Group>) groupQuery.getResultList();
    }
    
    public Integer createGroup(String name, String description, Integer vacancies) {
        Group group = new Group();
        
        group.setName(name);
        group.setDescription(description);
        group.setVacancies(vacancies);
        
        entityManager.get().merge(group);
        
        for(Group g : getGroups()) {
            if(g.equals(group)) {
                return g.getId();
            }
        }
        
        return null;
    }
    
    public void setGroup(int id, String name, String description, Integer vacancies) {
        Group group = getGroup(id);
        
        group.setName(name);
        group.setDescription(description);
        group.setVacancies(vacancies);
        
        entityManager.get().merge(group);
    }
    
    public void deleteGroup(int id) {
        Group group = getGroup(id);
        
        for(User user : group.getMembers()) {
            group.removeMember(user);
        }
        
        entityManager.get().remove(group);
    }
    
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
    
    public boolean isInGroup(User user, int groupId) {
        Group group = getGroup(groupId);
        
        return !group.getMembers().isEmpty() && group.getMembers().contains(user);
    }
    
    public boolean isPriviledgedInGroup(User user, int groupId) {
        Group group = getGroup(groupId);
        
        return user.getType() == UserType.ADMIN || group.getMembers(GroupRole.PRIVILEDGED).contains(user);
    }
    
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

    public void setRole(User user, int groupId, GroupRole role) {
        if(!isInGroup(user, groupId)) {
            throw new NotFoundException("Not in group");
        }
        
        Group group = getGroup(groupId);
        List<GroupAssignment> groupAssignment = group.getGroupAssignment();
        
        for(GroupAssignment assignment : groupAssignment) {
            if(assignment.getUser().equals(user)) {
                assignment.setRole(role);
                entityManager.get().merge(assignment);
            }
        }
        
        entityManager.get().merge(group);
    }

    public void removeFromGroup(User user, int groupId, boolean updateVacancies) {
        if(!isInGroup(user, groupId)) {
            throw new NotFoundException("Not in group");
        }
        
        Group group = getGroup(groupId);
        
        for(Presence userPresence : group.getUserPresence(user)) {
            entityManager.get().remove(userPresence);
        }
        
        entityManager.get().remove(group.removeMember(user));
        
        if(updateVacancies){
            group.setVacancies(group.getVacancies() + 1);
        }
        
        entityManager.get().merge(group);
        entityManager.get().merge(user);
    }
    
    public void setPresence(User user, Date date, int groupId, boolean presence) {
        Group group = getGroup(groupId);
        Presence presenceEntity = group.setPresence(user, date, presence);
        
        entityManager.get().merge(presenceEntity);
        entityManager.get().merge(group);
    }
    
    public void removePresence(User user, Date date, int groupId) {
        Group group = getGroup(groupId);
        Presence presence = group.removePresence(user, date);
        
        entityManager.get().remove(presence);
        entityManager.get().merge(group);
    }
}
