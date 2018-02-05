package pl.cezaryregec.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.servlet.RequestScoped;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.cezaryregec.entities.User;
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
    
    private UserService userService;
    private ObjectMapper objectMapper;
    
    @Inject
    public AuthResource(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
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
            User user = userService.getUser(mail);
            Token token = userService.registerSession(password, user);
            result = Response.ok(objectMapper.writeValueAsString(token));
        } catch(EJBException ex) {
            result = Response.status(401);
            //result = Response.ok(ex.getCause().toString());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AuthResource.class.getName()).log(Level.SEVERE, null, ex);
            result = Response.status(500);
        }
        
        return result.build();
    }
}
