package com.searchengine.springboot;

import com.searchengine.dao.RecordSegDao;
import com.searchengine.utils.RedisUtil_db1;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private RecordSegDao recordSegDao;

    @Autowired
    private RedisUtil_db1 redisUtilDb1;

    @Test
    void contextLoads() {
//        RecordSeg recordSeg = new RecordSeg();
//        Long l = new Long(1);
//        recordSeg.setDataId(l);
//        recordSeg.setSegId(l);
//        recordSeg.setCount(2);
//        recordSegDao.updateRecordSeg(recordSeg);

    }

    @Test
    void testRedis(){
        redisUtilDb1.set("name", "joker");
    }

    private String signature = "default";

    @Test
    void testJWT(){
        long time = 1000 * 60 * 30; //30分钟
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwt_token = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("username", "tom")
                .claim("role", "default")
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();

        System.out.println(jwt_token);
    }

    @Test
    void testJWT_out(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJkZWZhdWx0Iiwic3ViIjoidGVzdCIsImV4cCI6MTY1Mjc2NDk4MiwianRpIjoiYWI2MDA3ZGYtZDk4MS00YWM1LTkwNDQtYzM1NmU0YmU5MGRhIn0.esaYAGup7508n1s_MvoBd0GKal__n1OqYsbEUtnXMt8";
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        System.out.println(claims.get("username"));
        System.out.println(claims.get("role"));
        System.out.println(claims.getId());
    }

    @Test
    void testBCryPassword(){
        System.out.println(UUID.randomUUID().toString().replaceAll("-","").length());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode1 = passwordEncoder.encode("qwe456789145635");
        System.out.println(encode1.length());
        System.out.println("$2a$10$WMN4OxC3mv6yfAfkwocqAuL3p5PRraTK4u46vTZPXvZZi96XqV34K".length());
    }

}
