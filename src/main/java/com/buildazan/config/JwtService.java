package com.buildazan.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtService {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(UserDetailsImpl userDetailsImpl){

        Map<String, Object> claims = new HashMap<>();
        claims.put("emailVerified", userDetailsImpl.isEmailVerified());
        claims.put("memberShipLevel", userDetailsImpl.getMemberShipLevel());
        claims.put("subscriptionStatus", userDetailsImpl.getSubscriptionStatus());

        return Jwts.builder()
        .subject(userDetailsImpl.getUsername())
        .claims(claims)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24 * 3))  // valid : 3 days
        .signWith(getSignKey(), Jwts.SIG.HS256)
        .compact();
    }

    public void setTokenCookies(HttpServletResponse response, String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(259200); //  max-age : 3 days
        response.addCookie(cookie);
    }

    public String extractTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
            for(Cookie cookie : cookies){
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean extractEmailVerified(String token){
        return extractClaim(token, claims -> claims.get("emailVerified", Boolean.class));
    }

    public SubscriptionStatus extracSubscriptionStatus(String token){
        return extractClaim(token, claims -> claims.get("subscriptionStatus", SubscriptionStatus.class));
    }

    public MemberShipLevel extractMemberShipLevel(String token){
        return extractClaim(token, claim -> claim.get("memberShipLevel", MemberShipLevel.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    
}
