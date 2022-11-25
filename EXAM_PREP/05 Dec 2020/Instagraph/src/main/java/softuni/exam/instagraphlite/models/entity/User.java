package softuni.exam.instagraphlite.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity{
    private String username;
    private String password ;

    private Picture profilePicture;
    @ManyToOne
    @JoinColumn(name = "profile_picture_id")
    public Picture getProfilePicture() {
        return profilePicture;
    }

    public User() {
    }
    @Column(unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
     @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }
}
