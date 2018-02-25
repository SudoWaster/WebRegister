package pl.cezaryregec.entities;

import java.io.Serializable;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class GroupAssignmentId implements Serializable {
    
    private Integer userId;
    private Integer projectId;
    
    @Override
    public int hashCode() {
        return userId.hashCode() + projectId.hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
        if(!(object instanceof GroupAssignmentId)) {
            return false;
        }
        
        GroupAssignmentId other = (GroupAssignmentId) object;
        
        return this.userId.equals(other.userId) && this.projectId.equals(other.projectId);
    }
}
