package entity;
// Generated Oct 31, 2016 12:44:50 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TversionProjecttree generated by hbm2java
 */
@Entity
@Table(name = "TVERSION_PROJECTTREE", schema = "DARKOOBUSER"
)
public class TversionProjecttree implements java.io.Serializable {

    private BigDecimal id;
    private Tprojecttree tprojecttree;
    private Tversion tversion;
    private String type;
    private String description;
    private Date dto;

    public TversionProjecttree() {
    }

    public TversionProjecttree( Tprojecttree tprojecttree, Tversion tversion) {
        this.tprojecttree = tprojecttree;
        this.tversion = tversion;
    }

    public TversionProjecttree(BigDecimal id, Tprojecttree tprojecttree, Tversion tversion, String type, String description, Date dto) {
        this.id = id;
        this.tprojecttree = tprojecttree;
        this.tversion = tversion;
        this.type = type;
        this.description = description;
        this.dto = dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERSIONPROJECTTREE_SEQ")
    @SequenceGenerator(name = "VERSIONPROJECTTREE_SEQ", sequenceName = "VERSIONPROJECTTREE_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECTTREEID", nullable = false)
    public Tprojecttree getTprojecttree() {
        return this.tprojecttree;
    }

    public void setTprojecttree(Tprojecttree tprojecttree) {
        this.tprojecttree = tprojecttree;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSIONID", nullable = false)
    public Tversion getTversion() {
        return this.tversion;
    }

    public void setTversion(Tversion tversion) {
        this.tversion = tversion;
    }

    @Column(name = "TYPE", length = 4)
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "DESCRIPTION", length = 2000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DTO", length = 7)
    public Date getDto() {
        return this.dto;
    }

    public void setDto(Date dto) {
        this.dto = dto;
    }

}
