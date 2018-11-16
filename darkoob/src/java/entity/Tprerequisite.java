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
 * Tprerequisite generated by hbm2java
 */
@Entity
@Table(name = "TPREREQUISITE", schema = "DARKOOBUSER"
)
public class Tprerequisite implements java.io.Serializable {

    private BigDecimal id;
    private Ttestcase ttestcaseByTestcaseid;
    private Ttestcase ttestcaseByPrerequisiteid;
    private String description;
    private Date dto;

    public Tprerequisite() {
    }

    public Tprerequisite(Ttestcase ttestcaseByTestcaseid, Ttestcase ttestcaseByPrerequisiteid) {
        this.ttestcaseByTestcaseid = ttestcaseByTestcaseid;
        this.ttestcaseByPrerequisiteid = ttestcaseByPrerequisiteid;
    }

    public Tprerequisite(BigDecimal id, Ttestcase ttestcaseByTestcaseid, Ttestcase ttestcaseByPrerequisiteid, String description, Date dto) {
        this.id = id;
        this.ttestcaseByTestcaseid = ttestcaseByTestcaseid;
        this.ttestcaseByPrerequisiteid = ttestcaseByPrerequisiteid;
        this.description = description;
        this.dto = dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUISITE_SEQ")
    @SequenceGenerator(name = "REQUISITE_SEQ", sequenceName = "REQUISITE_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESTCASEID", nullable = false)
    public Ttestcase getTtestcaseByTestcaseid() {
        return this.ttestcaseByTestcaseid;
    }

    public void setTtestcaseByTestcaseid(Ttestcase ttestcaseByTestcaseid) {
        this.ttestcaseByTestcaseid = ttestcaseByTestcaseid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREREQUISITEID", nullable = false)
    public Ttestcase getTtestcaseByPrerequisiteid() {
        return this.ttestcaseByPrerequisiteid;
    }

    public void setTtestcaseByPrerequisiteid(Ttestcase ttestcaseByPrerequisiteid) {
        this.ttestcaseByPrerequisiteid = ttestcaseByPrerequisiteid;
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
