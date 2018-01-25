package pl.cezaryregec.auth;

import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@WebService(serviceName = "AuthService")
@Path("auth")
public class AuthService {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUser(@FormParam("mail") String mail, 
            @FormParam("password") String password) {
        EntityManagerFactory emfactory;
        emfactory = Persistence.createEntityManagerFactory( "pl.cezaryregec_WebRegister-web_war_1.0" );
        
        EntityManager entitymanager = emfactory.createEntityManager();
        Query q = entitymanager.createNamedQuery("User.findByMail");
        q.setParameter("mail", mail);
        
        List<User> result = q.getResultList();
        
        if(result.size() > 0 && result.get(0).checkPassword(password)) {
            
            entitymanager.getTransaction().begin();
            
            Token token = new Token();
            token.setExpiration(0);
            token.setUser(1);
            
            entitymanager.persist(token);
            entitymanager.getTransaction().commit();
            
            entitymanager.close();
            emfactory.close();
            
            return Response.ok(token.getToken()).build();
        }
        
        entitymanager.close();
        emfactory.close();
        
        return Response.status(401).build();
    }
}
