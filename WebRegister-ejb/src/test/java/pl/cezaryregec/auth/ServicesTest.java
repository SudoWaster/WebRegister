package pl.cezaryregec.auth;

import com.google.inject.Inject;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import pl.cezaryregec.DefaultModule;
import pl.cezaryregec.entities.Token;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RunWith(JukitoRunner.class)
@UseModules(DefaultModule.class)
public class ServicesTest {
    
    // MOCK USER
    private final Random randomGenerator = new Random();
    private final String mail = "foo" + randomGenerator.nextInt() +"@bar.com";
    private final String password = "pass" + randomGenerator.nextLong();
    private final String firstname = "F" + randomGenerator.nextInt();
    private final String lastname = "L" + randomGenerator.nextInt();
    private final UserType type = UserType.STUDENT;
    
    // MOCK REQUEST
    private final HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    
    private User mockUser;
    private User currentUser;
    private String testFingerprint;
    private Token testToken;
    private int userId;
    
    @Inject
    UserService userService;
    
    @Before
    public void userSetup() {
        System.out.println("Creating mock user");
        userService.createUser(mail, password, firstname, lastname, type);
        
        testFingerprint = userService.getFingerprint(mockRequest);
        testToken = userService.getRegisteredToken(mail, password, testFingerprint);
        
        mockUser = new User();
        mockUser.setMail(mail);
        mockUser.setFirstname(firstname);
        mockUser.setLastname(lastname);
        mockUser.setType(type);
        
        currentUser = userService.getUser(mail);
        userId = currentUser.getId();
        System.out.println("USER: " + userId);
    }
    
    @Test
    public void tokenValidationTest() {
        String tokenId = testToken.getToken();
        
        System.out.println("Testing token validation");
        Assert.assertTrue(userService.isTokenValid(tokenId, testFingerprint));
        Assert.assertTrue(userService.isTokenValid(tokenId, testFingerprint, type));
        
        Assert.assertFalse(userService.isTokenValid(tokenId, testFingerprint, UserType.ADMIN));
    }
    
    @Test
    public void tokenSessionTest() {
        String tokenId = testToken.getToken();
        
        System.out.println("Testing token session");
        User tokenUser = userService.getUserFromToken(tokenId);
        Assert.assertEquals(mockUser, tokenUser);
        
    }
    
    @Test
    public void tokenRemovalTest() {
        String tokenId = testToken.getToken();
        
        System.out.println("Deleting token");
        userService.removeToken(tokenId);
        
        System.out.println("Checking deleted token validation");
        Assert.assertFalse(userService.isTokenValid(tokenId, testFingerprint));
        
        System.out.println("Regenerating token");
        testToken = userService.getRegisteredToken(mail, password, testFingerprint);
    }
    
    @Test
    public void userTest() {
        System.out.println("Comparing user entities");
        Assert.assertEquals(mockUser, currentUser);
        
        System.out.println("Comparing user getters");
        Assert.assertEquals(userService.getUser(userId), userService.getUser(mail));
        
        System.out.println("Testing all user getter");
        Assert.assertNotNull(userService.getUsers());
    }
    
    @After
    public void userTearDown() {
        System.out.println("Deleting mock user");
        userService.deleteUser(userId);
    }
}
