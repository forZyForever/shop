import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Package: PACKAGE_NAME
 * @Author: ZZM
 * @Date: Created in 2019/9/5 11:34
 * @Address:CN.SZ
 **/
public class test {
    public static void main(String[] args) throws ParseException {
        String err_code = "ORDERPAID";
        switch (err_code) {
            //订单已支付情况
            case "ORDERPAID":
                //调用已支付情况
                cte(2);
                break;
      /*      case "ORDERCLOSED":
                throw new RuntimeException("订单已关闭,请勿重复调用");
                break;
            case "SIGNERROR":
                throw new RuntimeException("请检查签名参数和方法是否都符合签名算法要求");
            case "REQUIRE_POST_METHOD":
                throw new RuntimeException("请检查请求参数是否通过post方法提交");
            case "XML_FORMAT_ERROR":
                throw new RuntimeException("请检查XML参数格式是否正确");*/
            default:
                throw new RuntimeException("系统异常，请重新调用该API");

        }
    }
    private static int  cte(int i){
       i+=2;
        System.out.println(i);
       return i;
    }
}
