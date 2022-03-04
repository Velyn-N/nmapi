package de.nmadev.nmapi.ejb.ausbildung.nachweise;

import de.nmadev.nmapi.ejb.ausbildung.nachweise.entities.Ausbildungsnachweis;
import de.nmadev.nmapi.ejb.ausbildung.nachweise.entities.AusbildungsnachweisActivity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class AusbildungsNachweisDao {

    @PersistenceContext(unitName="mysql")
    private EntityManager em;

    @Transactional
    public void saveNachweisActivities(List<AusbildungsnachweisActivity> activities) {
        for (AusbildungsnachweisActivity activity : activities) {
            try {
                em.persist(activity);
            } catch (EntityExistsException e) {
                em.merge(activity);
            }
        }
    }

    @Transactional
    public void saveNachweis(Ausbildungsnachweis nachweis) {
        try {
            em.persist(nachweis);
        } catch (EntityExistsException e) {
            em.merge(nachweis);
        }
    }

    @Transactional
    public List<Ausbildungsnachweis> getNachweiseBetweenDates(LocalDateTime fromDate, LocalDateTime untilDate) {
        return em.createQuery("SELECT nw FROM Ausbildungsnachweis nw WHERE nw.startDate >= :from OR nw.endDate <= :until")
                .setParameter("from", fromDate)
                .setParameter("until", untilDate)
                .getResultList();
    }

    @Transactional
    public List<AusbildungsnachweisActivity> getNachweisActivitiesForNachweis(Ausbildungsnachweis ausbildungsnachweis) {
        return em.createQuery("SELECT act FROM AusbildungsnachweisActivity act WHERE act.date >= :startdate AND act.date <= :enddate", AusbildungsnachweisActivity.class)
                .setParameter("startdate", ausbildungsnachweis.getStartDate())
                .setParameter("enddate", ausbildungsnachweis.getEndDate())
                .getResultList();
    }
}
