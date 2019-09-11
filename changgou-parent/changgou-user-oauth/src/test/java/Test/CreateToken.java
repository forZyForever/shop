package Test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package: Test
 * @Author: ZZM
 * @Date: Created in 2019/8/31 10:19
 * @Address:CN.SZ 创建jwt并使用私钥加密
 **/
public class CreateToken {
    @Test
    public void createByToken() {
        //证书文件路径
        String key_location ="changgou.jks";
        //私钥库密码
        String key_password="changgou";
        //私钥密码
        String keypwd="changgou";
        //私钥别名
        String alias="changgou";
        //访问证书路径,读取jks的文件
        ClassPathResource resource=new ClassPathResource(key_location);
        //创建秘钥工厂(spring-security=>rsa)
        //arg0:资源文件(生成的以jks结尾的密钥对文件)
        //arg1:生成密钥对时的私钥库密码,需转化成字节数组
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,key_password.toCharArray());
        //读取密钥对,arg0:私钥别名 arg1:私钥密码
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());
        //获取私钥
        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //自定载荷payload
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "1");
        tokenMap.put("name", "itheima");
        tokenMap.put("roles", "ROLE_VIP,ROLE_USER");
        //生成令牌
        //使用security的api生成,把私钥用rsa算法进行加密
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));
        System.out.println(jwt);
        //取出令牌
        String token = jwt.getEncoded();
        System.out.println(token);
    }
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("zhangsan"));
    }
}
