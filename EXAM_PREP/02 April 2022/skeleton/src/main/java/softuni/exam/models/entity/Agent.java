package softuni.exam.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "agents")
public class Agent extends BaseEntity {
    private String firstName;
    private String lastName ;
    private String email  ;
    private Town town;

    public Agent() {
    }

    public Agent(String firstName, String lastName, String email, Town town) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.town = town;
    }
    @Column(name = "first_name", unique = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @ManyToOne
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
