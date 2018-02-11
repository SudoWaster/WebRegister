package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.entities.Token;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
public class AuthResource {
    
    private final UserService userService;
    
    @Inject
    public AuthResource(UserService userService) {
        this.userService = userService;
    }
    
    @POST
    public Response getUser(@FormParam("mail") String mail, 
            @FormParam("password") String password,
            @Context HttpServletRequest request) {
        
        try {
            Token token = userService.getRegisteredToken(mail, password, userService.getFingerprint(request));
            
            return Response.ok(token).build();
            
        } catch(NoResultException ex) {
            
            return exceptionResponse(ex);
            
        }
    }
    
    @DELETE
    public Response deleteToken(@PathParam("token") String token) {
        
        userService.removeToken(token);
        
        return Response.ok().build();
    }
    
    private Response exceptionResponse(Exception ex) {
        Logger.getLogger(AuthResource.class.getName()).log(Level.SEVERE, null, ex);
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
