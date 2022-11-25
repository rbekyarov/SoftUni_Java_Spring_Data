package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "agents")
public class Agent extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private Town town;

    public Agent() {
    }

    @Column(unique = true, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }


    @ManyToOne
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @Column(nullable = false)
    public String getLastName() {
        return lastName;
    }

    public Agent setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Column(unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public Agent setEmail(String email) {
        this.email = email;
        return this;
    }
}
