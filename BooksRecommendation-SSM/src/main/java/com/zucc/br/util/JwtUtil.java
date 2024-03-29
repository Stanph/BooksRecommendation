package com.zucc.br.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    /**
     * APP登录Token的生成和解析
     *
     */

    /** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
    public static final String SECRET = "JKKLJOoasdlfj";
    /** token 过期时间: 1天 */
    public static final int CALENDAR_FIELD = Calendar.DATE;
    public static final int CALENDAR_INTERVAL = 1;

    /**
     * JWT生成Token.
     *
     * JWT构成: header, payload, signature
     *
     * @param userid
     *            登录成功后用户userid, 参数userid不可传空
     */
        public static String createToken(Integer userid,String account) throws Exception {
        System.out.println(userid);
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "User")
                .withClaim("userid", userid.toString())
                .withClaim("account",account)
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature

        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
             e.printStackTrace();
            // token 校验失败, 抛出Token验证非法异常
        }
        return jwt.getClaims();
    }

    /**
     * 根据Token获取userid
     *
     * @param token
     * @return userid
     */
    public static Integer getAppUID(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim userIDClaim = claims.get("userid");
        System.out.println("jwt"+userIDClaim.asString());
        if (null == userIDClaim || StringUtils.isEmpty(userIDClaim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
        }
        return (Integer.valueOf(userIDClaim.asString()));
    }
    public static String getAppName(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim nameClaim = claims.get("account");
        System.out.println("jwtac  "+nameClaim.asString());
        if (null == nameClaim || StringUtils.isEmpty(nameClaim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
        }
        return (nameClaim.asString());
    }
}