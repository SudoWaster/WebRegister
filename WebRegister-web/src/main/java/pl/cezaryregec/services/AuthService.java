package pl.cezaryregec.services;

import javax.persistence.NoResultException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.cezaryregec.auth.Token;
import pl.cezaryregec.auth.User;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthService {

    @GET
    public Response heartbeat() {
        return Response.ok("{ \"works\": true }").build();
    }
    
    @POST
    public Response getUser(@FormParam("mail") String mail, 
            @FormParam("password") String password) {
        
        // TODO: EJB injection, splitting business logic
        
        ResponseBuilder result = Response.noContent();
        
        EntityManagerFactory emfactory;
        emfactory = Persistence.createEntityManagerFactory( "pl.cezaryregec_WebRegister-web_war_1.0" );
        
        EntityManager entitymanager = emfactory.createEntityManager();
        Query q = entitymanager.createNamedQuery("User.findByMail");
        q.setParameter("mail", mail);
        
        
        try {
            User user = (User) q.getSingleResult();

            if(user.checkPassword(password)) {

                entitymanager.getTransaction().begin();

                Token token = new Token();
                token.setExpiration(0);
                token.setUser(user.getId());

                entitymanager.persist(token);
                entitymanager.getTransaction().commit();

                result = Response.ok("{ \"token\" : \"" + token.getToken() + "\"}");
            }

        } catch(NoResultException ex) {
            result = Response.status(401);
        }

        entitymanager.close();
        emfactory.close();
        
        return result.build();
    }
}
