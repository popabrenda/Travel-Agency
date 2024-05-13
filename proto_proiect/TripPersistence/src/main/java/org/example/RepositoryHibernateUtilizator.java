package org.example;

import org.example.Interfaces.RepositoryUtilizator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.example.Utilizator;
import java.util.List;
import java.util.Optional;

public class RepositoryHibernateUtilizator implements RepositoryUtilizator {

    private final SessionFactory sessionFactory;

    public RepositoryHibernateUtilizator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Utilizator> findUtilizatorByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.createQuery("from Utilizator where username = :username", Utilizator.class)
                    .setParameter("username", username)
                    .uniqueResult());
        } catch (Exception e) {
            System.err.println("Eroare la findUtilizatorByUsernameHibernate " + e);
            return Optional.empty(); // Returnează un Optional gol în caz de eroare
        }
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            System.out.println("Utilizator adaugat: " + entity);
            return Optional.of(entity);
        } catch (Exception e) {
            System.err.println("Eroare la salvarea utilizatorului " + entity + ": " + e);
            return Optional.empty(); // Returnează un Optional gol în caz de eroare
        }
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Utilizator utilizator = session.merge(entity);
            session.getTransaction().commit();
            return Optional.of(utilizator);
        } catch (Exception e) {
            System.err.println("Eroare la actualizarea utilizatorului " + entity + ": " + e);
            return Optional.empty(); // Returnează un Optional gol în caz de eroare
        }
    }

    @Override
    public Optional<Utilizator> delete(Integer idEntity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Utilizator utilizator = session.find(Utilizator.class, idEntity);
            if (utilizator != null) {
                session.delete(utilizator);
                session.getTransaction().commit();
                return Optional.of(utilizator);
            } else {
                return Optional.empty(); // Utilizatorul nu a fost găsit
            }
        } catch (Exception e) {
            System.err.println("Eroare la ștergerea utilizatorului cu id " + idEntity + ": " + e);
            return Optional.empty(); // Returnează un Optional gol în caz de eroare
        }
    }

    @Override
    public Optional<Utilizator> findOne(Integer idEntity) {
        try (Session session = sessionFactory.openSession()) {
            Utilizator utilizator = session.find(Utilizator.class, idEntity);
            return Optional.ofNullable(utilizator);
        } catch (Exception e) {
            System.err.println("Eroare la găsirea utilizatorului cu id " + idEntity + ": " + e);
            return Optional.empty(); // Returnează un Optional gol în caz de eroare
        }
    }

    @Override
    public List<Utilizator> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Utilizator", Utilizator.class).getResultList();
        } catch (Exception e) {
            System.err.println("Eroare la găsirea tuturor utilizatorilor: " + e);
            return List.of(); // Returnează o listă goală în caz de eroare
        }
    }
}
