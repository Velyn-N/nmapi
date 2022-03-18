package de.nmadev.nmapi.ejb.user;

import de.nmadev.nmapi.ejb.user.entities.User;
import de.nmadev.nmapi.ejb.user.entities.UserPermission;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserDao implements Serializable {

    @PersistenceContext(unitName="mysql")
    private EntityManager em;

    @Transactional
    public Optional<User> findUser(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Transactional
    public Optional<User> findUser(String username) {
        return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findAny();
    }

    @Transactional
    public List<User> getAusbilderUsers() {
        return em.createQuery("SELECT u FROM User u WHERE u.isAusbilder = true", User.class)
                .getResultList();
    }

    @Transactional
    public List<UserPermission> getPermissionsFor(long userId) {
        return em.createQuery("SELECT p FROM UserPermission p WHERE p.userid = :userid", UserPermission.class)
                .setParameter("userid", userId)
                .getResultList();
    }
}
