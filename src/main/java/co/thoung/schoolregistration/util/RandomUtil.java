package co.thoung.schoolregistration.util;

import java.util.Random;

public class RandomUtil {
    public static String random6Digits(){
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d", number);
    }
}
