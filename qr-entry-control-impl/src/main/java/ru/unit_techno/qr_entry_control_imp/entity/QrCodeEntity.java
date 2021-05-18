
package ru.unit_techno.qr_entry_control_imp.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@Entity
@Table(name = "qr_code", schema = "qr_entry_control")
@SequenceGenerator(name = "qr_code_id_seq", sequenceName = "qr_code_id_seq")
public class QrCodeEntity {
    private Long qrId;
    private String qrPicture;
    private Timestamp creationDate;
    private String governmentNumber;
    private String name;
    private String surname;
    private String email;
    private Boolean expire;

    @Id
    @Column(name = "qr_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qr_code_id_seq")
    public Long getQrId() {
        return qrId;
    }

    public void setQrId(Long qrId) {
        this.qrId = qrId;
    }

    @Basic
    @Column(name = "qr_picture")
    public String getQrPicture() {
        return qrPicture;
    }

    public void setQrPicture(String qrPicture) {
        this.qrPicture = qrPicture;
    }

    @Basic
    @Column(name = "creation_date")
    //TODO create date time after save
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "government_number")
    public String getGovernmentNumber() {
        return governmentNumber;
    }

    public void setGovernmentNumber(String governmentNumber) {
        this.governmentNumber = governmentNumber;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "expire")
    public Boolean getExpire() {
        return expire;
    }

    public void setExpire(Boolean expire) {
        this.expire = expire;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QrCodeEntity that = (QrCodeEntity) o;
        return Objects.equals(qrId, that.qrId) && Objects.equals(qrPicture, that.qrPicture) && Objects.equals(creationDate, that.creationDate) && Objects.equals(governmentNumber, that.governmentNumber) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(expire, that.expire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qrId, qrPicture, creationDate, governmentNumber, name, surname, expire);
    }
}