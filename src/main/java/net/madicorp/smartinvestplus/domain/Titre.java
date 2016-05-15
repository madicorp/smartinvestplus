package net.madicorp.smartinvestplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Titre.
 */
@Entity
@Table(name = "titre")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "titre")
public class Titre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "symbole")
    private String symbole;

    @ManyToOne
    private Bourse bourse;

    @OneToMany(mappedBy = "titre")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Cloture> clotures = new HashSet<>();

    @OneToMany(mappedBy = "titre")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fractionnement> fractionnements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSymbole() {
        return symbole;
    }

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    public Bourse getBourse() {
        return bourse;
    }

    public void setBourse(Bourse bourse) {
        this.bourse = bourse;
    }

    public Set<Cloture> getClotures() {
        return clotures;
    }

    public void setClotures(Set<Cloture> clotures) {
        this.clotures = clotures;
    }

    public Set<Fractionnement> getFractionnements() {
        return fractionnements;
    }

    public void setFractionnements(Set<Fractionnement> fractionnements) {
        this.fractionnements = fractionnements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Titre titre = (Titre) o;
        if(titre.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, titre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Titre{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", symbole='" + symbole + "'" +
            '}';
    }
}
