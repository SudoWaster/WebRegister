package pl.cezaryregec.entities;

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
