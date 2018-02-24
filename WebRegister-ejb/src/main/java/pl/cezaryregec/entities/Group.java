package pl.cezaryregec.entities;

import java.io.Serializable;
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
    @NamedQuery(name = "Group.findById", query = "SELECT g FROM Group g WHERE g.group_id = :id"),
    @NamedQuery(name = "Group.findOpen", query = "SELECT g FROM Group g WHERE g.group_vacancies > 0")
})
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
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "group_assignment",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> members = new ArrayList();
    
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
    }
    
    public void removeMember(User user) {
        members.remove(user);
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
