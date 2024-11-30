package rohwerkevin.ghotsnetfishing;

import jakarta.persistence.*;

@Entity
@Table(name = "geisternetze")
public class Geisternetz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // GPS-Koordinaten
    @Column(name = "standort_latitude", nullable = false)
    private double standortLatitude;

    @Column(name = "standort_longitude", nullable = false)
    private double standortLongitude;

    // Geschätzte Größe
    @Column(name = "geschaetzte_groesse", nullable = false)
    private double geschaetzteGroesse;

    // Beschreibung (optional)
    @Column(name = "beschreibung", length = 500)
    private String beschreibung;

    // Status des Geisternetzes
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // Meldende Person (kann NULL sein, wenn anonym)
    @ManyToOne(cascade = CascadeType.PERSIST) // Cascade Persist hinzugefügt
    @JoinColumn(name = "meldende_person_id")
    private Person meldendePerson;

    // Bergende Person (kann NULL sein, wenn noch nicht zugewiesen)
    @ManyToOne(cascade = CascadeType.PERSIST) // Optional: Cascade Persist hinzufügen
    @JoinColumn(name = "bergende_person_id")
    private Person bergendePerson;

    // Konstruktoren
    public Geisternetz() {
    }

    public Geisternetz(double standortLatitude, double standortLongitude, double geschaetzteGroesse, Status status) {
        this.standortLatitude = standortLatitude;
        this.standortLongitude = standortLongitude;
        this.geschaetzteGroesse = geschaetzteGroesse;
        this.status = status;
    }

    // Getter und Setter
    public Integer getId() {
        return id;
    }

    public double getStandortLatitude() {
        return standortLatitude;
    }

    public void setStandortLatitude(double standortLatitude) {
        this.standortLatitude = standortLatitude;
    }

    public double getStandortLongitude() {
        return standortLongitude;
    }

    public void setStandortLongitude(double standortLongitude) {
        this.standortLongitude = standortLongitude;
    }

    public double getGeschaetzteGroesse() {
        return geschaetzteGroesse;
    }

    public void setGeschaetzteGroesse(double geschaetzteGroesse) {
        this.geschaetzteGroesse = geschaetzteGroesse;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getMeldendePerson() {
        return meldendePerson;
    }

    public void setMeldendePerson(Person meldendePerson) {
        this.meldendePerson = meldendePerson;
    }

    public Person getBergendePerson() {
        return bergendePerson;
    }

    public void setBergendePerson(Person bergendePerson) {
        this.bergendePerson = bergendePerson;
    }
}
