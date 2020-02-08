package q.rest.vendor.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class Helper {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";

    private static final String SALT = CHAR_LOWER + CHAR_UPPER + NUMBER;

    public List<Date> getAllDatesBetween(Date from, Date to){
        LocalDate fromLocal = convertToLocalDate(from);
        LocalDate toLocal = convertToLocalDate(to);

        List<LocalDate> localDates = fromLocal.datesUntil(toLocal)
                .collect(Collectors.toList());

        List<Date> dates = new ArrayList<>();
        for(LocalDate ld : localDates){
            dates.add(convertToDate(ld));
        }
        return dates;
    }


    public LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    public Date convertToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static String getRandomString(int length) {
        SecureRandom random = new SecureRandom();
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(SALT.length());
            char rndChar = SALT.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    public static String undecorate(String string) {
        return string.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    public static String getSecuredRandom() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public static String getSecuredRandom(int length) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(length);
    }


    public static String cypher(String text) throws NoSuchAlgorithmException {
        String shaval = "";
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");

        byte[] defaultBytes = text.getBytes();

        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        shaval = hexString.toString();

        return shaval;
    }

    public static int getRandomInteger(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static Date addSeconds(Date original, int seconds) {
        return new Date(original.getTime() + (1000L * seconds));
    }

    public static Date addMinutes(Date original, int minutes) {
        return new Date(original.getTime() + (1000L * 60 * minutes));
    }

    public String getDateFormat(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
        return sdf.format(date);
    }

    public String getDateFormat(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }


}
