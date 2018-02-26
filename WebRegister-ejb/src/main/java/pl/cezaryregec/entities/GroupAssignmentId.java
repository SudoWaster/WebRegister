package pl.cezaryregec.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Embeddable
public class GroupAssignmentId implements Serializable {
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "group_id")
    private Integer groupId;
    
    @Override
    public int hashCode() {
        return userId.hashCode() + groupId.hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
        if(!(object instanceof GroupAssignmentId)) {
            return false;
        }
        
        GroupAssignmentId other = (GroupAssignmentId) object;
        
        return this.userId.equals(other.userId) && this.groupId.equals(other.groupId);
    }
}
