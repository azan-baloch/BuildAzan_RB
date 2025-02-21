package com.buildazan.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsServiceImpl;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request,
    // HttpServletResponse response, FilterChain filterChain)
    // throws ServletException, IOException {
    // String jwtToken = jwtService.extractTokenFromCookie(request);
    // if (jwtToken != null &&
    // SecurityContextHolder.getContext().getAuthentication() == null) {
    // String username = jwtService.extractUsername(jwtToken);
    // System.out.println("username " + username);
    // UserDetails userDetails =
    // userDetailsServiceImpl.loadUserByUsername(username);

    // if (jwtService.validateToken(jwtToken, userDetails)) {
    // UsernamePasswordAuthenticationToken authenticationToken = new
    // UsernamePasswordAuthenticationToken(userDetails, null,
    // userDetails.getAuthorities());
    // authenticationToken.setDetails(new
    // WebAuthenticationDetailsSource().buildDetails(request));
    // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    // }

    // }
    // filterChain.doFilter(request, response);
    // }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = jwtService.extractTokenFromCookie(request);
        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtService.extractUsername(jwtToken);
            System.out.println("username " + username);

            try {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                if (jwtService.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (UsernameNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // response.getWriter().write("User not found. Please log in again.");
                response.addCookie(new Cookie("jwt", "") {
                    {
                        setMaxAge(0);
                        setPath("/");
                    }
                });
            }
        }
        filterChain.doFilter(request, response);
    }

}
