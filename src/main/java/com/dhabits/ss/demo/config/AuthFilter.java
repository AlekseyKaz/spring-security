package com.dhabits.ss.demo.config;

import com.dhabits.ss.demo.data.entity.UserEntity;
import com.dhabits.ss.demo.data.enums.UserRoles;
import com.dhabits.ss.demo.data.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    private final ErrorResponseWriter errorResponseWriter;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if(!checkJwtSkip(request)) {
            try {
                String token = extractToken(request);
                Jwt jwt = jwtDecoder.decode(token);
                UserEntity user = findUser(jwt);
                List<UserRoles> authorities = Arrays.stream(UserRoles.values())
                        .filter(role -> role.hasAuthority(user.getActions()))
                        .collect(Collectors.toList());
                LoggedUser loggedUser = new LoggedUser(user.getId(), user.getLogin(), user.getRefreshToken(), user.getPassword(), authorities);

                var authToken = new UserAuthToken(loggedUser, token, authorities);
                authToken.setDetails(jwt);
                authToken.setAuthenticated(true);
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            } catch (UsernameNotFoundException e) {
                log.warn("User not found", e);
                errorResponseWriter.write(response, HttpStatus.NOT_FOUND, "user not found");
            } catch (JwtException e) {
                log.info("Unable to get jwt", e);
                errorResponseWriter.write(response, HttpStatus.UNAUTHORIZED, "invalid bearer");
            } catch (Exception e) {
                log.error("Unable to authenticate", e);
                errorResponseWriter.write(response, HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
            }
        }
//            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                chain.doFilter(request, response);
//            }

    }

    private UserEntity findUser(Jwt jwt) {
        String login = (String) jwt.getClaims().get("preferred_username");
        if (!StringUtils.hasLength(login)) {
            throw new JwtException("missing login");
        }
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(login));
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null) {
            throw new JwtException("bearer is missing");
        }
        return bearer.replace("Bearer ", "");
    }
    private boolean checkJwtSkip(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/token/auth");
    }
}
