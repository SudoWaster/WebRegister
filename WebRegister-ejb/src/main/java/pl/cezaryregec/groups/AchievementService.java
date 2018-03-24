package pl.cezaryregec.groups;

import pl.cezaryregec.entities.User;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public interface AchievementService {
    
    void addAchievement(String name, String description, Integer groupId);
    
    void setAchievement(Integer id, Integer groupId, String name, String description);
    
    void removeAchievement(Integer id, Integer groupId);
    
    void giveAchievement(Integer id, User user);
    
    void denyAchievement(Integer id, User user);
}
