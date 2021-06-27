package org.apiquery.entities;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.Set;

@Table(name = "Role")
@Entity
public class RoleEntity {
    @Column(name = "Id")
    @Id
    private int id;
    @Column(name = "Name")
    private String name;

    @ManyToMany
    @JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "RoleId"), inverseJoinColumns = @JoinColumn(name = "UserId"))
    private Set<UserEntity> users;

    public Set<UserEntity> getUsers() {
        return this.users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
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