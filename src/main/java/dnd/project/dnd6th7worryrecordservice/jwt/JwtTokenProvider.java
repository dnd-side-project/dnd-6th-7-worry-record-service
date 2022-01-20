package dnd.project.dnd6th7worryrecordservice.jwt;

import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
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
    private int accessTokenExpMin = 1800;   //30min
    private int refreshTokenExpMin = 604800;    //7day

    public JwtTokenProvider(String secret){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenDto createToken(UserRequestDto userRequestDto) {
        String accessToken = createJws(accessTokenExpMin, userRequestDto);
        String refreshToken = createJws(refreshTokenExpMin, null);

        TokenDto tokens = new TokenDto(accessToken,refreshToken);

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
        if(userRequestDto != null) {
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
        }catch(JwtException e) {
            return false;   //유효하지 않은 Token일 경우 응답으로 false를 return
        }

    }
}
