package de.nmadev.nmapi.rest.user;

import com.google.gson.Gson;
import de.nmadev.nmapi.ejb.user.entities.User;
import de.nmadev.nmapi.rest.user.auth.RequestUserBean;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.Data;

@Path("/self")
public class SelfUserRest {
    private static final Gson GSON = new Gson();

    @Inject
    private RequestUserBean requestUser;

    @GET
    @Path("/user")
    public String getSelfUser() {
        return GSON.toJson(requestUser.getUser());
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(LoginCredentials loginCredentials) {
        if (loginCredentials != null) {
            return requestUser.loginWithJWT(loginCredentials.username, loginCredentials.password);
        }
        return null;
    }

    @Data
    public static class LoginCredentials {
        private String username;
        private String password;
    }
}
