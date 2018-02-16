package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.cezaryregec.groups.GroupService;
import pl.cezaryregec.auth.UserService;
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
        
        return Response.status(Response.Status.OK).entity(groupService.getList(id, GroupRole.STUDENT)).build();
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
}
