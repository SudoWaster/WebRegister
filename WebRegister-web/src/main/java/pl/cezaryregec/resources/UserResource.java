package pl.cezaryregec.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.servlet.RequestScoped;
import java.io.IOException;
import java.util.List;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.entities.Group;
import pl.cezaryregec.entities.User;
import pl.cezaryregec.entities.UserType;
import pl.cezaryregec.groups.GroupService;

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
    public Response getCurrent(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        userService.validateToken(tokenId, request);
        return Response.ok(userService.getUserFromToken(tokenId)).build();
    }
    
    @GET
    @Path("groups")
    public Response getGroupsOfCurrent(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        userService.validateToken(tokenId, request);
        return Response.ok(userService.getUserFromToken(tokenId).getGroups()).build();
    }
    
    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Integer id,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        userService.validateToken(tokenId, request);
        return Response.ok(userService.getUser(id)).build();
    }
    
    @GET
    @Path("{id}/groups")
    public Response getGroupsOfUser(@PathParam("id") Integer id,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
     
        userService.validateToken(tokenId, request);
        return Response.ok(userService.getUser(id).getGroups()).build();
    }
    
    @GET
    @Path("all")
    public Response getAll(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) throws IOException {
        
        String fingerprint = userService.getFingerprint(request);

        if(!userService.isTokenValid(tokenId, fingerprint, UserType.ADMIN)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        return Response.ok(userService.getUsers()).build();
    }
    
    @POST
    public Response createUser(@FormParam("mail") String mail,
            @FormParam("password") String password,
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname) {
        
        userService.createUser(mail, password, firstname, lastname, UserType.STUDENT);
        return Response.status(Response.Status.CREATED).build();
    }
    
    @PUT
    public Response setUser(@FormParam("id") Integer id,
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname,
            @FormParam("password") String password,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        userService.validateToken(tokenId, request);
        
        if(!(userService.getUserFromToken(tokenId).getId().equals(id)
                && userService.getUserFromToken(tokenId).getType().equals(UserType.ADMIN))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        userService.setUser(id, firstname, lastname);
        userService.getToken(tokenId).setUser(userService.getUser(id));
        
        return Response.ok().build();
    }
    
    @PUT
    @Path("auth")
    public Response setCredentials(@FormParam("id") Integer id,
            @FormParam("oldpassword") String oldPasssword,
            @FormParam("mail") String mail,
            @FormParam("password") String password,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        userService.validateToken(tokenId, request);
        userService.setUserCredentials(id, oldPasssword, mail, password, tokenId);
        userService.getToken(tokenId).setUser(userService.getUser(id));
        
        return Response.ok().build();
    }
    
    @DELETE
    public Response deleteSelf(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        userService.validateToken(tokenId, request);
        userService.deleteUser(userService.getUserFromToken(tokenId).getId());
        
        return Response.ok().build();
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Integer userId,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        userService.validateToken(tokenId, request);

        if(!userService.isTokenValid(tokenId, userService.getFingerprint(request), UserType.ADMIN)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        userService.deleteUser(userId);
        
        return Response.ok().build();
    }    
}
