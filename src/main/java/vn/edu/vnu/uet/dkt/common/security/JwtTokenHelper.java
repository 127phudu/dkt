package vn.edu.vnu.uet.dkt.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenHelper {
    static final long EXPIRATION_TIME = 3600_000; // 10'
    static String SECRET_KEY;
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String ROLE = "role";
    static final String ID = "id";
    static final String USERNAME = "username";
    static final String EMAIL = "email";
    static final String COURSES = "courses";
    static final String FULL_NAME = "fullName";

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${dkt.secret.key}")
    public void setPortalSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public String generateTokenStudent(DktStudent student) {
        String token;
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        student.setRole("Student");
        token = Jwts.builder()
                .claim(ID, student.getId())
                .claim(USERNAME, student.getUsername())
                .claim(EMAIL, student.getEmail())
                .claim(ROLE, student.getRole())
                .claim(FULL_NAME, student.getFullName())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return TOKEN_PREFIX + token;
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        return getAuthentication(token);
    }

    private static Authentication getAuthentication(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) return null;
        Authentication auth = null;
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String role = claims.get(ROLE).toString();
            String id = claims.get(ID).toString();
            String username = claims.get(USERNAME).toString();
            String email = claims.get(EMAIL).toString();
            Collection authorities =
                    Arrays.stream(claims.get(ROLE).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            DktStudent student = DktStudent.builder()
                    .id(Long.parseLong(id))
                    .username(username)
                    .email(email)
                    .role(role)
                    .build();
            auth = new UsernamePasswordAuthenticationToken(student, null, authorities);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return auth;
    }
}
