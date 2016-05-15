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
 * A Fractionnement.
 */
@Entity
@Table(name = "fractionnement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fractionnement")
public class Fractionnement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "valeur", nullable = false)
    private Integer valeur;

    @ManyToOne
    private Titre titre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getValeur() {
        return valeur;
    }

    public void setValeur(Integer valeur) {
        this.valeur = valeur;
    }

    public Titre getTitre() {
        return titre;
    }

    public void setTitre(Titre titre) {
        this.titre = titre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fractionnement fractionnement = (Fractionnement) o;
        if(fractionnement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fractionnement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fractionnement{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", valeur='" + valeur + "'" +
            '}';
    }
}
