package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.cezaryregec.groups.GroupService;
import pl.cezaryregec.auth.UserService;

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
}
