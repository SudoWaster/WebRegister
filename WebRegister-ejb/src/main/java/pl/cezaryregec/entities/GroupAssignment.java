package pl.cezaryregec.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Entity
@Table(name = "group_assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupAssignment.findAll", query = "SELECT g FROM GroupAssignment g"),
    @NamedQuery(name = "GroupAssignment.findByUserId", query = "SELECT g FROM GroupAssignment g WHERE g.userId = :id"),
    @NamedQuery(name = "GroupAssignment.findByGroupId", query = "SELECT g FROM GroupAssignment g WHERE g.groupId = :id"),
    @NamedQuery(name = "GroupAssignment.findInGroupByRole", query = "SELECT g FROM GroupAssignment g WHERE g.groupId = :id AND g.role = :role"),
    @NamedQuery(name = "GroupAssignment.findUserInGroup", query = "SELECT g FROM GroupAssignment g WHERE g.userId = :uid AND g.groupId = :gid")
})
public class GroupAssignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @Column(name = "group_id")
    private Integer groupId;
    @Basic(optional = false)
    @Column(name = "group_role")
    private Integer role;
    
    public GroupAssignment() {
        
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GroupAssignment)) {
            return false;
        }
        GroupAssignment other = (GroupAssignment) object;
        return this.id.equals(other.id) || (this.userId.equals(other.userId) && this.groupId.equals(other.groupId));
    }

    @Override
    public String toString() {
        return "pl.cezaryregec.entities.GroupAssignment[ id=" + id + ", user_id = " + userId + ", group_id = " + groupId + ", role = " + GroupRole.cast(role).name() + " ]";
    }
}
