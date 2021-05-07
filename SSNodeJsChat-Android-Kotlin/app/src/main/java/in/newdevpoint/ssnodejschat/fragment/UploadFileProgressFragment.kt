package `in`.newdevpoint.ssnodejschat.fragment

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.databinding.FragmentUploadFileBinding
import `in`.newdevpoint.ssnodejschat.model.ChatModel
import `in`.newdevpoint.ssnodejschat.model.UploadFileMode
import `in`.newdevpoint.ssnodejschat.webService.FileUploader
import `in`.newdevpoint.ssnodejschat.webService.ResponseModel
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.*

class UploadFileProgressFragment : Fragment() {
    var fragmentTag: String
    private lateinit var binding: FragmentUploadFileBinding
    private val placeId: String? = null
    private val hotelList: LinearLayout? = null
    private val foodList: LinearLayout? = null
    private val addAdviseList: LinearLayout? = null
    private var uploadFileProgressCallback: UploadFileProgressCallback? = null
    private var file: File? = null
    private var date: Date? = null
    private var messageType: ChatModel.MessageType? = null
    private var messageMeta: HashMap<String, Any>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_file, container, false)
        val view = binding.getRoot()
        //here data must be an instance of the class MarsDataProvider
//		binding.setMarsdata(data);
        binding.progressPercentage.text = "10%"
        binding.progressCancel.setOnClickListener { uploadFileProgressCallback!!.uploadFinished(fragmentTag, null, messageType, date, messageMeta) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressBar.setProgress(10, true)
        } else {
            binding.progressBar.progress = 10
        }
        binding.progressFileName.text = file!!.name
        return view
    }

    fun uploadFiles(file: File, thumb: File?, messageType: ChatModel.MessageType?, messageMeta: HashMap<String, Any>?, uploadFileProgressCallback: UploadFileProgressCallback) {
        this.uploadFileProgressCallback = uploadFileProgressCallback
        this.file = file
        this.messageType = messageType
        date = Date()
        this.messageMeta = messageMeta

//		showProgress("Uploading media ...");
        val fileUploader = FileUploader()
        fileUploader.uploadFiles("file", "file", file, thumb, object : FileUploader.FileUploaderCallback {
            override fun onError() {
//				hideProgress();
                uploadFileProgressCallback.uploadFinished(fragmentTag, null, messageType, date, messageMeta)
            }

            override fun onFinish(data: String?) {
//				hideProgress();

                //					Log.e("RESPONSE " + i, responses[i]);
                val gson = Gson()
                val type = object : TypeToken<ResponseModel<UploadFileMode?>?>() {}.type
                Log.d("Response : ", data!!)
                val obj: ResponseModel<UploadFileMode> = gson.fromJson<ResponseModel<UploadFileMode>>(data, type)
                uploadFileProgressCallback.uploadFinished(fragmentTag, obj.getData(), messageType, date, messageMeta)
            }

            override fun onProgressUpdate(currentpercent: Int, totalpercent: Int) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding!!.progressBar.setProgress(currentpercent, true)
                } else {
                    binding!!.progressBar.progress = currentpercent
                }
                binding!!.progressPercentage.text = "$currentpercent%"
                //				updateProgress(totalpercent, "Uploading file " + filenumber, "");
                Log.e("Progress Status", "$currentpercent $totalpercent")
            }
        }, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjhmYTBiOTcxMTBmM2EzZWQ0ZjI0ZWU1NjQxMmZkMWUwNTJiNmI1ZDM3NmU5MmNiNDgyZGVjMjNkNjk0NDI2NzdjNDFlYzI1Nzc4NjY3MTAyIn0.eyJhdWQiOiIxIiwianRpIjoiOGZhMGI5NzExMGYzYTNlZDRmMjRlZTU2NDEyZmQxZTA1MmI2YjVkMzc2ZTkyY2I0ODJkZWMyM2Q2OTQ0MjY3N2M0MWVjMjU3Nzg2NjcxMDIiLCJpYXQiOjE1ODk4ODg0MTAsIm5iZiI6MTU4OTg4ODQxMCwiZXhwIjoxNjIxNDI0NDA5LCJzdWIiOiIyMCIsInNjb3BlcyI6W119.di40nRlxEKWrJrGqeHb5oOzkAJAqwOokFI1IRdfjRw1usvhM3Q5O2Wop3QtYryjqA5A3NryxYTdl6D1x58zM7CITDeT7wzChw7RYaIm9Nl8Jf1m3AQ6lgJTGp4ZoZ9sUEPQMdPQK163c9rrLYjuxreHglEUr59iS-0dKsIhqak7GzUqX6I-q8IhVuZG1TZ1JQpWds1x2bGUVwHWiylJkq61pp6t7Zbn6-35aCpizDbWjfBY-1KbEldLtlnEmZzHYJZkNskpzMTwZQRKlZNaOdZSDtkm88Awx5vhIQo3ha_YQCr7EklgJUiSyUh2APQ1yy40lIMkEEoA02aajDXhXMpKfTVRQFVAaPpCbwCC4pZVDaHUi0-FhEENW1FE83g6qTK9EV1cjN3UJej8Dm494rkMNI4BNKwyzmew5j5j49BU4VqAi9_JiHNB02K2jhakM_NUn3OAbFRbKCrjbeB1qWffd4j1FoR1HNClNikYd-bsV-Nspe2TTYWaV1wWvX787LwBfa0zqm5b1dNjqCmKlbN8HrfFamS68VhiiqU-NCTgf4StqASQAgVVzJqzxRAersoGpJ8QiRiu8T6umkJkt8mD1OWu6MvJYtBj7MooDmaO5GgbXW9US1GgPiiCE8zNr1-rBVL4-HPgS10g_7WnJt78Yc4GsziqHQGx2SO4x0t4")
    }

    interface UploadFileProgressCallback {
        fun uploadFinished(fragmentTag: String?, data: UploadFileMode?, messageType: ChatModel.MessageType?, date: Date?, messageMeta: HashMap<String, Any>?)
    }

    companion object {
        private const val TAG = "UploadFileProgressFrag:"
    }

    init {
        Log.d(TAG, "UploadFileProgressFragment: ")
        fragmentTag = "Upload" + Date().time
    }
}