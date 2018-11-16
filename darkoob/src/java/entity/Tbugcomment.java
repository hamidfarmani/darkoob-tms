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
 * Tbugcomment generated by hbm2java
 */
@Entity
@Table(name = "TBUGCOMMENT", schema = "DARKOOBUSER"
)
public class Tbugcomment implements java.io.Serializable {

    private BigDecimal id;
    private Tuser tuser;
    private Tbug tbug;
    private String bugcomment;
    private Date creationdate;
    private Date dto;

    public Tbugcomment() {
    }

    public Tbugcomment(BigDecimal id, Tuser tuser, Tbug tbug, String bugcomment) {
        this.id = id;
        this.tuser = tuser;
        this.tbug = tbug;
        this.bugcomment = bugcomment;
    }

    public Tbugcomment(BigDecimal id, Tuser tuser, Tbug tbug, String bugcomment, Date creationdate, Date dto) {
        this.id = id;
        this.tuser = tuser;
        this.tbug = tbug;
        this.bugcomment = bugcomment;
        this.creationdate = creationdate;
        this.dto = dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUGCOMMENT_SEQ")
    @SequenceGenerator(name = "BUGCOMMENT_SEQ", sequenceName = "BUGCOMMENT_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CREATEDBYID", nullable = false)
    public Tuser getTuser() {
        return this.tuser;
    }

    public void setTuser(Tuser tuser) {
        this.tuser = tuser;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUGID", nullable = false)
    public Tbug getTbug() {
        return this.tbug;
    }

    public void setTbug(Tbug tbug) {
        this.tbug = tbug;
    }

    @Column(name = "BUGCOMMENT", nullable = false, length = 2000)
    public String getBugcomment() {
        return this.bugcomment;
    }

    public void setBugcomment(String bugcomment) {
        this.bugcomment = bugcomment;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATIONDATE", length = 7)
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

}
