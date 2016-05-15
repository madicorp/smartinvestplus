package net.madicorp.smartinvestplus.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Indice.
 */
@Entity
@Table(name = "indice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "indice")
public class Indice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "top", nullable = false)
    private Double top;

    @NotNull
    @Column(name = "composite", nullable = false)
    private Double composite;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    private Bourse bourse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTop() {
        return top;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    public Double getComposite() {
        return composite;
    }

    public void setComposite(Double composite) {
        this.composite = composite;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Bourse getBourse() {
        return bourse;
    }

    public void setBourse(Bourse bourse) {
        this.bourse = bourse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Indice indice = (Indice) o;
        if(indice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, indice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Indice{" +
            "id=" + id +
            ", top='" + top + "'" +
            ", composite='" + composite + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
