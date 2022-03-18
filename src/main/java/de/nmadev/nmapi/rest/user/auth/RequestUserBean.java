package de.nmadev.nmapi.rest.user.auth;

import de.nmadev.nmapi.ApplicationConstants;
import de.nmadev.nmapi.ejb.user.UserFactoryBean;
import de.nmadev.nmapi.ejb.user.entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@RequestScoped
public class RequestUserBean {

    @Inject
    private UserFactoryBean userFactoryBean;
    @Inject
    private ApiAuthenticationBean apiAuthBean;
    @Inject
    private HttpServletRequest request;

    @Getter
    private User user;

    @PostConstruct
    private void init() {
        String jwtHeader = request.getHeader(ApplicationConstants.JWT_REQUEST_HEADER.value);
        user = apiAuthBean.getUser(jwtHeader);
    }

    /**
     * Logs in the given User
     * @return A JWT for logged in User
     */
    public @Nullable String loginWithJWT(String username, String password) {
        Optional<User> userOptional = userFactoryBean.loginUser(username, password);
        if (userOptional.isPresent()) {
            this.user = userOptional.get();
            return apiAuthBean.createJWT(userOptional.get().getId());
        }
        return null;
    }

    public boolean isLoggedIn() {
        return user != null && !(user.getId().longValue() == ApplicationConstants.DEFAULT_USER_ID.longValue().longValue());
    }
}
