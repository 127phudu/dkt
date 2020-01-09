package vn.edu.vnu.uet.dkt.common.security;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.stream.Collectors;

@Component
public class JwtTokenHelper {
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static String SECRET_KEY;
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String ROLE = "ROLE";

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${dkt.secret.key}")
    public void setPortalSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public String generateToken(DktStudent student) {
        String token;
        try {
            token = Jwts.builder()
                    .setSubject(mapper.writeValueAsString(student))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
            return TOKEN_PREFIX + token;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
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
            Collection authorities =
                    Arrays.stream(claims.get(ROLE).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            DktStudent student = mapper.readValue(claims.getSubject(), DktStudent.class);
            auth = new UsernamePasswordAuthenticationToken(student,null,authorities);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return auth;
    }
}
