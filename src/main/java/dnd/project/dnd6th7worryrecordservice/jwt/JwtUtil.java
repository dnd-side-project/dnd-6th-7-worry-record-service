package dnd.project.dnd6th7worryrecordservice.jwt;

import dnd.project.dnd6th7worryrecordservice.dto.user.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Elements.JWT;
import static org.springframework.security.oauth2.jose.jws.JwsAlgorithms.HS256;

@Slf4j
public class   JwtUtil {
    private SecretKey key;
    private Date now = new Date();
    private int accessTokenExpMin = 1800;   //30min
    private int refreshTokenExpMin = 604800;    //7day

    public JwtUtil(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenDto createToken(UserRequestDto userRequestDto) {

        String accessToken = createJws(accessTokenExpMin, userRequestDto);
        String refreshToken = createJws(refreshTokenExpMin, null);

        TokenDto tokens = new TokenDto(accessToken, refreshToken);

        return tokens;
    }

    private String createJws(Integer expMin, UserRequestDto userRequestDto) {


        //Header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", JWT);
        header.put("alg", HS256);

        //Body(Claims)
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "worryrecord");
        claims.put("issueAt", now);
        claims.put("exp", new Date(System.currentTimeMillis() + 1000 * 60 * expMin));
        if (userRequestDto != null) {
            claims.put("kakaoId", userRequestDto.getKakaoId());
            claims.put("username", userRequestDto.getUsername());
            claims.put("email", userRequestDto.getEmail());
            claims.put("imgURL", userRequestDto.getImgURL());
        }

        //Signiture
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;

    }

    public boolean validate(String token) {

        Jws<Claims> jws;

        try {   //유효한 Token일 경우 decode된 내용을 출력
            JwtParserBuilder jpb = Jwts.parserBuilder();
            jpb.setSigningKey(key);
            jws = jpb.build().parseClaimsJws(token);

            System.out.println(jws);
            System.out.println(jws.getBody().getSubject());

            return true;
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
