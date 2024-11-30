package rohwerkevin.ghotsnetfishing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;

@Named
@ApplicationScoped
public class GeisternetzDAO {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private CriteriaBuilder builder;

    public GeisternetzDAO() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("G1");
        }
        em = emf.createEntityManager();
        builder = em.getCriteriaBuilder();
    }

    public long getGeisternetzCount() {
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        cq.select(builder.count(cq.from(Geisternetz.class)));
        return em.createQuery(cq).getSingleResult();
    }

    public void persist(Geisternetz gen) {
        try {
            em.getTransaction().begin();
            em.persist(gen);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public List<Geisternetz> getAllGeisternetze() {
        return em.createQuery("SELECT g FROM Geisternetz g ORDER BY g.id", Geisternetz.class).getResultList();
    }

    public void updateStatus(Integer geisternetzId, Status newStatus) {
        try {
            em.getTransaction().begin();
            // Geisternetz anhand der ID finden
            Geisternetz geisternetz = em.find(Geisternetz.class, geisternetzId);

            if (geisternetz != null) {
                geisternetz.setStatus(newStatus); // Setzt den neuen Status
                em.merge(geisternetz); // Änderungen persistieren
            } else {
                throw new IllegalArgumentException("Geisternetz mit ID " + geisternetzId + " nicht gefunden.");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
}
