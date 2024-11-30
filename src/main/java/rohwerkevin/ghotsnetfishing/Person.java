package rohwerkevin.ghotsnetfishing;

import jakarta.persistence.*;

@Entity
@Table(name = "personen")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    // Weitere Felder und Methoden nach Bedarf

    // Getter und Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Konstruktoren
    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }
}
