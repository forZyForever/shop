import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package: PACKAGE_NAME
 * @Author: ZZM
 * @Date: Created in 2019/8/30 10:00
 * @Address:CN.SZ
 **/
public class TestJwT {

    /****
     * 创建Jwt令牌
     */
    @Test
    public void testCreateJwt(){
        JwtBuilder jwtBuilder= Jwts.builder()
                .setId("车子")  //设置id唯一标识
                .setSubject("主题") //设置主题
                .setIssuedAt(new Date()) //设置签发日期
                //.setExpiration(new Date()) //设置有效期
                .signWith(SignatureAlgorithm.HS256,"chelsea");//设置签名
        //自定义数据
        Map<String,Object> map=new HashMap<>();
        map.put("id","1898");
        map.put("name","cech");
        map.put("address","London");
        jwtBuilder.addClaims(map);
        //构建字符串
        System.out.println(jwtBuilder.compact());
    }
    /***
     * 解析Jwt令牌数据
     */
    @Test
    public void testParseJwt(){
        String compactJwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiLovablrZAiLCJzdWIiOiLkuLvpopgiLCJpYXQiOjE1NjcxMzE3NzUsImFkZHJlc3MiOiJMb25kb24iLCJuYW1lIjoiY2VjaCIsImlkIjoiMTg5OCJ9.dmBM2HtQXHc5Tq9q-fezeAmsuIzqhsER0Eivhe9NBgo";
        Claims claims=Jwts.parser()
                .setSigningKey("chelsea")
                .parseClaimsJws(compactJwt)
                .getBody();
        System.out.println(claims);
    }
}
