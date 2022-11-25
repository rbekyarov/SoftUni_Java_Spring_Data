package softuni.exam.instagraphlite.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity{
    private String path;
    private Float size ;

    public Picture() {
    }
    @Column(name = "path", nullable = false, unique = true)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    @Column(name = "size", nullable = false)
    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }
}
