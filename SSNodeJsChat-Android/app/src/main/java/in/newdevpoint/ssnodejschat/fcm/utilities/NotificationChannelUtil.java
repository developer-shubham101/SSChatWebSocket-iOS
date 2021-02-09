package in.newdevpoint.ssnodejschat.fcm.utilities;

public class NotificationChannelUtil {

    public static Names DEFAULT_CHANNEL = new Names("default", "This is default channel", "tone_eventually");
    public static Names ALERT_CHANNEL = new Names("alert", "This is alert!", "tone_goes_without_saying");
    public static Names ERROR_CHANNEL = new Names("error", "We found some error in the account!", "tone_got_it_done");

    public static class Names {
        public String id;
        public String name;
        public String tone;

        public Names(String id, String name, String tone) {
            this.id = id;
            this.name = name;
            this.tone = tone;
        }
    }
}
