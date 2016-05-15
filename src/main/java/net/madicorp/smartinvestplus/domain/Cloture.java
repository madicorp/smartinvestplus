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
 * A Cloture.
 */
@Entity
@Table(name = "cloture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cloture")
public class Cloture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "valeur")
    private Integer valeur;

    @Column(name = "transactions")
    private Integer transactions;

    @NotNull
    @Column(name = "cours", nullable = false)
    private Double cours;

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

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getValeur() {
        return valeur;
    }

    public void setValeur(Integer valeur) {
        this.valeur = valeur;
    }

    public Integer getTransactions() {
        return transactions;
    }

    public void setTransactions(Integer transactions) {
        this.transactions = transactions;
    }

    public Double getCours() {
        return cours;
    }

    public void setCours(Double cours) {
        this.cours = cours;
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
        Cloture cloture = (Cloture) o;
        if(cloture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cloture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cloture{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", volume='" + volume + "'" +
            ", valeur='" + valeur + "'" +
            ", transactions='" + transactions + "'" +
            ", cours='" + cours + "'" +
            '}';
    }
}
