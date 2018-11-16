package entity;
// Generated Oct 31, 2016 12:44:50 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tprojecttreetype generated by hbm2java
 */
@Entity
@Table(name = "TPROJECTTREETYPE", schema = "DARKOOBUSER"
)
public class Tprojecttreetype implements java.io.Serializable {

    private BigDecimal id;
    private Tproject tproject;
    private String name;
    private String description;
    private BigDecimal createdbyid;
    private Date dto;
    private Set<Tprojecttree> tprojecttrees = new HashSet<Tprojecttree>(0);

    public Tprojecttreetype() {
    }

    public Tprojecttreetype(BigDecimal id, Tproject tproject, String name, BigDecimal createdbyid) {
        this.id = id;
        this.tproject = tproject;
        this.name = name;
        this.createdbyid = createdbyid;
    }

    public Tprojecttreetype(BigDecimal id, Tproject tproject, String name, String description, BigDecimal createdbyid, Date dto, Set<Tprojecttree> tprojecttrees) {
        this.id = id;
        this.tproject = tproject;
        this.name = name;
        this.description = description;
        this.createdbyid = createdbyid;
        this.dto = dto;
        this.tprojecttrees = tprojecttrees;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIERARCHY_SEQ")
    @SequenceGenerator(name = "HIERARCHY_SEQ", sequenceName = "HIERARCHY_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECTID", nullable = false)
    public Tproject getTproject() {
        return this.tproject;
    }

    public void setTproject(Tproject tproject) {
        this.tproject = tproject;
    }

    @Column(name = "NAME", nullable = false, length = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION", length = 2000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CREATEDBYID", nullable = false, precision = 22, scale = 0)
    public BigDecimal getCreatedbyid() {
        return this.createdbyid;
    }

    public void setCreatedbyid(BigDecimal createdbyid) {
        this.createdbyid = createdbyid;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DTO", length = 7)
    public Date getDto() {
        return this.dto;
    }

    public void setDto(Date dto) {
        this.dto = dto;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tprojecttreetype")
    public Set<Tprojecttree> getTprojecttrees() {
        return this.tprojecttrees;
    }

    public void setTprojecttrees(Set<Tprojecttree> tprojecttrees) {
        this.tprojecttrees = tprojecttrees;
    }

}
