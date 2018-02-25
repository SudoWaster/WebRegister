package pl.cezaryregec.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Entity
@Table(name = "tokens")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Token.findById", query = "SELECT t FROM Token t WHERE t.token = :id"),
    @NamedQuery(name = "Token.findSessions", query = "SELECT t FROM Token t WHERE t.user.id = :id")
})
@JsonIgnoreProperties({"user", "fingerprint"})
public class Token implements Serializable {
  
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @NotNull
    @Column(name = "expiration")
    private Timestamp expiration;
    @Basic(optional = false)
    @Column(name = "fingerprint")
    private String fingerprint;
    
    @OneToOne(cascade = { 
        CascadeType.PERSIST, 
        CascadeType.MERGE
    })
    @JoinColumn(name = "user_id")
    private User user;
    
    public Token() {
        this.token = "null" + new Date().getTime();
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return this.token;
    }
    
    @XmlTransient
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setExpiration(long time) {
        this.expiration = new Timestamp(System.currentTimeMillis() + time);
    }
    
    public boolean hasExpired() {
        return this.expiration.before(new Timestamp(System.currentTimeMillis()));
    }
    
    public boolean isValid(String fingerprint) {
        return !hasExpired() && this.fingerprint.equals(fingerprint);
    }
    
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
    
    @XmlTransient
    public String getFingerprint() {
        return this.fingerprint;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (token != null ? token.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Token)) {
            return false;
        }
        Token other = (Token) object;
        return !((this.token == null && other.token != null) || (this.token != null && !this.token.equals(other.token)));
    }

    @Override
    public String toString() {
        return "pl.cezaryregec.entities.Token[ token=" + token + ", expiration = " + expiration + ", user = " + user.getId() + " ]";
    }
    
}
