package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.persistence.NoResultException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.cezaryregec.auth.entities.User;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.auth.entities.Token;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
public class AuthResource {
    
    @EJB
    private UserService userService;
    
    @GET
    public Response heartbeat() {
        return Response.ok("{ \"works\": " + (userService != null) + " }").build();
    }
    
    @POST
    public Response getUser(@FormParam("mail") String mail, 
            @FormParam("password") String password) {
        
        
        ResponseBuilder result = Response.noContent();
        
        try {
            User user = userService.getUser(mail);
            Token token = userService.registerSession(password, user);
            result = Response.ok("{ \"token\" : \"" + token.getToken() + "\"}");
        } catch(EJBException ex) {
            result = Response.status(401);
            //result = Response.ok(ex.getCause().toString());
        }
        
        return result.build();
    }
}
