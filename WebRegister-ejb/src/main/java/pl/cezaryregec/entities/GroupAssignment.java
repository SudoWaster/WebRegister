package pl.cezaryregec.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Entity
@Table(name = "assignment")
@XmlRootElement
@IdClass(GroupAssignmentId.class)
public class GroupAssignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "user_id")
    private Integer userId;
    
    @Id
    @Column(name = "group_id")
    private Integer groupId;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    private User user_assignment;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "group_id", referencedColumnName = "id")
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
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        hash += (groupId != null ? groupId.hashCode() : 0);
        
        return hash;
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
