package softuni.exam.instagraphlite.models.dto;

import com.google.gson.annotations.Expose;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ImportPictureDto {
    @Expose
    private String path;
    @Expose
    private Float size;

    public ImportPictureDto() {
    }
    @NotNull
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Min(500)
    @Max(60000)
    @NotNull
    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }
}
