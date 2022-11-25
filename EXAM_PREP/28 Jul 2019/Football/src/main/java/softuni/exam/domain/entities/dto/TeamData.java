package softuni.exam.domain.entities.dto;

import com.google.gson.annotations.Expose;

public class TeamData {
    @Expose
    private String name;
    @Expose
    private PictureURL picture;

    public TeamData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureURL getPicture() {
        return picture;
    }

    public void setPicture(PictureURL picture) {
        this.picture = picture;
    }
}
