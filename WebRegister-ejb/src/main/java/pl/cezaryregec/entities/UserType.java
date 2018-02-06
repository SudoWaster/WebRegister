package pl.cezaryregec.entities;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public enum UserType {
    UNAUTHORIZED(0), STUDENT(1), PRIVILEDGED(2), ADMIN(3);
    
    private final int value;
    private UserType(int value) {
        this.value = value;
    }
    
    public int getInt() {
        return value;
    }
    
    public static UserType cast(int i) {
        for (UserType userType : UserType.values()) {
            if (userType.getInt() == i) {
                return userType;
            }
        }
        
        return null;
    }
}
