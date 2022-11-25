package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class AgentsSeedDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String town;
    @Expose
    private String email;


    public AgentsSeedDto() {
    }

    @Size(min = 2)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 2)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTown() {
        return town;
    }


    public AgentsSeedDto setTown(String town) {
        this.town = town;
        return this;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public AgentsSeedDto setEmail(String email) {
        this.email = email;
        return this;
    }
}
