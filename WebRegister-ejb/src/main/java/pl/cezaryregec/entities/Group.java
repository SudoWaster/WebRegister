package pl.cezaryregec.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "groups")
@XmlRootElement
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
        return "pl.cezaryregec.Group[ id=" + id + ", name = " + name + ", description = " + description + ", vacancies = " + vacancies + " ]";
    }
}
