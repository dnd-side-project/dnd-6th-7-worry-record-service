package dnd.project.dnd6th7worryrecordservice.jwt;

import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserInfoDto;
import dnd.project.dnd6th7worryrecordservice.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Elements.JWT;
import static org.springframework.security.oauth2.jose.jws.JwsAlgorithms.HS256;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final int accessTokenExpMin = 36000;   //30min
    private final int refreshTokenExpMin = 604800;    //7day
    private final UserDetailsServiceImpl userDetailsService;
    private Date now = new Date();

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public TokenDto createToken(UserInfoDto userInfoDto) {
        String accessToken = createJws(accessTokenExpMin, userInfoDto);
        String refreshToken = createJws(refreshTokenExpMin, null);

        TokenDto tokens = new TokenDto(accessToken, refreshToken);

        return tokens;
    }

    private String createJws(Integer expMin, UserInfoDto userInfoDto) {


        //Header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", JWT);
        header.put("alg", HS256);

        //Body(Claims)
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "worryrecord");
        claims.put("issueAt", now);
        claims.put("exp", new Date(System.currentTimeMillis() + 1000 * 60 * expMin));
        if (userInfoDto != null) {
            claims.put("socialId", userInfoDto.getSocialId());
            claims.put("socialType", userInfoDto.getSocialType().toString());
            claims.put("username", userInfoDto.getUsername());
            claims.put("email", userInfoDto.getEmail());
        }

        //Signiture
        String token = Jwts.builder()
//                .setHeader(header)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;

    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("at-jwt-access-token");
    }

    // Jwt Token에서 SocialId/SocialType 추출
    public String getUserPk(String token) {
        String socialId = Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody().get("socialId").toString();
        String socialType = Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody().get("socialType").toString();

        String value = socialId+"@"+socialType;
        return value;
    }

    // Jwt Token의 유효성 및 만료 기간 검사
    public boolean validateToken(String token) {

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public String decodePayload(String token) {

        String[] splitToken = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(splitToken[1]);

        String decodedString = null;
        try {
            decodedString = new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedString;

    }
}