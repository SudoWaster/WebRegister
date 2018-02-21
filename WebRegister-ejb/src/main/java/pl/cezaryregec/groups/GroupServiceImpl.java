package pl.cezaryregec.groups;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
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
        
        entityManager.get().remove(group);
    }
    
    @Override
    @Transactional
    public Group getGroup(int id) {
        Query groupQuery = entityManager.get().createNamedQuery("Group.findById", Group.class);
        groupQuery.setParameter("id", id);
        
        return (Group) groupQuery.getSingleResult();
    }
    
    @Override
    @Transactional
    public boolean isInGroup(User user, int groupId) {
        List<GroupAssignment> assignment = getGroupAssignment(user.getId());
        
        GroupAssignment supposedAssignment = new GroupAssignment();
        supposedAssignment.setUserId(user.getId());
        supposedAssignment.setGroupId(groupId);
        
        return assignment.contains(supposedAssignment);
    }
    
    private List<GroupAssignment> getGroupAssignment(int userId) {
        Query groupAssignmentQuery = entityManager.get().createNamedQuery("GroupAssignment.findByUserId", GroupAssignment.class);
        groupAssignmentQuery.setParameter("id", userId);
        
        return (List<GroupAssignment>) groupAssignmentQuery.getResultList();
    }
    
    @Override
    @Transactional
    public boolean isPriviledgedInGroup(User user, int groupId) {
        GroupAssignment supposedAssignment = new GroupAssignment();
        supposedAssignment.setUserId(user.getId());
        supposedAssignment.setGroupId(groupId);
        
        return getList(groupId, GroupRole.PRIVILEDGED).contains(supposedAssignment) || user.getType() == UserType.ADMIN;
    }
    
    @Override
    @Transactional
    public List<GroupAssignment> getList(int groupId, GroupRole role) {
        Query groupAssignmentQuery = entityManager.get().createNamedQuery("GroupAssignment.findInGroupByRole", GroupAssignment.class);
        groupAssignmentQuery.setParameter("id", groupId);
        groupAssignmentQuery.setParameter("role", role.getInt());
        
        return (List<GroupAssignment>) groupAssignmentQuery.getResultList();
    }
    
    @Override
    @Transactional
    public void addToGroup(User user, int groupId, GroupRole role) {
        GroupAssignment assignment = new GroupAssignment();
        assignment.setUserId(groupId);
        assignment.setGroupId(groupId);
        
        if(getList(groupId, GroupRole.STUDENT).contains(assignment) || getList(groupId, GroupRole.PRIVILEDGED).contains(assignment)) {
            throw new ForbiddenException("Already in group");
        }
        
        entityManager.get().merge(assignment);
    }

    @Override
    public void setRole(User user, int groupId, GroupRole role) {
        if(!isInGroup(user, groupId)) {
            throw new NotFoundException();
        }
        
        Query assignmentQuery = entityManager.get().createNamedQuery("GroupAssignment.findUserInGroup", GroupAssignment.class);
        assignmentQuery.setParameter("uid", user.getId());
        assignmentQuery.setParameter("gid", groupId);
        
        GroupAssignment assignment = (GroupAssignment) assignmentQuery.getSingleResult();
        assignment.setRole(role);
        
        entityManager.get().merge(assignment);
    }

    @Override
    public void deleteFromGroup(User user, int groupId) {
        if(!isInGroup(user, groupId)) {
            throw new NotFoundException();
        }
        
        Query assignmentQuery = entityManager.get().createNamedQuery("GroupAssignment.findUserInGroup", GroupAssignment.class);
        assignmentQuery.setParameter("uid", user.getId());
        assignmentQuery.setParameter("gid", groupId);
        
        GroupAssignment assignment = (GroupAssignment) assignmentQuery.getSingleResult();
        
        entityManager.get().remove(assignment);
    }
}
