package com.fundoobackendapi.utility;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.fundoobackendapi.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.util.Date;

@Configuration
public class JwtTokenUtil {

    @Autowired
    private RedisTemplateUtility<Object> redis;
    private static final String SECRETE_KEY="fq123mpn";

    public String createToken(long id, long expiryTime) {
        try {
            long currentTime = System.currentTimeMillis();
            Date issueDate = new Date(currentTime);
            Date expireDate = new Date(issueDate.getTime()+expiryTime);
            System.out.println("ExpiryDate "+expireDate);
            Algorithm algorithm = Algorithm.HMAC256(SECRETE_KEY);
            String token = JWT.create().withClaim("id", id).withExpiresAt(expireDate).sign(algorithm);
            redis.putMap("Key",token,String.valueOf(id));
            System.out.println(token);
            return token;
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        }
        return null;
    }
    public Long decodeToken(String token) throws UserException {
        String tokenKey = redis.getMap("Key",token);
        if(tokenKey==null)
            throw new UserException(UserException.exceptionType.WRONG_OR_EXPIRE_TOKEN);
        Verification verification = JWT.require(Algorithm.HMAC256(SECRETE_KEY));
        JWTVerifier jwtverifier = verification.build();
          DecodedJWT decodedjwt = jwtverifier.verify(token);
        Claim claim = decodedjwt.getClaim("id");
        return claim.asLong();
    }
}
