package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class ImportPictureDto {
    @Expose
    private String name;
    @Expose
    private String dateAndTime;
    @Expose
    private String car;

    public ImportPictureDto() {
    }
@Size(min=2,max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getCarId() {
        return car;
    }

    public void setCarId(String carId) {
        this.car = carId;
    }
}
