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
 * Tversionfile generated by hbm2java
 */
@Entity
@Table(name = "TVERSIONFILE", schema = "DARKOOBUSER"
)
public class Tversionfile implements java.io.Serializable {

    private BigDecimal id;
    private Tversion tversion;
    private String filename;
    private String filepath;
    private String description;
    private Date creationdate;
    private BigDecimal createdbyid;
    private Date dto;

    public Tversionfile() {
    }

    public Tversionfile(BigDecimal id, Tversion tversion, String filename, String filepath, Date creationdate, BigDecimal createdbyid) {
        this.id = id;
        this.tversion = tversion;
        this.filename = filename;
        this.filepath = filepath;
        this.creationdate = creationdate;
        this.createdbyid = createdbyid;
    }

    public Tversionfile(BigDecimal id, Tversion tversion, String filename, String filepath, String description, Date creationdate, BigDecimal createdbyid, Date dto) {
        this.id = id;
        this.tversion = tversion;
        this.filename = filename;
        this.filepath = filepath;
        this.description = description;
        this.creationdate = creationdate;
        this.createdbyid = createdbyid;
        this.dto = dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TVERSIONFILE_SEQ")
    @SequenceGenerator(name = "TVERSIONFILE_SEQ", sequenceName = "TVERSIONFILE_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSIONID", nullable = false)
    public Tversion getTversion() {
        return this.tversion;
    }

    public void setTversion(Tversion tversion) {
        this.tversion = tversion;
    }

    @Column(name = "FILENAME", nullable = false, length = 200)
    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Column(name = "FILEPATH", nullable = false, length = 400)
    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Column(name = "DESCRIPTION", length = 2000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATIONDATE", nullable = false, length = 7)
    public Date getCreationdate() {
        return this.creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
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

}