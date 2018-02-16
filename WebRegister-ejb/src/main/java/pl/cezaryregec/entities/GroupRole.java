package pl.cezaryregec.entities;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public enum GroupRole {
    UNAUTHORIZED(0), STUDENT(1), PRIVILEDGED(2);
    
    private final int value;
    private GroupRole(int value) {
        this.value = value;
    }
    
    public int getInt() {
        return value;
    }
    
    public static GroupRole cast(int i) {
        for (GroupRole role : GroupRole.values()) {
            if (role.getInt() == i) {
                return role;
            }
        }
        
        return null;
    }
}
