package pl.cezaryregec.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Entity
@Table(name = "presence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Presence.findAll", query = "SELECT p FROM Presence p"),
    @NamedQuery(name = "Presence.findGroup", query = "SELECT p FROM Presence p WHERE p.group_id = :id"),
    @NamedQuery(name = "Presence.findInGroupByDate", query = "SELECT p FROM Presence p WHERE p.group_id = :id AND p.date = :date"),
    @NamedQuery(name = "Presence.findByUser", query = "SELECT p FROM Presence p WHERE p.user_id = :id"),
    @NamedQuery(name = "Presence.findByUserInGroup", query = "SELECT p FROM Presence p WHERE p.user_id = :uid AND p.group_id = :gid"),
    @NamedQuery(name = "Presence.findByUserInGroupByDate", query = "SELECT p FROM Presence p WHERE p.user_id = :uid AND p.group_id = :gid AND p.date = :date")
})
public class Presence implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @Column(name = "group_id")
    private Integer groupId;
    @Basic(optional = false)
    @Column(name = "date")
    private Date date;
    @Basic(optional = false)
    @Column(name = "presence")
    private Boolean presence;
    
    public Presence() {
        
    }
    
    public Integer getId() {
        return id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Boolean getPresence() {
        return presence;
    }
    
    public void setPresence(Boolean presence) {
        this.presence = presence;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Presence)) {
            return false;
        }
        Presence other = (Presence) object;
        return this.id.equals(other.id) || (this.userId.equals(other.userId) && this.groupId.equals(other.groupId) && this.date.equals(other.date));
    }

    @Override
    public String toString() {
        return "pl.cezaryregec.entities.Presence[ id=" + id + ", user_id = " + userId + ", group_id = " + groupId + ", date = " + date + ", presence = " + presence + " ]";
    }
}
