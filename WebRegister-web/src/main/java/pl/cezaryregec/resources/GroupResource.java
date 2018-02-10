package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.ejb.LocalBean;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("group")
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
public class GroupResource {
    
}
