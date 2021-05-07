package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.AppApplication
import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.activity.ChatActivity
import `in`.newdevpoint.ssnodejschat.adapter.ChatAdapter
import `in`.newdevpoint.ssnodejschat.adapter.HeaderDataImpl
import `in`.newdevpoint.ssnodejschat.databinding.ActivityChatBinding
import `in`.newdevpoint.ssnodejschat.dialog.ContactDialog
import `in`.newdevpoint.ssnodejschat.fragment.PlayAudioFragment
import `in`.newdevpoint.ssnodejschat.fragment.UploadFileProgressFragment
import `in`.newdevpoint.ssnodejschat.model.*
import `in`.newdevpoint.ssnodejschat.observer.ResponseType
import `in`.newdevpoint.ssnodejschat.observer.WebSocketObserver
import `in`.newdevpoint.ssnodejschat.observer.WebSocketSingleton
import `in`.newdevpoint.ssnodejschat.stickyheader.stickyView.StickHeaderItemDecoration
import `in`.newdevpoint.ssnodejschat.utility.*
import `in`.newdevpoint.ssnodejschat.utility.FileUtils.saveVideoToInternalStorage
import `in`.newdevpoint.ssnodejschat.utility.Utils.blurRenderScript
import `in`.newdevpoint.ssnodejschat.utility.Utils.getImageString
import `in`.newdevpoint.ssnodejschat.webService.APIClient
import `in`.newdevpoint.ssnodejschat.webService.ResponseModel
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.MediaRecorder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.*
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.downloader.OnDownloadListener
import com.downloader.request.DownloadRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.theartofdev.edmodo.cropper.CropImage
import org.apache.commons.io.FilenameUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class ChatActivity : AppCompatActivity(), View.OnClickListener,
        PermissionClass.PermissionRequire,
        UploadFileProgressFragment.UploadFileProgressCallback,
        PlayAudioFragment.PlayAudioCallback, WebSocketObserver {
    private val chatListTmp = ArrayList<ChatModel>()
    private val chatList = HashMap<Date, ArrayList<ChatModel>>()
    private val uploadFragment: PlayAudioFragment = PlayAudioFragment()
    var mStartRecording = true
    private var isBlocked = false
    private lateinit var binding: ActivityChatBinding
    private lateinit var permissionClass: PermissionClass
    private var recorder: MediaRecorder? = null
    private lateinit var adapter: ChatAdapter
    private var startTime: Long = 0
    private var timeBuff: Long = 0
    private var updateTime = 0L //millisecondTime,
    private var handler: Handler? = null
    private var seconds = 0
    private var minutes = 0
    private var hours = 0
    var runnable: Runnable = object : Runnable {
        override fun run() {
            seconds = (timeBuff + System.currentTimeMillis() / 1000 - startTime).toInt()
            hours = seconds / 3600
            minutes = seconds / 60
            seconds = seconds % 60
            binding.chatRecordingTime.text = "" + hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
            handler!!.postDelayed(this, 0)
        }
    }
    private var applyBlur = false
    private var isSetWallpaper = false
    private var _roomId: String? = null
    private var _isGroup = false
    private var _groupDetails: FSGroupModel? = null
    private lateinit var _senderDetails: FSUsersModel
    private var noOfNewMessages = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        // Record to the external cache directory for visibility
        fileName = externalCacheDir!!.absolutePath
        fileName += "/audiorecordtest.m4a"
        handler = Handler(Looper.getMainLooper())
        binding = DataBindingUtil.setContentView(this@ChatActivity, R.layout.activity_chat)
        permissionClass = PermissionClass(this, this)
        binding.attachmentWrapper.visibility = View.GONE
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding.sendButton.setOnClickListener(this)
        binding.chatAttachment.setOnClickListener(this)
        binding.attachmentDoc.setOnClickListener(this)
        binding.attachmentCamera.setOnClickListener(this)
        binding.attachmentGallery.setOnClickListener(this)
        binding.attachmentLocation.setOnClickListener(this)
        binding.attachmentContact.setOnClickListener(this)
        binding.chatStartRecording.setOnClickListener(this)
        binding.sendAudioButton.setOnClickListener(this)
        binding.openRecorder.setOnClickListener(this)
        binding.chatCloseRecording.setOnClickListener(this)
        binding.chatGoToBottom.setOnClickListener(this)
        binding.chatUnblockBtn.setOnClickListener(this)
        adapter = setUpRecyclerView()
        binding.audioPlayerFragment.visibility = View.GONE
        binding.chatGoToBottom.visibility = View.GONE
        setAudioPlayer()
        addChatMenu()
        val wallpaperFilePath: String = DownloadUtility.createPath(applicationContext, DownloadUtility.FILE_PATH_WALLPAPER) + "/wallpaper.jpg"
        val wallpaperFile = File(wallpaperFilePath)
        if (wallpaperFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(wallpaperFilePath)
            binding.chatBgImage.setImageBitmap(myBitmap)
        }
        parseExtras()
        WebSocketSingleton.getInstant()?.register(this)
    }

    private fun parseExtras() {
        _roomId = intent.getStringExtra(INTENT_EXTRAS_KEY_ROOM_ID)
        _isGroup = intent.getBooleanExtra(INTENT_EXTRAS_KEY_IS_GROUP, false)
        _groupDetails = intent.getSerializableExtra(INTENT_EXTRAS_KEY_GROUP_DETAILS) as FSGroupModel?
        //        _senderDetails = (FSUsersModel) getIntent().getSerializableExtra(ChatActivity.INTENT_EXTRAS_KEY_SENDER_DETAILS);
        val tmpSenderDetails = intent.getSerializableExtra(INTENT_EXTRAS_KEY_SENDER_DETAILS)
        if (tmpSenderDetails != null) {
            _senderDetails = tmpSenderDetails as FSUsersModel
            addChatMenu()
            if (_isGroup) {
                setGroupDetails()
            } else {
                setIndividualDetails()
            }
            joinCommand()
            blockList
        } else {
            roomInfo
        }
    }

    private fun setIndividualDetails() {
        binding.chatChatWithUserName.text = _senderDetails.name
        binding.chatChatWithUserStatus.text = if (_senderDetails.isOnline) "Online" else _senderDetails.lastSeen
        Glide.with(this)
                .setDefaultRequestOptions(AppApplication.USER_PROFILE_DEFAULT_GLIDE_CONFIG)
                .load(getImageString(_senderDetails.profile_image))
                .into(binding.chatUserProfile)
    }

    private fun setGroupDetails() {
        binding.chatChatWithUserName.text = _groupDetails?.group_name
        binding.chatChatWithUserStatus.text = _groupDetails?.about_group
        //        Glide.with(this).setDefaultRequestOptions(userProfileDefaultOptions).load(groupInfo.getRoomImage()).into(binding.chatUserProfile);
    }

    private val roomInfo: Unit
        private get() {
            val messageMap = HashMap<String?, Any?>()
            messageMap["type"] = "roomsDetails"
            messageMap["roomId"] = _roomId
            messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] = APIClient.KeyConstant.REQUEST_TYPE_ROOM
            WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
        }

    fun addChatMenu() {
        binding.chatMenu.setOnClickListener { v: View? ->
            val popup = PopupMenu(this@ChatActivity, binding.chatMenu)
            //inflating menu from xml resource
            popup.inflate(R.menu.chat_menu)
            if (isBlocked) {
                popup.menu.getItem(0).title = "Un-mute"
            } else {
                popup.menu.getItem(0).title = "Mute"
            }
            //adding click listener
            popup.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.chatMute ->                         //IF is single user chat then check some other user details and set it
                        if (!_isGroup) {
                            isBlocked = !isBlocked
                            // TODO: 27/01/21 Block Code
                            blockOrUnblock(isBlocked)
                        }
                    R.id.changeWallpaper -> {
                        val popup1 = PopupMenu(this@ChatActivity, binding.chatMenu)
                        //inflating menu from xml resource
                        popup1.inflate(R.menu.change_chat_wallpaper_menu)

                        //adding click listener
                        popup1.setOnMenuItemClickListener { item1: MenuItem ->
                            val displayMetrics = DisplayMetrics()
                            windowManager.defaultDisplay.getMetrics(displayMetrics)
                            val height = displayMetrics.heightPixels
                            val width = displayMetrics.widthPixels
                            when (item1.itemId) {
                                R.id.changeWallpaperDefault -> {
                                    val wallpaperFilePath: String = DownloadUtility.createPath(applicationContext, DownloadUtility.FILE_PATH_WALLPAPER) + "/wallpaper.jpg"
                                    val wallpaperFile = File(wallpaperFilePath)
                                    if (wallpaperFile.exists()) {
                                        wallpaperFile.delete()
                                        binding.chatBgImage.setImageResource(R.drawable.bg_chat)
                                    }
                                }
                                R.id.changeWallpaperWithOutBlur -> {
                                    applyBlur = false
                                    isSetWallpaper = true
                                    CropImage.activity().setFixAspectRatio(true).setAspectRatio(width, height).start(this@ChatActivity)
                                }
                                R.id.changeWallpaperWithBlur -> {
                                    applyBlur = true
                                    isSetWallpaper = true
                                    CropImage.activity().setFixAspectRatio(true).setAspectRatio(width, height).start(this@ChatActivity)
                                }
                            }
                            false
                        }
                        //displaying the popup
                        popup1.show()
                    }
                }
                false
            }
            //displaying the popup
            popup.show()
        }
    }

    private fun blockOrUnblock(isBlocked: Boolean) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("type", "blockUser")
            jsonObject.put("blockedBy", UserDetails.myDetail.id)
            jsonObject.put("blockedTo", _senderDetails.id)
            jsonObject.put("isBlock", isBlocked)
            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_BLOCK_USER)
            WebSocketSingleton.getInstant()?.sendMessage(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setAudioPlayer() {
        uploadFragment.setPlayAudioCallback(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.audioPlayerFragment, uploadFragment)
        ft.commit()
    }

    @Throws(JSONException::class)
    private fun appendMessage(chatData: JSONObject, showMessageCount: Boolean) {
        val senderDetails: FSUsersModel = UserDetails.chatUsers.get(chatData.getString("sender_id"))!!
        val chatModel = ChatModel(chatData, senderDetails)
        if (chatModel.roomId == _roomId) {
            chatListTmp.add(chatModel)
            val tempDate = chatModel.createdDate
            var correspondingChatList = chatList[tempDate]
            if (correspondingChatList == null) {
                correspondingChatList = ArrayList()
                chatList[tempDate] = correspondingChatList
            }
            correspondingChatList.add(chatModel)
            Collections.sort(correspondingChatList) { o1: ChatModel, o2: ChatModel -> o1.messageDate.compareTo(o2.messageDate) }
            val keys = ArrayList(chatList.keys)
            Collections.sort(keys)
            adapter.clearAll()
            for (key in keys) {
                val chatForThatDay = chatList[key]!!
                //										HeaderDataImpl headerData1 = new HeaderDataImpl(R.layout.header1_item_recycler, key);
                val headerData1 = HeaderDataImpl(R.layout.header1_item_recycler, key)
                adapter.setHeaderAndData(chatForThatDay as List<ChatModel?>, headerData1)
            }
            if (showMessageCount && chatModel.sender_detail.id != UserDetails.myDetail.id) {
                noOfNewMessages += 1
                binding.chatGoToBottom.visibility = View.VISIBLE
                binding.chatNewMessageCount.text = Integer.toString(noOfNewMessages)
                if (noOfNewMessages <= 1) {
                    val layout = binding.chatRecyclerView.layoutManager as LinearLayoutManager?
                    layout!!.scrollToPosition(layout.findLastVisibleItemPosition() + 1)
                }
            }
            if (chatModel.sender_detail.id == UserDetails.myDetail.id) {
                binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)


                /* int offset = binding.chatRecyclerView.computeVerticalScrollOffset();
                int extent = binding.chatRecyclerView.computeVerticalScrollExtent();
                int range = binding.chatRecyclerView.computeVerticalScrollRange();

                float percentage = (100.0f * offset / (float)(range - extent));
                Log.d(TAG, "appendMessage: " + percentage);*/
//                ((LinearLayoutManager) binding.chatRecyclerView.getLayoutManager()).scrollToPositionWithOffset(2, 20);
            }
        }
    }

    private fun resetNewMessageCount() {
        noOfNewMessages = 0
        binding.chatGoToBottom.visibility = View.GONE
        binding.chatNewMessageCount.text = Integer.toString(noOfNewMessages)
    }

    private fun setUpRecyclerView(): ChatAdapter {
        val adapter = ChatAdapter(this, object : ChatAdapter.ChatCallbacks {
            override fun onClickDownload(chatModel: ChatModel?, messageContent: MediaModel?, stopDownloading: Boolean, onDownloadListener: OnDownloadListener?) {
                if (chatModel!!.message_type == ChatModel.MessageType.image) {
                    val intent = Intent(this@ChatActivity, ZoomImageActivity::class.java)
                    intent.putExtra(ZoomImageActivity.INTENT_EXTRA_URL, messageContent?.file_url)
                    startActivity(intent)
                } else {
                    downloadFile(chatModel, messageContent, stopDownloading, onDownloadListener)
                }
            }

            override fun onClickLocation(chatModel: ChatModel?, locationModel: LocationModel?) {
                val geoUri = "http://maps.google.com/maps?q=loc:" + locationModel!!.latitude + "," + locationModel.longitude + " (" + locationModel.name + ")"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                startActivity(intent)
            }

            override fun onClickContact(chatModel: ChatModel?, contactModel: ContactModel?) {
                val dialogBuilder: ContactDialog.DialogBuilder = ContactDialog.DialogBuilder(this@ChatActivity).setOnItemClick(object : ContactDialog.OnItemClick {
                    override fun addContactNew(dialog: ContactDialog?) {
                        /*	Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
						contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

						contactIntent
								.putExtra(ContactsContract.Intents.Insert.NAME, contactModel.getFirst_name() + " " + contactModel.getLast_name())
								.putExtra(ContactsContract.Intents.Insert.PHONE, contactModel.getMobile());

						startActivityForResult(contactIntent, REQUEST_ADD_CONTACT);*/
                        val contactIntent = Intent(Intent.ACTION_INSERT_OR_EDIT)
                        contactIntent.type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
                        contactIntent
                                .putExtra(ContactsContract.Intents.Insert.NAME, contactModel?.first_name + " " + contactModel?.last_name)
                                .putExtra(ContactsContract.Intents.Insert.PHONE, contactModel?.mobile)
                        startActivityForResult(contactIntent, REQUEST_ADD_CONTACT)
                    }

                    override fun close(dialog: ContactDialog) {
                        dialog.cancel()
                    }
                }).setContactNumber(contactModel?.mobile!!).setTitle(contactModel.first_name + " " + contactModel.last_name)
                val dialog: ContactDialog = dialogBuilder.build()
                val display = windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width = (size.x * 0.90).toInt()
                //        int height = size.y;
                dialog.show()
                dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            }

            override fun onLongClick(chatModel: ChatModel?) {
                Toast.makeText(this@ChatActivity, "On long click ", Toast.LENGTH_SHORT).show()
            }
        })
        val layoutManager = LinearLayoutManager(this)

//		setData(adapter);
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = layoutManager
        binding.chatRecyclerView.addItemDecoration(StickHeaderItemDecoration(adapter))
        binding.chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                Log.d(TAG, "onScrolled: " + dx + " " + dy);
//                if (dy > 0) { //check for scroll down
                val layout = binding.chatRecyclerView.layoutManager as LinearLayoutManager?
                if (layout!!.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    resetNewMessageCount()
                }


//                layout.scrollToPosition();

//                Log.d(TAG, "onScrolled: " + layout.findLastVisibleItemPosition() + " " + adapter.getItemCount());

//                }
            }
        })
        return adapter
    }

    private fun downloadFile(chatModel: ChatModel?, appListModel: MediaModel?, stopDownloading: Boolean, onDownloadListener: OnDownloadListener?) {
        val downloadUrl: String = appListModel!!.file_url
        try {
            val url = URL(downloadUrl)
            val downloadFileName = FilenameUtils.getName(url.path)
            val downloadFile: File = File(DownloadUtility.getPath(applicationContext, DownloadUtility.FILE_PATH_CHAT_FILES) + "/" + downloadFileName)
            val isAppDownloaded = downloadFile.exists()
            if (isAppDownloaded) {
                chatModel!!.downloadStatus = ChatModel.DownloadStatus.DOWNLOADED
                val filePath = downloadFile.absolutePath
                if (FileOpenUtility.isVideo(filePath)) {
                    val intent = Intent(this, PlayerActivity::class.java)
                    intent.putExtra(PlayerActivity.INTENT_EXTRA_FILE_PATH, filePath)
                    startActivity(intent)
                } else if (FileOpenUtility.isAudio(filePath)) {
                    binding.audioPlayerFragment.visibility = View.VISIBLE
                    uploadFragment.play(filePath)
                } else {
                    FileOpenUtility.openFile(this@ChatActivity, filePath)
                }
            } else {
                val task: DownloadRequest? = DownloadUtility.downloadList.get(MD5.stringToMD5(downloadUrl))
                if (task == null) {
                    DownloadUtility.downloadFile(applicationContext,
                            DownloadUtility.getPath(
                                    applicationContext,
                                    DownloadUtility.FILE_PATH_CHAT_FILES),
                            downloadUrl, downloadFileName,
                            MD5.stringToMD5(downloadUrl),
                            onDownloadListener)
                    chatModel!!.downloadStatus = ChatModel.DownloadStatus.DOWNLOADING
                } else {
                    if (stopDownloading) {
                        task.cancel()
                        DownloadUtility.downloadList.remove(MD5.stringToMD5(downloadUrl))
                        chatModel!!.downloadStatus = ChatModel.DownloadStatus.PENDING
                    }
                }
            }
            adapter.notifyDataSetChanged()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        WebSocketSingleton.getInstant()?.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        WebSocketSingleton.getInstant()?.unregister(this)
        //        webSocket.cancel();
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
        WebSocketSingleton.getInstant()?.unregister(this)

//        mUserRef.child(currentUser.getUid()).child("isOnline").setValue(false);
    }

    private fun showPicVideoDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
                "Select video from gallery",
                "Record video from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> chooseVideoFromGallery()
                1 -> takeVideoFromCamera()
            }
        }
        pictureDialog.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.chatUnblockBtn -> {
                blockOrUnblock(false)
            }
            R.id.chatGoToBottom -> {
                binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                resetNewMessageCount()
            }
            R.id.sendButton -> {
                if (!binding.messageArea.text.toString().isEmpty()) {
                    val message = binding.messageArea.text.toString()
                    val emailFilteredString: String = StringUtilities.replaceEmailAddressWithStarsInString(message)
                    val mobileFilteredString: String = StringUtilities.replaceMobileWithStarsInString(emailFilteredString)!!
                    sendMessage(mobileFilteredString, ChatModel.MessageType.text, HashMap())
                    binding.messageArea.setText("")
                }
            }
            R.id.openRecorder -> {
                binding.chatMessageWrapper.visibility = View.GONE
                binding.chatRecordingWrapper.visibility = View.VISIBLE
            }
            R.id.chatAttachment -> {
                toggleAttachmentWrapper()
            }
            R.id.chatCloseRecording -> {
                stopRecording()
                mStartRecording = true
                binding.chatMessageWrapper.visibility = View.VISIBLE
                binding.chatRecordingWrapper.visibility = View.GONE
            }
            R.id.attachmentGallery -> {
                toggleAttachmentWrapper()
                binding.attachmentWrapper.visibility = View.GONE
                permissionClass.askPermission(REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO)
            }
            R.id.attachmentCamera -> {
                toggleAttachmentWrapper()
                binding.attachmentWrapper.visibility = View.GONE
                permissionClass.askPermission(REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE)
            }
            R.id.chatStartRecording -> {
                permissionClass.askPermission(REQUEST_RECORD_AUDIO_PERMISSION)
            }
            R.id.sendAudioButton -> {
                sendRecording()
            }
            R.id.attachmentContact -> {
                toggleAttachmentWrapper()
                contacts
            }
        }
    }

    private fun toggleAttachmentWrapper() {
        binding.attachmentWrapper.visibility = if (binding.attachmentWrapper.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private val contacts: Unit
        private get() {
            val intent = Intent(this, ContactListActivity::class.java)
            startActivityForResult(intent, REQUEST_SELECT_CONTACTS)
        }

    private fun sendRecording() {
        val audio = File(fileName)
        val fileMeta = HashMap<String, Any>()
        fileMeta[MediaMetaModel.KEY_FILE_TYPE] = MediaMetaModel.MediaType.audioM4A.toString()
        fileMeta[MediaMetaModel.KEY_FILE_NAME] = audio.name
        if (audio.exists()) {
            addFragment(audio, null, ChatModel.MessageType.document, fileMeta)
        }
    }

    private fun sendMessage(message: String, messageType: ChatModel.MessageType?, messageContent: HashMap<String, Any?>) {
        val messageMap = HashMap<String?, Any?>()
        messageMap["type"] = "addMessage"
        messageMap["roomId"] = _roomId
        messageMap["room"] = _roomId
        messageMap["message"] = message
        messageMap["message_type"] = messageType.toString()
        //        messageMap.put("sender_id", UserDetails.myDetail.getId());
        messageMap["sender_id"] = UserDetails.myDetail.id
        messageMap["receiver_id"] = "12312faa"
        messageMap["message_content"] = messageContent
        messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] = APIClient.KeyConstant.REQUEST_TYPE_MESSAGE
        //        messageMap.put("time", time);

        // TODO: 27/01/21 SendMessage
//        chatReference.add(messageMap);
        WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
        Timer().schedule(
                object : TimerTask() {
                    override fun run() {
                        binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                        // your code here
                    }
                },
                500
        )


//        binding.chatRecyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
//        binding.chatRecyclerView.setHasFixedSize(true);
    }

    private fun chooseVideoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takeVideoFromCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri?
                if (result != null) {
                    resultUri = result.uri
                    if (resultUri != null) {
                        if (isSetWallpaper) {
                            setWallpaper(resultUri)
                        } else {
                            uploadImageFormUri(resultUri)
                        }
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception
                if (result != null) {
                    error = result.error
                    error.printStackTrace()
                }
            }
        } else if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                contentURI?.let { uploadVideoFormUri(it) }
            }
        } else if (requestCode == CAMERA) {
            if (data != null) {
                val contentURI = data.data
                contentURI?.let { uploadVideoFormUri(it) }
            }
        } else if (requestCode == REQUEST_SELECT_CONTACTS && resultCode == RESULT_OK) {
            if (data != null) {
                val extras = data.extras
                if (extras != null) {
                    val contacts = extras.getString(ContactListActivity.INTENT_SELECTED_CONTACTS)
                    val gson = Gson()
                    val type = object : TypeToken<ArrayList<ContactModel?>?>() {}.type
                    Log.d("ContactList: ", contacts!!)
                    val contactList: ArrayList<ContactModel> = gson.fromJson(contacts, type)
                    for (element in contactList) {
                        sendMessage("", ChatModel.MessageType.contact, element.list)
                    }
                    println(contactList)
                }
            }
        }
    }

    private fun uploadImageFormUri(resultUri: Uri) {
        val imagePath = resultUri.path
        try {
            val thumbnailFile = File(cacheDir, "image.jpg")
            thumbnailFile.createNewFile()
            thumbnailFile.exists()
            val bitmap: Bitmap?
            bitmap = if (Build.VERSION.SDK_INT <= 29) {
                ThumbnailUtils.createImageThumbnail(imagePath!!, MediaStore.Images.Thumbnails.MINI_KIND)
            } else {
                // TODO: 4/17/2020 here we will do code for crete thumnail for latest api version 29 bcoz createVideoThumbnail is depricate for this version
                val signal = CancellationSignal()
                val size = Size(100, 100)
                val file = File(imagePath)
                ThumbnailUtils.createImageThumbnail(file,
                        size, signal)
            }
            val bos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos)
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(thumbnailFile)
                val file = File(imagePath)
                if (file.exists()) {
                    val fileMeta = HashMap<String, Any>()
                    fileMeta[MediaMetaModel.KEY_FILE_TYPE] = MediaMetaModel.MediaType.imageJPG.toString()
                    addFragment(file, thumbnailFile, ChatModel.MessageType.image, fileMeta)
                    Log.d(TAG, "onActivityResult: $file")
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (fos != null) {
                fos.write(bitmapData)
                fos.flush()
                fos.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setWallpaper(resultUri: Uri) {
        val imagePath = resultUri.path
        try {
            val file = File(imagePath)
            if (file.exists()) {
                val wallpaperFilePath: String = DownloadUtility.createPath(applicationContext, DownloadUtility.FILE_PATH_WALLPAPER) + "/wallpaper.jpg"
                val wallpaperFile = File(wallpaperFilePath)
                if (wallpaperFile.exists()) {
                    wallpaperFile.delete()
                }
                val srcBitmap = BitmapFactory.decodeFile(imagePath)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (applyBlur) {
                        val blurred = blurRenderScript(this@ChatActivity, srcBitmap, 25)
                        try {
                            FileOutputStream(wallpaperFilePath).use { out ->
                                blurred.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            DownloadUtility.copy(file, wallpaperFile)
                        }
                    } else {
                        DownloadUtility.copy(file, wallpaperFile)
                    }
                } else {
                    DownloadUtility.copy(file, wallpaperFile)
                }
                if (wallpaperFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(wallpaperFilePath)
                    binding.chatBgImage.setImageBitmap(myBitmap)
                }
                Log.d(TAG, "onActivityResult: $file")
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun uploadVideoFormUri(contentURI: Uri) {
        val selectedVideoPath = getPath(contentURI)
        Log.d("path", selectedVideoPath!!)
        saveVideoToInternalStorage(selectedVideoPath)
        try {
            val thumbnailFile = File(cacheDir, "image.jpg")
            thumbnailFile.createNewFile()
            //			thumbnailFile.exists();
            Log.d(TAG, "onActivityResult: " + thumbnailFile.exists())
            //Convert bitmap to byte array
            try {
                var bitmap: Bitmap? = null
                bitmap = if (Build.VERSION.SDK_INT <= 29) {
                    ThumbnailUtils.createVideoThumbnail(selectedVideoPath,
                            MediaStore.Images.Thumbnails.MINI_KIND)
                } else {
                    // TODO: 4/17/2020 here we will do code for crete thumbnail for latest api version 29 because createVideoThumbnail is deprecated for this version
                    val signal = CancellationSignal()
                    val size = Size(100, 100)
                    val file = File(selectedVideoPath)
                    ThumbnailUtils.createVideoThumbnail(file, size, signal)
                }
                val bos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos)
                val bitMapData = bos.toByteArray()

                //write the bytes in file
                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(thumbnailFile)
                    val file = File(selectedVideoPath)
                    if (file.exists()) {
                        val fileMeta = HashMap<String, Any>()
                        fileMeta[MediaMetaModel.KEY_FILE_TYPE] = MediaMetaModel.MediaType.videoMP4.toString()
                        addFragment(file, thumbnailFile, ChatModel.MessageType.video, fileMeta)
                        Log.d(TAG, "onActivityResult: $file")
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    if (fos != null) {
                        fos.write(bitMapData)
                        fos.flush()
                        fos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun addFragment(file: File, thumb: File?, messageType: ChatModel.MessageType, messageMeta: HashMap<String, Any>) {
        val ft = supportFragmentManager.beginTransaction()
        val uploadFragment = UploadFileProgressFragment()
        ft.add(R.id.uploadFileWrapper, uploadFragment, uploadFragment.fragmentTag)
        ft.commit()
        uploadFragment.uploadFiles(file, thumb, messageType, messageMeta, this)
    }

    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    override fun permissionDeny() {}
    private fun selectCameraImage() {
//		CropImage.activity().start(this, AddPostBottomSheetFragment.this);
        isSetWallpaper = false
        CropImage.activity().start(this)

//		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//		galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//		startActivityForResult(galleryIntent, GALLERY);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionClass.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onRecord(start: Boolean) {
        if (start) {
            startRecording()
        } else {
            stopRecording()
        }
    }

    private fun stopRecording() {
        if (recorder != null) {
            recorder!!.stop()
            recorder!!.release()
            recorder = null
        }
        resetTimer()
    }

    private fun startRecording() {
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setOutputFile(fileName)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder!!.prepare()
            startTime = System.currentTimeMillis() / 1000
            startTimer()
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed")
        }
        recorder!!.start()
    }

    private fun startTimer() {
        handler!!.postDelayed(runnable, 1000)
    }

    private fun resetTimer() {
//        millisecondTime = 0L;
        startTime = 0L
        timeBuff = 0L
        updateTime = 0L
        seconds = 0
        minutes = 0
        handler!!.removeCallbacks(runnable)
        binding.chatRecordingTime.text = "00:00:00"
    }

    override fun permissionGranted(flag: Int) {
        when (flag) {
            REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE -> selectCameraImage()
            REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO -> showPicVideoDialog()
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                onRecord(mStartRecording)
                if (mStartRecording) {
                    binding.chatStartRecording.text = "Stop recording"
                } else {
                    binding.chatStartRecording.text = "Start recording"
                }
                mStartRecording = !mStartRecording
            }
        }
    }

    override fun listOfPermission(flag: Int): Array<String> {
        if (flag == REQUEST_RECORD_AUDIO_PERMISSION) {
            return arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO)
        } else if (flag == REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE || flag == REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO) {
            return arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            )
        }
        return arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
    }

    override fun uploadFinished(fragmentTag: String?, data: UploadFileMode?, messageType: ChatModel.MessageType?, date: Date?, messageData: HashMap<String, Any>?) {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        if (fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
            if (data != null) {
                if (data.thumbnail != null && !data.thumbnail.isEmpty()) {
                    messageData!![MediaMetaModel.KEY_FILE_THUMB] = APIClient.IMAGE_URL + data.thumbnail
                }
                val messageContent = HashMap<String, Any?>()
                val fileUrl: String =  /*APIClient.IMAGE_URL + */data.file!!
                messageContent["file_url"] = fileUrl
                messageContent["file_meta"] = messageData
                sendMessage("", messageType, messageContent)
            }
        }
    }

    override fun closeAudioPlayer() {
        binding.audioPlayerFragment.visibility = View.GONE
    }

    private fun joinCommand() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("type", "allMessage")
            jsonObject.put("room", _roomId)
            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_MESSAGE)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        WebSocketSingleton.getInstant()?.sendMessage(jsonObject)
    }

    private val blockList: Unit
        private get() {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("type", "allBlockUser")
                jsonObject.put("user", UserDetails.myDetail.id)
                jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_BLOCK_USER)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            WebSocketSingleton.getInstant()?.sendMessage(jsonObject)
        }

    private fun setRead() {
        val messageMap = HashMap<String?, Any?>()
        messageMap["type"] = "roomsModify"
        messageMap["roomId"] = _roomId
        messageMap["unread"] = UserDetails.myDetail.id
        messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] = APIClient.KeyConstant.REQUEST_TYPE_ROOM
        WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        Log.d(TAG, "received message: $response")
        runOnUiThread {
            try {
                if (ResponseType.RESPONSE_TYPE_MESSAGES.equalsTo(type)) {
                    val responseData = JSONObject(response)
                    val responseCode = responseData.getInt("statusCode")
                    if (responseCode == 200) {
                        val jsonObject = responseData.getJSONArray("data")
                        for (i in 0 until jsonObject.length()) {
                            val nthObject = jsonObject.getJSONObject(i)
                            appendMessage(nthObject, false)
                        }
                        setRead()

                        ///Move to bottom if user open chat first time
                        binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    } else if (responseCode == 201) {
                        appendMessage(responseData.getJSONObject("data"), true)
                        //                        binding.chatNewMessageCount
                        setRead()
                    } else {
                        Toast.makeText(this@ChatActivity, responseData.getString("message"), Toast.LENGTH_SHORT).show()
                    }


//                        if (jsonObject.has("message")) {
//                            ChattingModel chattingModel = ChattingModel.getModel(jsonObject, PreferenceUtils.getLoginUser(ChatActivity.this).getId());
//                            chattingAdapter.updateList(chattingModel);
////                            chatRecyclerView.add
//                            binding.chatRecyclerView.scrollToPosition(chattingAdapter.items.size() - 1);
//                        }
                } else if (ResponseType.RESPONSE_TYPE_ROOM_DETAILS.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<RoomResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> = Gson().fromJson<ResponseModel<RoomResponseModel>>(response, type1)
                        for (element in roomResponseModelResponseModel.getData().userList) {
                            UserDetails.chatUsers.put(element.id, element)
                        }
                        val roomDetails: FSRoomModel = roomResponseModelResponseModel.getData().roomList.get(0)
                        _senderDetails = roomDetails.senderUserDetail!!
                        /*for (FSRoomModel elemen  t : roomResponseModelResponseModel.getData().getRoomList()) {
                            for (String userId : element.getUserList()) {
                                if (!userId.equals(UserDetails.myDetail.getId())) {
                                    element.setSenderUserDetail(UserDetails.chatUsers.get(userId));
                                    break;
                                }
                            }
                        }*/joinCommand()
                        blockList
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } else if (ResponseType.RESPONSE_TYPE_USER_MODIFIED.equalsTo(type)) {
                    Log.d(TAG, "received message: $response")
                    val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                    val fsUsersModelResponseModel: ResponseModel<FSUsersModel> = Gson().fromJson<ResponseModel<FSUsersModel>>(response, type1)
                    _senderDetails = fsUsersModelResponseModel.getData()
                    if (_isGroup) {
//                    setGroupDetails();
                    } else {
                        setIndividualDetails()
                    }

//                otherUser = UserDetails.chatUsers.get(id);
//
//                setOtherDetails();
                } else if (ResponseType.RESPONSE_TYPE_USER_BLOCK_MODIFIED.equalsTo(type)) {
                    Log.d(TAG, "received message: $response")
                    val type1 = object : TypeToken<ResponseModel<UserBlockModel?>?>() {}.type
                    val userBlockModelResponseModel: ResponseModel<UserBlockModel> = Gson().fromJson<ResponseModel<UserBlockModel>>(response, type1)
                    val element: UserBlockModel = userBlockModelResponseModel.getData()
                    if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && element.isBlock) {
                        blockedByOtherUser()
                    }
                    if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && !element.isBlock) {
                        unBlockedByOtherUser()
                    }
                    if (element.blockedTo == _senderDetails.id && element.blockedBy == UserDetails.myDetail.id && element.isBlock) {
                        isBlocked = true
                    }
                } else if (ResponseType.RESPONSE_TYPE_USER_ALL_BLOCK.equalsTo(type)) {
                    Log.d(TAG, "received message: $response")
                    if (!_isGroup) {
                        val type1 = object : TypeToken<ResponseModel<ArrayList<UserBlockModel?>?>?>() {}.type
                        val userBlockModelResponseModel: ResponseModel<ArrayList<UserBlockModel>> = Gson().fromJson<ResponseModel<ArrayList<UserBlockModel>>>(response, type1)
                        for (element in userBlockModelResponseModel.getData()) {
                            if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && element.isBlock) {
                                blockedByOtherUser()
                                break
                            }
                            if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && !element.isBlock) {
                                unBlockedByOtherUser()
                                break
                            }
                            if (element.blockedTo == _senderDetails.id && element.blockedBy == UserDetails.myDetail.id && element.isBlock) {
                                isBlocked = true
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "onWebSocketResponse: $type")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    private fun blockedByOtherUser() {
        binding.blockedWrapper.visibility = View.VISIBLE
        binding.chatMessageWrapper.visibility = View.GONE
    }

    private fun unBlockedByOtherUser() {
        binding.blockedWrapper.visibility = View.GONE
        binding.chatMessageWrapper.visibility = View.VISIBLE
    }

    override val activityName: String = ChatActivity::class.java.name

    override fun registerFor(): Array<ResponseType> {
        return arrayOf<ResponseType>(
                ResponseType.RESPONSE_TYPE_MESSAGES,
                ResponseType.RESPONSE_TYPE_USER_MODIFIED,
                ResponseType.RESPONSE_TYPE_USER_BLOCK_MODIFIED,
                ResponseType.RESPONSE_TYPE_USER_ALL_BLOCK,
                ResponseType.RESPONSE_TYPE_ROOM_DETAILS
        )
    }

    companion object {
        const val INTENT_EXTRAS_KEY_IS_GROUP = "isGroup"
        const val INTENT_EXTRAS_KEY_GROUP_DETAILS = "groupDetails"
        const val INTENT_EXTRAS_KEY_ROOM_ID = "roomID"
        const val INTENT_EXTRAS_KEY_SENDER_DETAILS = "senderDetails"
        private const val TAG = "ChatActivity"
        private const val REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE = 3
        private const val REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO = 4
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 5
        private const val REQUEST_ADD_CONTACT = 8
        private const val REQUEST_SELECT_CONTACTS = 9
        private const val GALLERY = 1
        private const val CAMERA = 2
        private var fileName: String? = null
    }
}