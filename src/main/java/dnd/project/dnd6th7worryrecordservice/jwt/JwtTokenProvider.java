package dnd.project.dnd6th7worryrecordservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Elements.JWT;
import static org.springframework.security.oauth2.jose.jws.JwsAlgorithms.HS256;

public class JwtTokenProvider {
    private SecretKey key;
    private Date now = new Date();
    private Long accessTokenValidMillisecon = 60000L;
    private Long refreshTokenValidMillisecond = accessTokenValidMillisecon * 60L;

    public JwtTokenProvider(String secret){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(String username, String email, String imgURL) {

        //Header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", JWT);
        header.put("alg", HS256);

        //Body(Claims)
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "worryrecord");
        claims.put("issueAt", now);
        claims.put("exp", new Date(now.getTime() + accessTokenValidMillisecon));
        claims.put("username", username);
        claims.put("name", email);
        claims.put("imgURL", imgURL);

        //Signiture
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public String createRefreshToken(String username, String email) {

        //Header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", JWT);
        header.put("alg", HS256);

        //Body(Claims)
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "worryrecord");
        claims.put("issueAt", now);
        claims.put("exp", new Date(now.getTime() + accessTokenValidMillisecon));
        claims.put("username", username);
        claims.put("name", email);

        //Signiture
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
