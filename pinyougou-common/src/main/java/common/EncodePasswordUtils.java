package common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePasswordUtils {

    public static String getPassword(String str) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //加密
        return encoder.encode(str);
    }

    public static void main(String[] args) {
        String apple = EncodePasswordUtils.getPassword("apple");
        System.out.println(apple);
    }
}
