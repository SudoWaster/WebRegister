package pl.cezaryregec.auth;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-26T09:24:42")
@StaticMetamodel(Token.class)
public class Token_ { 

    public static volatile SingularAttribute<Token, Timestamp> expiration;
    public static volatile SingularAttribute<Token, Integer> user;
    public static volatile SingularAttribute<Token, String> token;

}