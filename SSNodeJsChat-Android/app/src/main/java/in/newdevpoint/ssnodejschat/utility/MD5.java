package in.newdevpoint.ssnodejschat.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String stringToMD5(String planeTXT) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(planeTXT.getBytes(), 0, planeTXT.length());
            StringBuilder sb = new StringBuilder();
            byte[] mdbytes = md.digest();
            for (byte mdbyte : mdbytes) {
                sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String integerToMD5(int planeINT) {
        String planeTXT = Integer.toString(planeINT);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(planeTXT.getBytes(), 0, planeTXT.length());
            StringBuilder sb = new StringBuilder();
            byte[] mdbytes = md.digest();
            for (byte mdbyte : mdbytes) {
                sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
