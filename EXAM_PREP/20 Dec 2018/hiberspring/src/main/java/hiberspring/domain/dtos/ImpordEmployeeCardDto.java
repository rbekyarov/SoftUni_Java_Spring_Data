package hiberspring.domain.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

public class ImpordEmployeeCardDto {
    @Expose
    private String number;

    public ImpordEmployeeCardDto() {
    }
@NotNull
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
