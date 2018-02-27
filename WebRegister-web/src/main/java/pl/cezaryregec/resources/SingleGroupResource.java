package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import java.sql.Date;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.cezaryregec.groups.GroupService;
import pl.cezaryregec.auth.UserService;
import pl.cezaryregec.entities.Group;
import pl.cezaryregec.entities.GroupRole;
import pl.cezaryregec.entities.UserType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("group")
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
public class SingleGroupResource {
    
    private final UserService userService;
    private final GroupService groupService;
    
    @Inject
    public SingleGroupResource(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }
    
    @GET
    @Path("{id}")
    public Response getGroup(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        return Response.status(Response.Status.OK).entity(groupService.getGroup(id)).build();
    }
    
    @GET
    @Path("{id}/members")
    public Response getMembers(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.status(Response.Status.OK).entity(groupService.getGroup(id).getMembers(GroupRole.STUDENT)).build();
    }
    
    @PUT
    @Path("{id}/members")
    public Response addSelf(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!canManageSelfInGroup(token, id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.addToGroup(userService.getUserFromToken(token), id, true);
        return Response.status(Response.Status.OK).build();
        
    }
    
    @PUT
    @Path("{id}/members/{user_id}")
    public Response addMember(@PathParam("id") Integer id,
            @PathParam("user_id") Integer userId,
            @QueryParam("updateVacancies") Boolean updateVacancies,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!canManageSelfInGroup(token, id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.addToGroup(userService.getUser(userId), id, updateVacancies);
        return Response.status(Response.Status.OK).build();
    }
    
    @DELETE
    @Path("{id}/members")
    public Response removeSelf(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!canManageSelfInGroup(token, id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }       
        
        groupService.removeFromGroup(userService.getUserFromToken(token), id, true);
        return Response.status(Response.Status.OK).build();
    }
    
    @DELETE
    @Path("{id}/members/{user_id}")
    public Response removeMember(@PathParam("id") Integer id,
            @PathParam("user_id") Integer userId,
            @QueryParam("updateVacancies") Boolean updateVacancies,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.removeFromGroup(userService.getUser(userId), id, updateVacancies);
        return Response.status(Response.Status.OK).build();
    }
    
    
    @GET
    @Path("{id}/instructors")
    public Response getInstructors(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.status(Response.Status.OK).entity(groupService.getGroup(id).getMembers(GroupRole.PRIVILEDGED)).build();
    }
    
    @PUT
    @Path("{id}/instructors/{user_id}")
    public Response addInstructor(@PathParam("id") Integer id,
            @PathParam("user_id") Integer userId,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.setRole(userService.getUser(id), id, GroupRole.PRIVILEDGED);
        return Response.status(Response.Status.OK).build();
    }
    
    @DELETE
    @Path("{id}/instructors/{user_id}")
    public Response removeInstructor(@PathParam("id") Integer id,
            @PathParam("user_id") Integer userId,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.setRole(userService.getUser(id), id, GroupRole.STUDENT);
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("{id}/presence")
    public Response getPresence(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.ok(groupService.getGroup(id).getUserPresence(userService.getUserFromToken(token))).build();
    }
    
    @GET
    @Path("{id}/presence/all")
    public Response getAllPresence(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.ok(groupService.getGroup(id).getPresence()).build();
    }
    
    @GET
    @Path("{id}/presence/date/{date}")
    public Response getDatePresence(@PathParam("id") Integer id,
            @PathParam("date") Date date,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.ok(groupService.getGroup(id).getDatePresence(date)).build();
    }
    
    @GET
    @Path("{id}/presence/user/{user_id}")
    public Response getUserPresence(@PathParam("id") Integer id,
            @PathParam("user_id") String userId,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.ok(groupService.getGroup(id).getUserPresence(userService.getUser(userId))).build();
    }
    
    @PUT
    @Path("{id}/presence/{date}/{user_id}")
    public Response setUserPresence(@PathParam("id") Integer id,
            @PathParam("date") Date date,
            @PathParam("userId") String userId,
            @QueryParam("present") Boolean present,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUser(userId), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.setPresence(userService.getUser(userId), date, id, present);
        return Response.ok().build();
    }
    
    @DELETE
    @Path("{id}/presence/{date}/{user_id}")
    public Response removeUserPresence(@PathParam("id") Integer id,
            @PathParam("date") Date date,
            @PathParam("userId") String userId,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
     
        userService.validateToken(token, request);
        
        if(!groupService.isPriviledgedInGroup(userService.getUser(userId), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.removePresence(userService.getUser(userId), date, id);
        return Response.ok().build();
    }
    
    
    private boolean canManageSelfInGroup(String token, Integer id) {
        return (!groupService.isInGroup(userService.getUserFromToken(token), id)
                    || groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id));
    }
}
