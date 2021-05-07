package `in`.newdevpoint.ssnodejschat.utility

import java.util.*
import java.util.regex.Pattern

object StringUtilities {
    fun replaceEmailAddressWithStarsInString(text: String): String {
        var finalText = text
        val emails = getEmailAddressesInString(text)
        for (email in emails) {
            finalText = finalText.replace(Pattern.quote(email).toRegex(), GiveStars(email.length))
        }
        return finalText
    }

    private fun getEmailAddressesInString(text: String): ArrayList<String> {
        val emails = ArrayList<String>()
        val m = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(text)
        while (m.find()) {
            emails.add(m.group())
        }
        return emails
    }

    private fun GiveStars(number: Int): String {
        return String(CharArray(number)).replace("\u0000", "*")
    }

    fun replaceMobileWithStarsInString(text: String?): String? {
        var finalText = text
        val emails = getMobileInString(text)
        for (email in emails) {
            val starts = GiveStars(email.length)
            finalText = finalText!!.replace(Pattern.quote(email).toRegex(), starts)
        }
        return finalText
    }

    private fun getMobileInString(text: String?): ArrayList<String> {
        val emails = ArrayList<String>()
        val m = Pattern.compile("(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}").matcher(text)
        while (m.find()) {
            emails.add(m.group())
        }
        return emails
    }
}