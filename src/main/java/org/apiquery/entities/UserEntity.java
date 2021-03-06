package org.apiquery.entities;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import java.util.Set;

@Table(name = "User")
@Entity
public class UserEntity {
    @Column(name = "Id")
    @Id
    private int id;
    @Column(name = "Name")
    private String name;

    @ManyToMany(mappedBy = "users")
    private Set<RoleEntity> roles;

    public Set<RoleEntity> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}