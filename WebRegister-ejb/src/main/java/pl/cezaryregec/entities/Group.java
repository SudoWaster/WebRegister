package pl.cezaryregec.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@Entity
@Table(name = "groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g"),
    @NamedQuery(name = "Group.findById", query = "SELECT g FROM Group g WHERE g.id = :id"),
    @NamedQuery(name = "Group.findOpen", query = "SELECT g FROM Group g WHERE g.vacancies > 0")
})
@JsonIgnoreProperties({"members", "presence"})
public class Group implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "group_id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "group_name")
    private String name;
    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "group_desc")
    private String description;
    @Basic(optional = false)
    @Column(name = "group_vacancies")
    private Integer vacancies;
    
    @ManyToMany(cascade = { 
        CascadeType.PERSIST, 
        CascadeType.MERGE
    })
    @JoinTable(
        name = "group_assignment",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> members = new ArrayList();
    
    @OneToMany(
        cascade = { 
            CascadeType.PERSIST, 
            CascadeType.MERGE
        },
        orphanRemoval = true
    )
    @JoinColumn(name = "group_id")
    private List<Presence> presence = new ArrayList();
    
    public Group() {
        
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getVacancies() {
        return vacancies;
    }
    
    public void setVacancies(Integer vacancies) {
        if(vacancies < 0) {
            throw new IllegalArgumentException();
        }
        
        this.vacancies = vacancies;
    }
    
    public List<User> getMembers() {
        return members;
    }
    
    public void addMember(User user) {
        members.add(user);
        user.getGroups().add(this);
    }
    
    public void removeMember(User user) {
        List<Presence> userPresence = getUserPresence(user);
        
        for(Presence singlePresence : userPresence) {
            removePresence(user, singlePresence.getDate());
        }
        
        members.remove(user);
        user.getGroups().remove(this);
    }
   
    public List<Presence> getPresence() {
        return presence;
    }
    
    public List<Presence> getUserPresence(User user) {
        List<Presence> result = new ArrayList<Presence>();
        
        for(Presence userPresence : presence) {
            if(userPresence.getUser().equals(user)) {
                result.add(userPresence);
            }
        }
        
        return result;
    }
    
    public List<Presence> getDatePresence(Date date) {
        List<Presence> result = new ArrayList<Presence>();
        
        for(Presence userPresence : presence) {
            if(userPresence.getDate().equals(date)) {
                result.add(userPresence);
            }
        }
        
        return result;
    }
    
    public void setPresence(User user, Date date, Boolean presence) {
        Presence newPresence = new Presence();
        newPresence.setGroupId(this.id);
        newPresence.setUser(user);
        newPresence.setDate(date);
        
        if(!this.presence.contains(newPresence)) {
            this.presence.add(newPresence);
        }
        
        this.presence.get(this.presence.indexOf(newPresence)).setPresence(presence);
    }
    
    public void removePresence(User user, Date date) {
        Presence existingPresence = new Presence();
        existingPresence.setGroupId(this.id);
        existingPresence.setUser(user);
        existingPresence.setDate(date);
        
        this.presence.remove(existingPresence);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Group)) {
            return false;
        }
        Group other = (Group) object;
        return this.name.equals(other.name) && this.description.equals(other.description);
    }

    @Override
    public String toString() {
        return "pl.cezaryregec.entities.Group[ id=" + id + ", name = " + name + ", description = " + description + ", vacancies = " + vacancies + " ]";
    }
}
