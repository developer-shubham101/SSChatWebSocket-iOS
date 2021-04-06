package in.newdevpoint.ssnodejschat.utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilities {

    public static String replaceEmailAddressWithStarsInString(String text) {
        String finalText = text;
        ArrayList<String> emails = getEmailAddressesInString(text);
        for (String email : emails) {
            finalText = finalText.replaceAll(Pattern.quote(email), GiveStars(email.length()));
        }
        return finalText;
    }

    private static ArrayList<String> getEmailAddressesInString(String text) {
        ArrayList<String> emails = new ArrayList<>();

        Matcher m = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(text);
        while (m.find()) {
            emails.add(m.group());
        }
        return emails;
    }

    private static String GiveStars(int number) {
        return new String(new char[number]).replace("\0", "*");
    }





    public static String replaceMobileWithStarsInString(String text) {
        String finalText = text;
        ArrayList<String> emails = getMobileInString(text);
        for (String email : emails) {
            String starts = GiveStars(email.length());
            finalText = finalText.replaceAll(Pattern.quote(email), starts);
        }
        return finalText;
    }

    private static ArrayList<String> getMobileInString(String text) {
        ArrayList<String> emails = new ArrayList<>();

        Matcher m = Pattern.compile("(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}").matcher(text);
        while (m.find()) {
            emails.add(m.group());
        }
        return emails;
    }

}
