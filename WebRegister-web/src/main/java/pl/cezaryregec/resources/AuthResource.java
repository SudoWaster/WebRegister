package pl.cezaryregec.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.servlet.RequestScoped;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.cezaryregec.auth.UserService;

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
            @FormParam("password") String password) {
        
        try {
            return Response.ok(userService.getRegisteredTokenJson(password, userService.getUserJson(mail))).build();
            
        } catch(NoResultException|IOException ex) {
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
