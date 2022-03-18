package de.nmadev.nmapi.ejb.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.nmadev.nmapi.ApplicationConstants;
import de.nmadev.nmapi.ejb.user.entities.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Optional;

@Stateless
public class UserFactoryBean implements Serializable {

    public static final long DEFAULT_USER_ID = ApplicationConstants.DEFAULT_USER_ID.longValue();

    @EJB
    private UserDao userDao;

    public User getDefaultUser() {
        return getUser(DEFAULT_USER_ID).orElse(null);
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> userOptional = userDao.findUser(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (checkPassword(password, user.getPassword())) {
                if (user.isAccepted() && !user.isBanned()) {
                    return Optional.of(loadAllDataOf(user));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<User> getFullyLoadedUser(long id) {
        Optional<User> userOptional = getUser(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getUserPermissions();
        }
        return userOptional;
    }

    public Optional<User> getUser(long id) {
        Optional<User> optionalUser = userDao.findUser(id);
        return (optionalUser.isPresent()) ? Optional.of(loadAllDataOf(optionalUser.get())) : Optional.empty();
    }

    public User loadAllDataOf(@NotNull User user) {
        user.getUserPermissions();
        return user;
    }

    public String hashPassword(String rawPassword) {
        return BCrypt.withDefaults().hashToString(14, rawPassword.toCharArray());
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword.toCharArray());
        return result.verified;
    }
}
