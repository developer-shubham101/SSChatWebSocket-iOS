package `in`.newdevpoint.ssnodejschat.model

import java.util.*

class ContactModel {
    var isSelected = false
    var first_name: String? = null
    var middle_name: String? = null
    var last_name: String? = null
    var mobile: String? = null

    constructor(first_name: String?, middle_name: String?, last_name: String?, mobile: String?) {
        this.first_name = first_name
        this.middle_name = middle_name
        this.last_name = last_name
        this.mobile = mobile
    }

    constructor() {}

    val name: String
        get() = "$first_name $last_name"
    val list: HashMap<String, Any?>
        get() {
            val tmpList = HashMap<String, Any?>()
            tmpList["first_name"] = first_name
            tmpList["middle_name"] = middle_name
            tmpList["last_name"] = last_name
            tmpList["mobile"] = mobile
            return tmpList
        }
}