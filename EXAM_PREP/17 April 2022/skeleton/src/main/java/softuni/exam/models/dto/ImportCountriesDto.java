package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class ImportCountriesDto {
    @Expose
    private String countryName;
    @Expose
    private String	currency;

    public ImportCountriesDto() {
    }
    @Size(min = 2, max = 60)
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    @Size(min = 2, max = 20)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
