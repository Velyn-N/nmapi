package de.nmadev.nmapi.rest.user.auth;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.nmadev.nmapi.ApplicationConstants;
import de.nmadev.nmapi.ejb.user.UserFactoryBean;
import de.nmadev.nmapi.ejb.user.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class ApiAuthenticationBean {
    private static final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Inject
    private UserFactoryBean userFactory;

    private LoadingCache<String, User> jwtUserCache;

    private Supplier<User> defaultUserSupplier;

    @PostConstruct
    private void init() {
        defaultUserSupplier = Suppliers.memoizeWithExpiration(() -> userFactory.getDefaultUser(), 12, TimeUnit.HOURS);

        jwtUserCache = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    @Override
                    public User load(String key) {
                        return userFactory.getUser(parseJWT(key)).orElse(userFactory.getDefaultUser());
                    }
                });
    }

    public User getUser(String jwt) {
        return (jwt != null) ? jwtUserCache.getUnchecked(jwt) : defaultUserSupplier.get();
    }

    /**
     * <b>Cached</b> access to the Default User
     */
    public User getDefaultUser() {
        return defaultUserSupplier.get();
    }

    public String createJWT(Long userId) {
        Date now = Date.from(Instant.now());

        Map<String, String> claims = new HashMap<>();
        claims.put(ApplicationConstants.JWT_USERID_CLAIM.value, userId.toString());

        return Jwts.builder()
                .setSubject(ApplicationConstants.JWT_SUBJECT.value)
                .setIssuedAt(now)
                .setIssuer(ApplicationConstants.JWT_ISSUER.value)
                .setExpiration(DateUtils.addMonths(now, 1))
                .setClaims(claims)
                .signWith(jwtKey)
                .compact();
    }

    public Long parseJWT(String jwt) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jwt);
            String claimedUserId = jws.getBody().get(ApplicationConstants.JWT_USERID_CLAIM.value, String.class);

            return NumberUtils.toLong(claimedUserId, ApplicationConstants.DEFAULT_USER_ID.longValue());
        } catch (JwtException e) {
            log.warn("Could not Parse JWT: {}", jwt);
            return ApplicationConstants.DEFAULT_USER_ID.longValue();
        }
    }
}
