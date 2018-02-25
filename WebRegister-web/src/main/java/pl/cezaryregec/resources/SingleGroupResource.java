package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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
        
        if(!userService.isTokenValid(token, userService.getFingerprint(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.status(Response.Status.OK).entity(groupService.getGroup(id)).build();
    }
    
    @GET
    @Path("{id}/members")
    public Response getMembers(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        if(!userService.isTokenValid(token, userService.getFingerprint(request))
                || !groupService.isInGroup(userService.getUserFromToken(token), id)) {
            
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.status(Response.Status.OK).entity(groupService.getGroup(id).getMembers()).build();
    }
    
    @PUT
    @Path("{id}/members")
    public Response addMember(@PathParam("id") Integer id,
            @FormParam("userId") Integer userId,
            @FormParam("updateVacancies") Boolean updateVacancies,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        if(!userService.isTokenValid(token, userService.getFingerprint(request))
                || !canManageSelfInGroup(token, id)) {
            
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        
        if(groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            groupService.addToGroup(userService.getUser(userId), id, updateVacancies);
            return Response.status(Response.Status.OK).build();
        }
        
        groupService.addToGroup(userService.getUserFromToken(token), id, true);
        return Response.status(Response.Status.OK).build();
        
    }
    
    @DELETE
    @Path("{id}/members")
    public Response removeMember(@PathParam("id") Integer id,
            @FormParam("userId") Integer userId,
            @FormParam("updateVacancies") Boolean updateVacancies,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
    
        if(!userService.isTokenValid(token, userService.getFingerprint(request))
                || !canManageSelfInGroup(token, id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        if(groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            groupService.deleteFromGroup(userService.getUser(userId), id, updateVacancies);
            return Response.status(Response.Status.OK).build();
        }
        
        groupService.deleteFromGroup(userService.getUserFromToken(token), id, true);
        return Response.status(Response.Status.OK).build();
    }
    
    
    @GET
    @Path("{id}/instructors")
    public Response getInstructors(@PathParam("id") Integer id,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        if(!userService.isTokenValid(token, userService.getFingerprint(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        return Response.status(Response.Status.OK).entity(groupService.getList(id, GroupRole.PRIVILEDGED)).build();
    }
    
    @PUT
    @Path("{id}/instructors")
    public Response addInstructor(@PathParam("id") Integer id,
            @FormParam("userId") Integer userId,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        if(!userService.isTokenValid(token, userService.getFingerprint(request))
                || !groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.setRole(userService.getUser(id), id, GroupRole.PRIVILEDGED);
        return Response.status(Response.Status.OK).build();
    }
    
    @DELETE
    @Path("{id}/instructors")
    public Response removeInstructor(@PathParam("id") Integer id,
            @FormParam("userId") Integer userId,
            @QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        if(!userService.isTokenValid(token, userService.getFingerprint(request))
                || !groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        groupService.setRole(userService.getUser(id), id, GroupRole.STUDENT);
        return Response.status(Response.Status.OK).build();
    }
    
    
    private boolean canManageSelfInGroup(String token, Integer id) {
        return (!groupService.isInGroup(userService.getUserFromToken(token), id)
                    || groupService.isPriviledgedInGroup(userService.getUserFromToken(token), id));
    }
}
