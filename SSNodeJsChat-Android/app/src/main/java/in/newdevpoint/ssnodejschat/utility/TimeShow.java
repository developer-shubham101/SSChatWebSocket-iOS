package in.newdevpoint.ssnodejschat.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeShow {

    public static String TimeFormatYesterdayToDay(String timeOfMomentUpload) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(timeOfMomentUpload);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

 /*         Log.d("dtaasec",""+TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");
*/
            if (seconds < 60) {
                return seconds + " seconds ago";
            } else if (minutes < 60) {

                return minutes + " minutes ago";
            } else if (hours < 24) {

                return hours + " hours ago";
            } else if (days == 1) {
                return days + " day ago";

            } else if (days < 3) {
                return days + " days ago";
            } else {

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy 'at' hh:mm:ss");
                String strDate = dateFormat.format(past);
                return strDate;
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return "";
    }
}
