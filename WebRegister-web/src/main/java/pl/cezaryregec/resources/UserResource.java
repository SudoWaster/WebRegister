package pl.cezaryregec.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.servlet.RequestScoped;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
public class UserResource {
    
    private final UserService userService;
    
    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }
    
    @GET
    public Response getAll(@QueryParam("token") String tokenId) {
        // TODO: return all users for admin
        
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Integer id,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        try {
            if(!userService.isTokenValid(tokenId, userService.getFingerprint(request))) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            
            return Response.ok(userService.getUserJson(id)).build();
            
        } catch (JsonProcessingException ex) {
            return exceptionResponse(ex);
        } 
    }
    
    @POST
    public Response createUser(@FormParam("mail") String mail,
            @FormParam("password") String password,
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname) {
        
        userService.createUser(mail, password, firstname, lastname, UserType.STUDENT);
        return Response.ok().build();
    }
    
    
    private Response exceptionResponse(Exception ex) {
        Logger.getLogger(AuthResource.class.getName()).log(Level.SEVERE, null, ex);
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    
}
