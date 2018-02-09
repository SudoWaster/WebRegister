package pl.cezaryregec.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.servlet.RequestScoped;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
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
import pl.cezaryregec.entities.User;
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
    private final ObjectMapper objectMapper;
    
    @Inject
    public UserResource(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }
    
    @GET
    public Response getCurrent(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) throws JsonProcessingException {
        
        try {
            userService.refreshToken(tokenId, userService.getFingerprint(request));
            
            return Response.ok(userService.getUserJsonFromToken(tokenId)).build();
            
        } catch (NoResultException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Integer id,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) throws JsonProcessingException {
        
        try {
            if(!userService.isTokenValid(tokenId, userService.getFingerprint(request))) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            
            return Response.ok(userService.getUserJson(id)).build();
            
        } catch (NoResultException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("all")
    public Response getAll(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) throws IOException {
        
        String fingerprint = userService.getFingerprint(request);

        if(!userService.isTokenValid(tokenId, fingerprint, UserType.ADMIN)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        return Response.ok(userService.getUsersJson()).build();
    }
    
    @POST
    public Response createUser(@FormParam("mail") String mail,
            @FormParam("password") String password,
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname) {
        
        userService.createUser(mail, password, firstname, lastname, UserType.STUDENT);
        return Response.ok().build();
    }
    
    @DELETE
    public Response deleteUser(@FormParam("id") Integer userId,
            @FormParam("password") String password, 
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        try {
            if(!userService.isTokenValid(tokenId, userService.getFingerprint(request))) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            userService.deleteUser(userId, password, tokenId);
            
            return Response.ok().build();
        
        } catch(NoResultException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
    }    
}
