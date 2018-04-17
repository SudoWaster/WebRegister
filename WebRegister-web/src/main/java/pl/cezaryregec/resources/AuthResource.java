package pl.cezaryregec.resources;

import com.google.inject.servlet.RequestScoped;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.cezaryregec.auth.TokenService;
import pl.cezaryregec.entities.Token;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RequestScoped
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
public class AuthResource {
    
    private final TokenService tokenService;
    
    @Inject
    public AuthResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    
    @POST
    public Response getUser(@FormParam("mail") String mail, 
            @FormParam("password") String password,
            @Context HttpServletRequest request) {
        
        Token token = tokenService.getRegisteredToken(mail, password, tokenService.getFingerprint(request));
        return Response.ok(token).build();
    }
    
    @DELETE
    public Response deleteToken(@QueryParam("token") String token) {
        
        tokenService.removeToken(token);
        return Response.ok().build();
    }
}
