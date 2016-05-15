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
 * A Bourse.
 */
@Entity
@Table(name = "bourse")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bourse")
public class Bourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "symbole")
    private String symbole;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "bourse_user",
               joinColumns = @JoinColumn(name="bourses_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="ID"))
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "bourse")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Titre> titres = new HashSet<>();

    @OneToMany(mappedBy = "bourse")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Indice> indices = new HashSet<>();

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Titre> getTitres() {
        return titres;
    }

    public void setTitres(Set<Titre> titres) {
        this.titres = titres;
    }

    public Set<Indice> getIndices() {
        return indices;
    }

    public void setIndices(Set<Indice> indices) {
        this.indices = indices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bourse bourse = (Bourse) o;
        if(bourse.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bourse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bourse{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", symbole='" + symbole + "'" +
            '}';
    }
}
