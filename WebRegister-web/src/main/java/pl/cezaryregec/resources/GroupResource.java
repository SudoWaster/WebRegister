package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.cezaryregec.auth.GroupService;
import pl.cezaryregec.auth.UserService;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("groups")
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
public class GroupResource {
    
    private final UserService userService;
    private final GroupService groupService;
    
    @Inject
    public GroupResource(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }
    
    @GET
    public Response getAllGroups(@QueryParam("token") String token,
            @Context HttpServletRequest request) {
        
        userService.validateToken(token, request);
        
        return Response.ok(groupService.getGroups()).build();
    }
}
