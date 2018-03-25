package pl.cezaryregec.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Entity
@Table(name = "assignment")
@XmlRootElement
public class GroupAssignment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private GroupAssignmentId id;
    
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user_assignment;
    
    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group_assignment;
    
    @Basic(optional = false)
    @Column(name = "group_role")
    private Integer role;
    
    public GroupAssignment() {
        
    }
    
    public User getUser() {
        return user_assignment;
    }
    
    public void setUser(User user) {
        this.user_assignment = user;
    }
    
    public Group getGroup() {
        return group_assignment;
    }
    
    public void setGroup(Group group) {
        this.group_assignment = group;
    }
    
    public GroupRole getRole() {
        return GroupRole.cast(role);
    }
    
    public void setRole(GroupRole role) {
        this.role = role.getInt();
    }
    
    public Double getProgress() {
        if(group_assignment.getAchievementCount() > 0) {

            Double achievementCount = (double) user_assignment.getGroupAchievements(group_assignment.getId()).size();
            Double maximumAchievements = (double) group_assignment.getAchievementCount();

            return achievementCount / maximumAchievements;
        }

        return 0.0;
    }
    
    @XmlTransient
    @JsonIgnore
    public Double getAttendance() {
        Double present = 0.0;
        Double all = 0.0;
        
        for(Presence presence : group_assignment.getUserPresence(user_assignment)) {
            all++;
            
            if(presence.getPresence().equals(true)) { // do not question, getPresence might be null
                present++;
            }
        }
        
        if(all > 0) {
            return present / all;
        }
        
        return 0.0;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GroupAssignment)) {
            return false;
        }
        GroupAssignment other = (GroupAssignment) object;
        return (this.user_assignment.equals(other.user_assignment) && this.group_assignment.equals(other.group_assignment));
    }

    @Override
    public String toString() {
        return "pl.cezaryregec.entities.GroupAssignment[ user_id = " + user_assignment.getId() + ", group_id = " + group_assignment.getId() + ", role = " + GroupRole.cast(role).name() + " ]";
    }
}
