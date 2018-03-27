package pl.cezaryregec.groups;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;
import pl.cezaryregec.entities.Achievement;
import pl.cezaryregec.entities.Group;
import pl.cezaryregec.entities.User;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class AchievementServiceImpl implements AchievementService {
    
    @Inject
    private Provider<EntityManager> entityManager;
    
    @Inject
    private GroupService groupService;

    @Override
    public void addAchievement(String name, String description, Integer groupId) {
        Group group = groupService.getGroup(groupId);
        Achievement achievement = group.addAchievement(name, description);
        
        entityManager.get().merge(achievement);
        entityManager.get().merge(group);
    }

    @Override
    public void setAchievement(Integer id, Integer groupId, String name, String description) {
        Achievement achievement = groupService.getGroup(groupId).getAchievement(id);
        
        achievement.setName(name);
        achievement.setDescription(description);
        
        entityManager.get().merge(achievement);
    }
    
    private Achievement getAchievement(Integer id) {
        Query achievementQuery = entityManager.get().createNamedQuery("Achievement.findById", Achievement.class);
        achievementQuery.setParameter("id", id);
        
        try {
            return (Achievement) achievementQuery.getSingleResult();
        } catch(NoResultException ex) {
            throw new NotFoundException("No achievement found");
        }
    }

    @Override
    public void removeAchievement(Integer id, Integer groupId) {
        Group group = groupService.getGroup(groupId);
        Achievement achievement = group.removeAchievement(id);
        
        entityManager.get().remove(achievement);
    }
    
    @Override
    public boolean isInGroup(Integer id, Integer groupId) {
        return getAchievement(id).getGroup().getId().equals(groupId);
    }

    @Override
    public void giveAchievement(Integer id, User user) {
        Achievement achievement = getAchievement(id);
        
        if(!groupService.isInGroup(user, achievement.getGroup().getId())) {
            throw new NotFoundException("User not in group");
        }
        
        user.giveAchievement(achievement);
        
        entityManager.get().merge(user);
        entityManager.get().merge(achievement);
    }

    @Override
    public void denyAchievement(Integer id, User user) {
        Achievement achievement = getAchievement(id);
        
        if(!groupService.isInGroup(user, achievement.getGroup().getId())) {
            throw new NotFoundException("User not in group");
        }
        
        user.denyAchievement(achievement);
        
        entityManager.get().merge(user);
        entityManager.get().merge(achievement);
    }
    
}
