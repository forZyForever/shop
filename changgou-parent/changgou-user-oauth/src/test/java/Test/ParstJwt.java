package Test;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/**
 * @Package: Test
 * @Author: ZZM
 * @Date: Created in 2019/8/31 10:48
 * @Address:CN.SZ
 * 使用公钥检验令牌
 **/
public class ParstJwt {
    @Test
    public void testParseJwt(){
        //令牌
       String token= "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTk5OTMxNDYyOCwianRpIjoiODVkMWUzYjQtZDE3Ny00M2VkLTg4YjYtNGRlNGExOGI5ZmQ1IiwiY2xpZW50X2lkIjoic3ppdGhlaW1hIiwidXNlcm5hbWUiOiJzeml0aGVpbWEifQ.bqdKMsLTnEYDc1hJrr5c14LcF9FazahPgzsCbUT6jY3tZRmRRq6wGfRvYC9cPH1B6WEjJwznLmDIsowSVudEOkyVM1fThe38Z5UubuuxXbtYnMRGXNJsyrTI4c7KLOv8JjDVc_6J50W28AT9c7Wln9qXZQNCK0jhVgWJ7hX2HYNTvAdy3ibgHTUMzXuBVrnw67rRrQ957MZS3ZiSXssTZaIjKH21_vtCPhgYGcgxT_qPrSHKW4K6BxqNUpLduTPf0k-GuLhyE07sC7L7s7w_GbPXslxgaR0jnDBjmijyIcFMHN1iv5SzH3_YaAA4xpZiV9qknDU-OhydhJuIxVn7Og";
        //公钥
        String publicKey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnvKEnQEgTNYy8g3QYqqlsYFJ+RyADkk5qljIoUdGb3qi9SFg0JaSTrD9vvMHjNYhVNsqmZ46XwswDbF7AlZ6o//Hx5czoNVtezPzcFE+Hz4gqLnQ3alC7+LQtT0vR7XpNGZKdJMlOkPLqnlTdFLvo2gAOsNtLYf4zzlYZoweZue7gqsWT4PJ+TdFbcSJ0wlCH8VNHqyHosA+7/R0qYWxmsV7c28BFuOAXFYpGL9Wxz1kJrb1DUS48ZxnS9IOSHOIu/x4Rkk2L083RAYfBMlEY+AkKYRg1VlzqMZc3p+0qvuIcqSVAYWqRSA254uQg7nBlvgoxCxf3IuSQkEyLKqFnQIDAQAB-----END PUBLIC KEY-----";
        //检验jwt,公钥通过rsa进行验证
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        String claims = jwt.getClaims();
        System.out.println("载荷为:"+claims);
        String encoded = jwt.getEncoded();
        System.out.println("令牌为:"+encoded);
    }
}
