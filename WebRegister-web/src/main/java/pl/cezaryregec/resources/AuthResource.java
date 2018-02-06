package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.ejb.LocalBean;
import javax.inject.Inject;
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
    
    @GET
    public Response heartbeat() {
        return Response.ok("{ \"works\": " + (userService != null) + " }").build();
    }
    
    @POST
    public Response getUser(@FormParam("mail") String mail, 
            @FormParam("password") String password) {
        
        
        ResponseBuilder result = Response.noContent();
        
        try {
            result = Response.ok(userService.getRegisteredTokenJson(password, userService.getUserJson(mail)));
        } catch(Exception ex) {
            result = Response.status(401);
            //result = Response.ok(ex.getMessage());
        }
        
        return result.build();
    }
    
    @DELETE
    public Response deleteToken(@PathParam("token") String token) {
        
        userService.removeToken(token);
        
        return Response.ok().build();
    }
}
