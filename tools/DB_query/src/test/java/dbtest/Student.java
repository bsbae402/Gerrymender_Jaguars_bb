package dbtest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private int stdId;

    public Student() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStdId() {
        return stdId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStdId(int stdId) {
        this.stdId = stdId;
    }
}
