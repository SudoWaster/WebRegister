package pl.cezaryregec.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    
    @OneToMany(cascade = { 
        CascadeType.MERGE,
        CascadeType.PERSIST,
        CascadeType.REMOVE
    }, mappedBy = "group_assignment")
    private List<GroupAssignment> members = new ArrayList<>();
    
    @OneToMany(
        cascade = { 
        CascadeType.MERGE,
        CascadeType.PERSIST,
        CascadeType.REMOVE
    },
        orphanRemoval = true
    )
    @JoinColumn(name = "group_id")
    private List<Presence> presence = new ArrayList<>();
    
    @OneToMany(cascade = {
        CascadeType.MERGE,
        CascadeType.PERSIST,
        CascadeType.REMOVE
    })
    @JoinColumn(name = "group_id")
    private List<Achievement> achievements = new ArrayList<>();
    
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
        List<User> result = new ArrayList<>();
        
        for(GroupAssignment assignment : members) {
            result.add(assignment.getUser());
        }
        
        return result;
    }
    
    public List<User> getMembers(GroupRole role) {
        List<User> result = new ArrayList<>();
        
        for(GroupAssignment assignment : members) {
            if(assignment.getRole().equals(role)) {
                result.add(assignment.getUser());
            }
        }
        
        return result;
    }
    
    public void addMember(User user, GroupRole role) {
        GroupAssignment assignment = new GroupAssignment();
        assignment.setGroup(this);
        assignment.setUser(user);
        assignment.setRole(role);
        
        members.add(assignment);
        user.getGroupAssignment().add(assignment);
    }
    
    public GroupAssignment removeMember(User user) {
        GroupAssignment targetAssignment = new GroupAssignment();
        
        for(GroupAssignment assignment : members) {
            if(assignment.getUser().equals(user)) {
                targetAssignment = assignment;
            }
        }
        
        members.remove(targetAssignment);
        user.getGroupAssignment().remove(targetAssignment);
        
        return targetAssignment;
    }
    
    @JsonIgnore
    public List<GroupAssignment> getGroupAssignment() {
        return members;
    }
   
    public List<Presence> getPresence() {
        return presence;
    }
    
    public List<Presence> getUserPresence(User user) {
        List<Presence> result = new ArrayList<>();
        
        for(Presence userPresence : presence) {
            if(userPresence.getUser().equals(user)) {
                result.add(userPresence);
            }
        }
        
        return result;
    }
    
    public List<Presence> getDatePresence(Date date) {
        List<Presence> result = new ArrayList<>();
        List<User> instructors = getMembers(GroupRole.PRIVILEDGED);
        
        for(Presence userPresence : presence) {
            if(userPresence.getDate().equals(date)
                    && !instructors.contains(userPresence.getUser())) {
                result.add(userPresence);
            }
        }
        
        return result;
    }
    
    public Presence setPresence(User user, Date date, Boolean presence) {
        Presence newPresence = new Presence();
        newPresence.setGroup(this);
        newPresence.setUser(user);
        newPresence.setDate(date);
        
        if(!this.presence.contains(newPresence)) {
            this.presence.add(newPresence);
        }
        
        this.presence.get(this.presence.indexOf(newPresence)).setPresence(presence);
        
        return newPresence;
    }
    
    public Presence removePresence(User user, Date date) {
        Presence existingPresence = new Presence();
        existingPresence.setGroup(this);
        existingPresence.setUser(user);
        existingPresence.setDate(date);
        
        this.presence.remove(existingPresence);
        
        return existingPresence;
    }
    
    @XmlTransient
    @JsonIgnore
    public List<Achievement> getAchievements() {
        return achievements;
    }
    
    public void addAchievement(String name, String description) {
        Achievement achievement = new Achievement();
        achievement.setGroup(this);
        achievement.setName(name);
        achievement.setDescription(description);
        
        this.achievements.add(achievement);
    }
    
    public Achievement removeAchievement(String name, String description) {
        Achievement achievement = new Achievement();
        achievement.setGroup(this);
        achievement.setName(name);
        achievement.setDescription(description);
        
        this.achievements.remove(achievement);
        
        return achievement;
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
