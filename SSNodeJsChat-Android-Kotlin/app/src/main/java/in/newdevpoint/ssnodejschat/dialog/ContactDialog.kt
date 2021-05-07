package `in`.newdevpoint.ssnodejschat.dialog

import `in`.newdevpoint.ssnodejschat.R
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ContactDialog internal constructor(dialogBuilder: DialogBuilder) : Dialog(dialogBuilder.activity), View.OnClickListener {
    private val dialogBuilder: DialogBuilder?
    var dialogAddContactNew: Button? = null
    private var dialogContactName: TextView? = null
    private var dialogTitle: TextView? = null
    private var dialogAddContactExisting: Button? = null
    private var dialogIcon: ImageView? = null
    fun update() {
        dialogAddContactExisting!!.setOnClickListener(this)
        dialogAddContactNew!!.setOnClickListener(this)
        dialogTitle!!.text = dialogBuilder!!.title
        if (dialogBuilder.resourceId != 0) dialogIcon!!.setImageResource(dialogBuilder.resourceId)
    }

    fun updateContactName() {
        dialogContactName!!.text = dialogBuilder!!.contactNumber
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            window!!.setBackgroundDrawableResource(R.color.transparent)
        }
        setContentView(R.layout.custom_dialog)
        dialogAddContactExisting = findViewById(R.id.dialogAddContactExisting)
        dialogAddContactNew = findViewById(R.id.dialogAddContactNew)
        dialogContactName = findViewById(R.id.dialogContactName)
        dialogTitle = findViewById(R.id.dialogTitle)
        dialogIcon = findViewById(R.id.dialogIcon)
        update()
        updateContactName()
        setCancelable(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dialogAddContactNew -> if (dialogBuilder != null) {
                dialogBuilder.onItemClick!!.addContactNew(this)
            }
            R.id.dialogAddContactExisting -> if (dialogBuilder != null) {
                dialogBuilder.onItemClick!!.close(this)
            }
            else -> {
            }
        }
    }

    interface OnItemClick {
        fun addContactNew(dialog: ContactDialog?)
        fun close(dialog: ContactDialog)
    }

    //Builder Class
    class DialogBuilder(val activity: Activity) {
        var title = "Contact"
            private set
        var onItemClick: OnItemClick? = null
            private set
        var resourceId = 0
        var contactNumber = ""
            private set

        fun setTitle(title: String): DialogBuilder {
            this.title = title
            return this
        }

        fun setContactNumber(contactNumber: String): DialogBuilder {
            this.contactNumber = contactNumber
            return this
        }

        fun setOnItemClick(onItemClick: OnItemClick?): DialogBuilder {
            this.onItemClick = onItemClick
            return this
        }

        fun build(): ContactDialog {
            return ContactDialog(this)
        }
    }

    init {
        this.dialogBuilder = dialogBuilder
    }
}