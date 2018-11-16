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
 * Tversion generated by hbm2java
 */
@Entity
@Table(name = "TVERSION", schema = "DARKOOBUSER"
)
public class Tversion implements java.io.Serializable {

    private BigDecimal id;
    private Tuser tuser;
    private Tprojecttree tprojecttree;
    private String name;
    private String desccription;
    private Date creationdate;
    private Date dto;
    private Set<TbugVersion> tbugVersions = new HashSet<TbugVersion>(0);
    private Set<TversionProjecttree> tversionProjecttrees = new HashSet<TversionProjecttree>(0);
    private Set<Tversionfile> tversionfiles = new HashSet<Tversionfile>(0);
    private Set<Titeration> titerations = new HashSet<Titeration>(0);

    public Tversion() {
    }

    public Tversion(BigDecimal id, Tuser tuser, Tprojecttree tprojecttree, String name, Date creationdate) {
        this.id = id;
        this.tuser = tuser;
        this.tprojecttree = tprojecttree;
        this.name = name;
        this.creationdate = creationdate;
    }

    public Tversion(BigDecimal id, Tuser tuser, Tprojecttree tprojecttree, String name, String desccription, Date creationdate, Date dto, Set<TbugVersion> tbugVersions, Set<TversionProjecttree> tversionProjecttrees, Set<Tversionfile> tversionfiles, Set<Titeration> titerations) {
        this.id = id;
        this.tuser = tuser;
        this.tprojecttree = tprojecttree;
        this.name = name;
        this.desccription = desccription;
        this.creationdate = creationdate;
        this.dto = dto;
        this.tbugVersions = tbugVersions;
        this.tversionProjecttrees = tversionProjecttrees;
        this.tversionfiles = tversionfiles;
        this.titerations = titerations;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERSION_SEQ")
    @SequenceGenerator(name = "VERSION_SEQ", sequenceName = "VERSION_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATEDBYID", nullable = false)
    public Tuser getTuser() {
        return this.tuser;
    }

    public void setTuser(Tuser tuser) {
        this.tuser = tuser;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECTTREEID", nullable = false)
    public Tprojecttree getTprojecttree() {
        return this.tprojecttree;
    }

    public void setTprojecttree(Tprojecttree tprojecttree) {
        this.tprojecttree = tprojecttree;
    }

    @Column(name = "NAME", nullable = false, length = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCCRIPTION", length = 2000)
    public String getDesccription() {
        return this.desccription;
    }

    public void setDesccription(String desccription) {
        this.desccription = desccription;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATIONDATE", nullable = false, length = 7)
    public Date getCreationdate() {
        return this.creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DTO", length = 7)
    public Date getDto() {
        return this.dto;
    }

    public void setDto(Date dto) {
        this.dto = dto;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tversion")
    public Set<TbugVersion> getTbugVersions() {
        return this.tbugVersions;
    }

    public void setTbugVersions(Set<TbugVersion> tbugVersions) {
        this.tbugVersions = tbugVersions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tversion")
    public Set<TversionProjecttree> getTversionProjecttrees() {
        return this.tversionProjecttrees;
    }

    public void setTversionProjecttrees(Set<TversionProjecttree> tversionProjecttrees) {
        this.tversionProjecttrees = tversionProjecttrees;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tversion")
    public Set<Tversionfile> getTversionfiles() {
        return this.tversionfiles;
    }

    public void setTversionfiles(Set<Tversionfile> tversionfiles) {
        this.tversionfiles = tversionfiles;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tversion")
    public Set<Titeration> getTiterations() {
        return this.titerations;
    }

    public void setTiterations(Set<Titeration> titerations) {
        this.titerations = titerations;
    }

}
