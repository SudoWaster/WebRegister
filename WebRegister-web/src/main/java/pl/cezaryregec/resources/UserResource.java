package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import java.io.IOException;
import javax.ejb.LocalBean;
import javax.inject.Inject;
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
import pl.cezaryregec.auth.TokenService;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.entities.Token;
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
    
    private final TokenService tokenService;
    private final UserService userService;
    
    @Inject
    public UserResource(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }
    
    @GET
    public Response getCurrent(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        return Response.ok(tokenService.getToken(tokenId).getUser()).build();
    }
    
    @GET
    @Path("groups")
    public Response getGroupsOfCurrent(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        return Response.ok(tokenService.getToken(tokenId).getUser().getGroups()).build();
    }
    
    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Integer id,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        return Response.ok(userService.getUser(id)).build();
    }
    
    @GET
    @Path("{id}/groups")
    public Response getGroupsOfUser(@PathParam("id") Integer id,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
     
        tokenService.validateToken(tokenId, request);
        return Response.ok(userService.getUser(id).getGroupAssignment()).build();
    }
    
    @GET
    @Path("{id}/groups/{gid}/achievements")
    public Response getGroupAchievements(@PathParam("id") Integer id,
            @PathParam("gid") Integer groupId,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        
        return Response.ok(userService.getUser(id).getGroupAchievements(groupId)).build();
    }
    
    @GET
    @Path("{id}/groups/{gid}/achievements/progress")
    public Response getGroupProgress(@PathParam("id") Integer id,
            @PathParam("gid") Integer groupId,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        
        return Response.ok(userService.getUser(id).getProgressInGroup(groupId)).build();
    }
    
    @GET
    @Path("all")
    public Response getAll(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) throws IOException {
        
        tokenService.validateToken(tokenId, request);

        if(!tokenService.getToken(tokenId).getUser().hasPriviledge(UserType.ADMIN)) {
            return Response.status(Response.Status.FORBIDDEN).build();
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
    public Response setSelf(@FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        
        Integer id = tokenService.getToken(tokenId).getUser().getId();
        
        userService.setUser(id, firstname, lastname);
        Token token = tokenService.getToken(tokenId);
        token.setUser(userService.getUser(id));
        tokenService.refreshToken(token);
        
        return Response.ok().build();
    }
    
    @PUT
    @Path("{id}")
    public Response setUser(@PathParam("id") Integer id,
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        
        if(!tokenService.getToken(tokenId).getUser().getId().equals(id)
                && !tokenService.getToken(tokenId).getUser().hasPriviledge(UserType.ADMIN)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        userService.setUser(id, firstname, lastname);
        Token token = tokenService.getToken(tokenId);
        token.setUser(userService.getUser(id));
        tokenService.refreshToken(token);
        
        return Response.ok().build();
    }
    
    @PUT
    @Path("{id}/priviledge")
    public Response setUserPriviledge(@PathParam("id") Integer id,
            @FormParam("type") Integer type,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        
        if(!tokenService.getToken(tokenId).getUser().hasPriviledge(UserType.ADMIN)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        userService.setUserType(id, UserType.cast(type));
        Token token = tokenService.getToken(tokenId);
        token.setUser(userService.getUser(id));
        tokenService.refreshToken(token);
        
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
        
        tokenService.validateToken(tokenId, request);
        userService.setUserCredentials(id, oldPasssword, mail, password, tokenId);
        tokenService.getToken(tokenId).setUser(userService.getUser(id));
        
        return Response.ok().build();
    }
    
    @DELETE
    public Response deleteSelf(@QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);
        userService.deleteUser(tokenService.getToken(tokenId).getUser().getId());
        
        return Response.ok().build();
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Integer userId,
            @QueryParam("token") String tokenId,
            @Context HttpServletRequest request) {
        
        tokenService.validateToken(tokenId, request);

        if(!tokenService.getToken(tokenId).getUser().hasPriviledge(UserType.ADMIN)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        userService.deleteUser(userId);
        
        return Response.ok().build();
    }    
}
