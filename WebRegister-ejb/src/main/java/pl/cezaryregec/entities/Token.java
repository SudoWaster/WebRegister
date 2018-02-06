package pl.cezaryregec.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Entity
@Table(name = "tokens")
@XmlRootElement
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
    @NotNull
    @Column(name = "user_id")
    private Integer user;
    @Basic(optional = false)
    @Column(name = "fingerprint")
    private String fingerprint;
    
    public Token() {
        // TODO: hash-based tokens with client app info to prevent fixation
        this.token = "test" + new Date().getTime();
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setExpiration(long time) {
        this.expiration = new Timestamp(System.currentTimeMillis() + time);
    }
    
    public boolean hasExpired() {
        return this.expiration.after(new Timestamp(System.currentTimeMillis()));
    }
    
    public boolean isValid(String fingerprint) {
        return hasExpired() && this.fingerprint.equals(fingerprint);
    }
    
    public void setUser(Integer user_id) {
        this.user = user_id;
    }
    
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (token != null ? token.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Token)) {
            return false;
        }
        Token other = (Token) object;
        if ((this.token == null && other.token != null) || (this.token != null && !this.token.equals(other.token))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.cezaryregec.Token[ token=" + token + ", expiration = " + expiration + ", user = " + user + " ]";
    }
    
}
