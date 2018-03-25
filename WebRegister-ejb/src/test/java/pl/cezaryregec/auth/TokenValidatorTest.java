package pl.cezaryregec.auth;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class TokenValidatorTest {
    
    Token token = new Token();
    TokenValidator validator = new TokenValidator(token);
    
    @Test
    public void shouldCheckTokenExpiration() {
        token.setExpiration(Integer.MAX_VALUE);
       
        Boolean expectedResult = false;
        Boolean actualResult = validator.hasTokenExpired();
        
        Assert.assertThat(actualResult, Is.is(IsEqual.equalTo(expectedResult)));
        
        token.setExpiration(Integer.MIN_VALUE);
        expectedResult = true;
        actualResult = validator.hasTokenExpired();
        
        Assert.assertThat(actualResult, Is.is(IsEqual.equalTo(expectedResult)));
    }
    
    @Test
    public void shouldValidateToken() {
        String fingerprint = "fingerprint";
        
        token.setUser(new User(0, "", "", UserType.STUDENT));
        token.setFingerprint(fingerprint);
        token.setExpiration(Integer.MAX_VALUE);
        
        Boolean expectedResult = true;
        Boolean actualResult = validator.isTokenValid(fingerprint);
        
        Assert.assertThat(actualResult, Is.is(IsEqual.equalTo(expectedResult)));
        
        expectedResult = false;
        actualResult = validator.isTokenValid("not" + fingerprint);
        
        Assert.assertThat(actualResult, Is.is(IsEqual.equalTo(expectedResult)));
        
        token.getUser().setType(UserType.UNAUTHORIZED);
        expectedResult = false;
        actualResult = validator.isTokenValid(fingerprint);
        
        Assert.assertThat(actualResult, Is.is(IsEqual.equalTo(expectedResult)));
    }
}
