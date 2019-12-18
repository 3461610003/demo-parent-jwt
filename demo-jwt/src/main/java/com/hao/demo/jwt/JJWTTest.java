package com.hao.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Description: JJWT测试
 * @Author zhenghao
 * @Date 2019/12/18 14:56
 */
public class JJWTTest {
    private static String SECRET = "DyoonSecret_0581";

    private static String getJwtToken(Long userId){
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DATE, 10);
        Date expiresDate = nowTime.getTime(); //有10天有效期
        Claims claims = Jwts.claims();
        claims.put("name","cy");
        claims.put("userId", userId);
        claims.setAudience("cy");
        claims.setIssuer("cy");
        return Jwts.builder().setClaims(claims).setExpiration(expiresDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public static Jws<Claims> parseJwtToken(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        String signature = jws.getSignature();
        Map<String, String> header = jws.getHeader();
        Claims Claims = jws.getBody();
        return jws;
    }

    public static void main(String[] args) {
        String jwtToken = getJwtToken(222L);
        System.out.println("token:" + jwtToken);
        Jws<Claims> claimsJws = parseJwtToken(jwtToken);
        System.out.println("claimsJws:" + claimsJws);
        System.out.println("signature:" + claimsJws.getSignature());
        System.out.println("header:" + claimsJws.getHeader());
        System.out.println("Claims:" + claimsJws.getBody());
    }

}
