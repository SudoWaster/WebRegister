package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.cezaryregec.auth.Token;
import pl.cezaryregec.auth.User;
import pl.cezaryregec.auth.UserService;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    private UserService us;
    
    @Inject
    public AuthResource(UserService us) {
        this.us = us;
    }

    @GET
    public Response heartbeat() {
        return Response.ok("{ \"works\": " + (us != null) + " }").build();
    }
    
    @POST
    public Response getUser(@FormParam("mail") String mail, 
            @FormParam("password") String password) {
        
        
        ResponseBuilder result = Response.noContent();
        
        try {
            Token token = us.registerSession(password, us.getUser(mail));
            result = Response.ok("{ \"token\" : \"" + token.getToken() + "\"}");
        } catch(NoResultException ex) {
            result = Response.status(401);
        }
        
        return result.build();
    }
}
