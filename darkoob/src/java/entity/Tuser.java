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
 * Tuser generated by hbm2java
 */
@Entity
@Table(name = "TUSER", schema = "DARKOOBUSER"
)
public class Tuser implements java.io.Serializable {

    private BigDecimal id;
    private Tuser tuser;
    private String fullname;
    private String username;
    private String password;
    private Integer roleid;
    private Date validfrom;
    private Date validto;
    private String descrption;
    private Date dto;
    private Set<Trequirement> trequirements = new HashSet<Trequirement>(0);
    private Set<Tuser> tusers = new HashSet<Tuser>(0);
    private Set<Tbugcomment> tbugcomments = new HashSet<Tbugcomment>(0);
    private Set<Tprojecttree> tprojecttrees = new HashSet<Tprojecttree>(0);
    private Set<Tbughistoy> tbughistoys = new HashSet<Tbughistoy>(0);
    private Set<Towner> towners = new HashSet<Towner>(0);
    private Set<Tproject> tprojects = new HashSet<Tproject>(0);
    private Set<Tversion> tversions = new HashSet<Tversion>(0);
    private Set<Titeration> titerations = new HashSet<Titeration>(0);
    private Set<Ttestcasefile> ttestcasefiles = new HashSet<Ttestcasefile>(0);
    private Set<Tbug> tbugs = new HashSet<Tbug>(0);
    private Set<Ttestcase> ttestcases = new HashSet<Ttestcase>(0);
    private Set<Tbugfile> tbugfiles = new HashSet<Tbugfile>(0);

    public Tuser() {
    }

    public Tuser(BigDecimal id, Tuser tuser, String fullname, String username, String password, Date validfrom) {
        this.id = id;
        this.tuser = tuser;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.validfrom = validfrom;
    }

    public Tuser(BigDecimal id, Tuser tuser, String fullname, String username, String password, Integer roleid, Date validfrom, Date validto, String descrption, Date dto, Set<Trequirement> trequirements, Set<Tuser> tusers, Set<Tbugcomment> tbugcomments, Set<Tprojecttree> tprojecttrees, Set<Tbughistoy> tbughistoys, Set<Towner> towners, Set<Tproject> tprojects, Set<Tversion> tversions, Set<Titeration> titerations, Set<Ttestcasefile> ttestcasefiles, Set<Tbug> tbugs, Set<Ttestcase> ttestcases, Set<Tbugfile> tbugfiles) {
        this.id = id;
        this.tuser = tuser;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.roleid = roleid;
        this.validfrom = validfrom;
        this.validto = validto;
        this.descrption = descrption;
        this.dto = dto;
        this.trequirements = trequirements;
        this.tusers = tusers;
        this.tbugcomments = tbugcomments;
        this.tprojecttrees = tprojecttrees;
        this.tbughistoys = tbughistoys;
        this.towners = towners;
        this.tprojects = tprojects;
        this.tversions = tversions;
        this.titerations = titerations;
        this.ttestcasefiles = ttestcasefiles;
        this.tbugs = tbugs;
        this.ttestcases = ttestcases;
        this.tbugfiles = tbugfiles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
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

    @Column(name = "FULLNAME", nullable = false, length = 200)
    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Column(name = "USERNAME", nullable = false, length = 200)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "PASSWORD", nullable = false, length = 200)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "ROLEID", precision = 6, scale = 0)
    public Integer getRoleid() {
        return this.roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "VALIDFROM", nullable = false, length = 7)
    public Date getValidfrom() {
        return this.validfrom;
    }

    public void setValidfrom(Date validfrom) {
        this.validfrom = validfrom;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "VALIDTO", length = 7)
    public Date getValidto() {
        return this.validto;
    }

    public void setValidto(Date validto) {
        this.validto = validto;
    }

    @Column(name = "DESCRPTION", length = 2000)
    public String getDescrption() {
        return this.descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DTO", length = 7)
    public Date getDto() {
        return this.dto;
    }

    public void setDto(Date dto) {
        this.dto = dto;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Trequirement> getTrequirements() {
        return this.trequirements;
    }

    public void setTrequirements(Set<Trequirement> trequirements) {
        this.trequirements = trequirements;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tuser> getTusers() {
        return this.tusers;
    }

    public void setTusers(Set<Tuser> tusers) {
        this.tusers = tusers;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tbugcomment> getTbugcomments() {
        return this.tbugcomments;
    }

    public void setTbugcomments(Set<Tbugcomment> tbugcomments) {
        this.tbugcomments = tbugcomments;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tprojecttree> getTprojecttrees() {
        return this.tprojecttrees;
    }

    public void setTprojecttrees(Set<Tprojecttree> tprojecttrees) {
        this.tprojecttrees = tprojecttrees;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tbughistoy> getTbughistoys() {
        return this.tbughistoys;
    }

    public void setTbughistoys(Set<Tbughistoy> tbughistoys) {
        this.tbughistoys = tbughistoys;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Towner> getTowners() {
        return this.towners;
    }

    public void setTowners(Set<Towner> towners) {
        this.towners = towners;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tproject> getTprojects() {
        return this.tprojects;
    }

    public void setTprojects(Set<Tproject> tprojects) {
        this.tprojects = tprojects;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tversion> getTversions() {
        return this.tversions;
    }

    public void setTversions(Set<Tversion> tversions) {
        this.tversions = tversions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Titeration> getTiterations() {
        return this.titerations;
    }

    public void setTiterations(Set<Titeration> titerations) {
        this.titerations = titerations;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Ttestcasefile> getTtestcasefiles() {
        return this.ttestcasefiles;
    }

    public void setTtestcasefiles(Set<Ttestcasefile> ttestcasefiles) {
        this.ttestcasefiles = ttestcasefiles;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tbug> getTbugs() {
        return this.tbugs;
    }

    public void setTbugs(Set<Tbug> tbugs) {
        this.tbugs = tbugs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Ttestcase> getTtestcases() {
        return this.ttestcases;
    }

    public void setTtestcases(Set<Ttestcase> ttestcases) {
        this.ttestcases = ttestcases;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuser")
    public Set<Tbugfile> getTbugfiles() {
        return this.tbugfiles;
    }

    public void setTbugfiles(Set<Tbugfile> tbugfiles) {
        this.tbugfiles = tbugfiles;
    }

}
