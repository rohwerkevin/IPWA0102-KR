package rohwerkevin.ghotsnetfishing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Named
@ApplicationScoped
public class PersonDAO {

    private static EntityManagerFactory emf;
    private EntityManager em;

    public PersonDAO() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("G1");
        }
        em = emf.createEntityManager();
    }

    public void persist(Person person) {
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    // Optional: Methoden zum Abrufen, Aktualisieren, Löschen von Personen
}
