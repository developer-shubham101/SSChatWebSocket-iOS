package in.newdevpoint.ssnodejschat.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.request.DownloadRequest;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.RowLeftChatContactBinding;
import in.newdevpoint.ssnodejschat.databinding.RowLeftChatDocBinding;
import in.newdevpoint.ssnodejschat.databinding.RowLeftChatImageBinding;
import in.newdevpoint.ssnodejschat.databinding.RowLeftChatLocationBinding;
import in.newdevpoint.ssnodejschat.databinding.RowLeftChatTextBinding;
import in.newdevpoint.ssnodejschat.databinding.RowLeftChatVideoBinding;
import in.newdevpoint.ssnodejschat.databinding.RowRightChatContactBinding;
import in.newdevpoint.ssnodejschat.databinding.RowRightChatDocBinding;
import in.newdevpoint.ssnodejschat.databinding.RowRightChatImageBinding;
import in.newdevpoint.ssnodejschat.databinding.RowRightChatLocationBinding;
import in.newdevpoint.ssnodejschat.databinding.RowRightChatTextBinding;
import in.newdevpoint.ssnodejschat.databinding.RowRightChatVideoBinding;
import in.newdevpoint.ssnodejschat.model.ChatModel;
import in.newdevpoint.ssnodejschat.model.ContactModel;
import in.newdevpoint.ssnodejschat.model.LocationModel;
import in.newdevpoint.ssnodejschat.model.MediaModel;
import in.newdevpoint.ssnodejschat.stickyheader.stickyView.StickHeaderRecyclerView;
import in.newdevpoint.ssnodejschat.utility.BroadCastConstants;
import in.newdevpoint.ssnodejschat.utility.DownloadUtility;
import in.newdevpoint.ssnodejschat.utility.MD5;


public class ChatAdapter extends StickHeaderRecyclerView<ChatModel, HeaderDataImpl> {

    private final Context context;
    private final ChatCallbacks chatCallbacks;

    public ChatAdapter(Context context, ChatCallbacks chatCallbacks) {
        this.context = context;
        this.chatCallbacks = chatCallbacks;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case StickHeaderRecyclerView.ROW_TYPE_RIGHT_TEXT:
            case StickHeaderRecyclerView.ROW_TYPE_RIGHT_REPlAY: {
                RowRightChatTextBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_right_chat_text, parent, false);
                return new RightTextViewHolder(binding);
            }
            case StickHeaderRecyclerView.ROW_TYPE_RIGHT_IMAGE: {
                RowRightChatImageBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_right_chat_image, parent, false);
                return new RightImageViewHolder(binding);
            }

            case StickHeaderRecyclerView.ROW_TYPE_RIGHT_VIDEO: {
                RowRightChatVideoBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_right_chat_video, parent, false);
                return new RightVideoViewHolder(binding);
            }
            case StickHeaderRecyclerView.ROW_TYPE_RIGHT_DOCUMENT: {
                RowRightChatDocBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_right_chat_doc, parent, false);
                return new RightDocViewHolder(binding);
            }
            case StickHeaderRecyclerView.ROW_TYPE_RIGHT_LOCATION: {
                RowRightChatLocationBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_right_chat_location, parent, false);
                return new RightLocationViewHolder(binding);
            }
            case StickHeaderRecyclerView.ROW_TYPE_RIGHT_CONTACT: {
                RowRightChatContactBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_right_chat_contact, parent, false);
                return new RightContactViewHolder(binding);
            }


//

            case StickHeaderRecyclerView.ROW_TYPE_LEFT_TEXT:
            case StickHeaderRecyclerView.ROW_TYPE_LEFT_REPlAY: {
                RowLeftChatTextBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_left_chat_text, parent, false);
                return new LeftTextViewHolder(binding);
            }
            case StickHeaderRecyclerView.ROW_TYPE_LEFT_IMAGE: {
                RowLeftChatImageBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_left_chat_image, parent, false);
                return new LeftImageViewHolder(binding);
            }

            case StickHeaderRecyclerView.ROW_TYPE_LEFT_VIDEO: {
                RowLeftChatVideoBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_left_chat_video, parent, false);
                return new LeftVideoViewHolder(binding);
            }
            case StickHeaderRecyclerView.ROW_TYPE_LEFT_DOCUMENT: {
                RowLeftChatDocBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_left_chat_doc, parent, false);
                return new LeftDocViewHolder(binding);
            }
            case StickHeaderRecyclerView.ROW_TYPE_LEFT_LOCATION: {
                RowLeftChatLocationBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_left_chat_location, parent, false);
                return new LeftLocationViewHolder(binding);
            }


            case StickHeaderRecyclerView.ROW_TYPE_LEFT_CONTACT: {
                RowLeftChatContactBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_left_chat_contact, parent, false);
                return new LeftContactViewHolder(binding);
            }


            default:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header1_item_recycler, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {

        TextView tv = header.findViewById(R.id.tvHeader);
        HeaderDataImpl object = getHeaderDataInPosition(headerPosition);
        tv.setText(object.getTitle());
    }

    public interface ChatCallbacks {
        void onClickDownload(ChatModel chatModel, MediaModel messageContent, boolean stopDownloading, OnDownloadListener onDownloadListener);

        void onClickContact(ChatModel chatModel, ContactModel contactModel);

        void onClickLocation(ChatModel chatModel, LocationModel contactModel);

        void onLongClick(ChatModel chatModel);
    }

    interface HolderCallback {
        TextView getChatDownloadPercentage();

        ImageView getChatDownloadStatus();
    }

    static class BroadcastProgress extends BroadcastReceiver {
        TextView appRowAppDownloadStatusText;

        public BroadcastProgress(TextView appRowAppDownloadStatusText) {
            this.appRowAppDownloadStatusText = appRowAppDownloadStatusText;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(BroadCastConstants.INTENT_PROGRESS_COUNT, 0);

//			appDetailsProgressBar.setProgress(progress);

            appRowAppDownloadStatusText.setText(progress + "%");
        }
    }

    class HeaderViewHolder extends BaseViewHolder {
        TextView tvHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }

        @Override
        void bindData(int position) {
            HeaderDataImpl object = getHeaderDataInPosition(position);
            tvHeader.setText(object.getTitle());
        }
    }

    class RightTextViewHolder extends BaseViewHolder {

        RowRightChatTextBinding binding;

        RightTextViewHolder(RowRightChatTextBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);

            binding.chatRightTextMessage.setText(object.getMessage());
            binding.chatRightTextTime.setText(object.getMessage_on());
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }
    }

    class RightImageViewHolder extends BaseViewHolder {
        RowRightChatImageBinding binding;

        RightImageViewHolder(RowRightChatImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//			itemView.setOnClickListener(v -> chatRowClicked.imageClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);

            MediaModel messageContent = (MediaModel) object.getMessage_content();


            Glide.with(context).load(messageContent.getFile_url()).into(binding.chatRightImageImage);
            binding.chatRightImageTime.setText(object.getMessage_on());
//            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    chatCallbacks.onLongClick(object);
//                    return true;
//                }
//            });

            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickDownload(object, messageContent, object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING, null);
                }
            });
        }
    }

    class RightVideoViewHolder extends MediaViewHolder {
        private static final String TAG = "RightVideoViewHolder";
        RowRightChatVideoBinding binding;

        RightVideoViewHolder(RowRightChatVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//			itemView.setOnClickListener(v -> chatRowClicked.imageClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {

            ChatModel object = getDataInPosition(position);
            MediaModel messageContent = (MediaModel) object.getMessage_content();

            OnDownloadListener onDownloadListener = super.getDownloadListener(position, object, messageContent);

            Log.d(TAG, "bindData: " + messageContent.getFile_meta().getThumbnail());
            Glide.with(context).load(messageContent.getFile_meta().getThumbnail()).into(binding.chatRightVideoImage);
            binding.chatRightVideoTime.setText(object.getMessage_on());
            binding.chatRightVideoTime.setText(object.getMessage_on());
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickDownload(object, messageContent, object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING, onDownloadListener);
                }
            });
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }

        @Override
        public TextView getChatDownloadPercentage() {
            return binding.chatRightDownloadPercentage;
        }

        @Override
        public ImageView getChatDownloadStatus() {
            return binding.chatRightDownloadStatus;
        }

		/*void bindData(int position) {
			ChatModel object = getDataInPosition(position);

			MediaModel messageContent = (MediaModel) object.getMessage_content();

			Log.d(TAG, "bindData: " + messageContent.getFile_meta().getThumbnail());
			Glide.with(context).load(messageContent.getFile_meta().getThumbnail()).into(binding.chatRightVideoImage);
//			binding.chatRightTextMessage.setText(object.getTitle());
		}*/
    }

    class RightDocViewHolder extends MediaViewHolder {
        private static final String TAG = "RightDocViewHolder";
        RowRightChatDocBinding binding;

        RightDocViewHolder(RowRightChatDocBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//			itemView.setOnClickListener(v -> chatRowClicked.docClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {

            ChatModel object = getDataInPosition(position);
            MediaModel messageContent = (MediaModel) object.getMessage_content();

            OnDownloadListener onDownloadListener = super.getDownloadListener(position, object, messageContent);

            Log.d(TAG, "bindData: " + messageContent.getFile_meta().getThumbnail());
//			Glide.with(context).load(messageContent.getFile_meta().getThumbnail()).into(binding.chatLeftViewImage);
            binding.chatRightDocFileName.setText(messageContent.getFile_url());
            binding.chatRightDocTime.setText(object.getMessage_on());
            try {
                URL url = new URL(messageContent.getFile_url());
                binding.chatRightDocFileName.setText(FilenameUtils.getName(url.getPath()));
                binding.chatRightDocType.setText(FilenameUtils.getExtension(url.getPath()));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickDownload(object, messageContent, object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING, onDownloadListener);
                }
            });

            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }

        @Override
        public TextView getChatDownloadPercentage() {
            return binding.chatLeftDocDownloadPercentage;
        }

        @Override
        public ImageView getChatDownloadStatus() {
            return binding.chatLeftDocDownloadStatus;
        }
    }

    class RightLocationViewHolder extends BaseViewHolder {

        RowRightChatLocationBinding binding;

        RightLocationViewHolder(RowRightChatLocationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//			itemView.setOnClickListener(v -> chatRowClicked.locationClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);
            LocationModel messageContent = (LocationModel) object.getMessage_content();


            binding.chatRightLocationName.setText(messageContent.getName());
            binding.chatRightLocationAddress.setText(messageContent.getAddress());
            binding.chatRightLocationTime.setText(object.getMessage_on());
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickLocation(object, messageContent);
                }


            });
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }
    }

    class RightContactViewHolder extends BaseViewHolder {

        RowRightChatContactBinding binding;

        RightContactViewHolder(RowRightChatContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//			itemView.setOnClickListener(v -> chatRowClicked.locationClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);
            ContactModel messageContent = (ContactModel) object.getMessage_content();

            String fullName = messageContent.getFirst_name() + " " + messageContent.getLast_name();
            binding.chatRightContactName.setText(fullName);
            binding.chatRightContactNumber.setText(messageContent.getMobile());
            binding.chatRightContactTime.setText(object.getMessage_on());

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickContact(object, messageContent);
                }
            });
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });

        }
    }


    class LeftLocationViewHolder extends BaseViewHolder {

        RowLeftChatLocationBinding binding;

        LeftLocationViewHolder(RowLeftChatLocationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//			itemView.setOnClickListener(v -> chatRowClicked.locationClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);
            LocationModel messageContent = (LocationModel) object.getMessage_content();


            binding.chatLeftLocationName.setText(messageContent.getName());
            binding.chatLeftLocationAddress.setText(messageContent.getAddress());
            binding.chatLeftLocationTime.setText(object.getMessage_on());
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickLocation(object, messageContent);
                }


            });
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }
    }


    class LeftContactViewHolder extends BaseViewHolder {

        RowLeftChatContactBinding binding;

        LeftContactViewHolder(RowLeftChatContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//			itemView.setOnClickListener(v -> chatRowClicked.locationClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);
            ContactModel messageContent = (ContactModel) object.getMessage_content();

            String fullName = messageContent.getFirst_name() + " " + messageContent.getLast_name();
            binding.chatLeftContactName.setText(fullName);
            binding.chatLeftContactNumber.setText(messageContent.getMobile());
            binding.chatLeftContactTime.setText(object.getMessage_on());
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickContact(object, messageContent);
                }
            });
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }
    }

    class LeftTextViewHolder extends BaseViewHolder {
        RowLeftChatTextBinding binding;

        LeftTextViewHolder(RowLeftChatTextBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);
            binding.chatLeftTextMessage.setText(object.getMessage());
            binding.chatLeftTextTime.setText(object.getMessage_on());
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }
    }

    class LeftImageViewHolder extends BaseViewHolder {
        RowLeftChatImageBinding binding;

        LeftImageViewHolder(RowLeftChatImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//			itemView.setOnClickListener(v -> chatRowClicked.imageClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {
            ChatModel object = getDataInPosition(position);

            MediaModel messageContent = (MediaModel) object.getMessage_content();


            Glide.with(context).load(messageContent.getFile_url()).into(binding.chatLeftImageImage);
            binding.chatLeftImageTime.setText(object.getMessage_on());

           /* this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });*/


            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickDownload(object, messageContent, object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING, null);
                }
            });
        }
    }

    abstract class MediaViewHolder extends BaseViewHolder implements HolderCallback {


        private static final String TAG = "MediaViewHolder";

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        OnDownloadListener getDownloadListener(int position, ChatModel object, MediaModel messageContent) {


            Log.d(TAG, "bindData: " + messageContent.getFile_meta().getThumbnail());


            if (object.getDownloadStatus() != ChatModel.DownloadStatus.DOWNLOADED) {
                @Nullable DownloadRequest task = DownloadUtility.downloadList.get(MD5.stringToMD5(messageContent.getFile_url()));

                if (task != null) {
                    object.setDownloadStatus(ChatModel.DownloadStatus.DOWNLOADING);

                } else {
                    object.setDownloadStatus(ChatModel.DownloadStatus.PENDING);
				/*boolean isAppDownloaded = false;
				try {
					URL url = new URL(messageContent.getFile_url());
					String downloadFileName = FilenameUtils.getName(url.getPath());
					File downloadDir = context.getExternalFilesDir(null);

					File downloadFile = new File(downloadDir.getAbsolutePath() + "/" + downloadFileName);
					isAppDownloaded = downloadFile.exists();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				if (isAppDownloaded) {
					object.setDownloadStatus(ChatModel.DownloadStatus.DOWNLOADED);
				} else {
					object.setDownloadStatus(ChatModel.DownloadStatus.PENDING);
				}*/

                }
            }


            if (object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING) {
                getChatDownloadStatus().setImageResource(R.drawable.ic_chat_loading);
                RotateAnimation rotateAnimation = new RotateAnimation(
                        0f, 359f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f

                );
                rotateAnimation.setDuration(1000);
                rotateAnimation.setRepeatCount(Animation.INFINITE);

                getChatDownloadStatus().startAnimation(rotateAnimation);
            } else if (object.getDownloadStatus() == ChatModel.DownloadStatus.PENDING) {
                getChatDownloadStatus().setImageResource(R.drawable.ic_chat_download);
                getChatDownloadStatus().clearAnimation();
                getChatDownloadPercentage().setText("Download");

            } else if (object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADED) {
                getChatDownloadStatus().setImageResource(R.drawable.ic_chat_open_file);
                getChatDownloadStatus().clearAnimation();
                getChatDownloadPercentage().setText("Open");
            }


            context.registerReceiver(new BroadcastProgress(getChatDownloadPercentage()), new IntentFilter(BroadCastConstants.INTENT_REFRESH + MD5.stringToMD5(messageContent.getFile_url())));


            return new OnDownloadListener() {

                @Override
                public void onDownloadComplete() {

                    File downloadDir = context.getExternalFilesDir(null);
                    String fileName = messageContent.getFile_url();
                    File downloadFile = new File(downloadDir.getAbsolutePath() + "/" + fileName);


                    DownloadUtility.downloadList.remove(MD5.stringToMD5(messageContent.getFile_url()));
                    object.setDownloadStatus(ChatModel.DownloadStatus.DOWNLOADED);
                    notifyDataSetChanged();
                    Log.d(TAG, "onDownloadComplete: ");
                }


                @Override
                public void onError(Error error) {
                    DownloadUtility.downloadList.remove(MD5.stringToMD5(messageContent.getFile_url()));
//					items.get(getAdapterPosition()).setDownloadStatus(AppListModel.Level.PENDING);
                    object.setDownloadStatus(ChatModel.DownloadStatus.PENDING);
                    notifyDataSetChanged();
                    Log.e(TAG, "onError: ");
                    System.out.println(error);
                }
            };


        }
    }

    class LeftVideoViewHolder extends MediaViewHolder {
        private static final String TAG = "LeftVideoViewHolder";
        RowLeftChatVideoBinding binding;

        LeftVideoViewHolder(RowLeftChatVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//			itemView.setOnClickListener(v -> chatRowClicked.imageClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {

            ChatModel object = getDataInPosition(position);
            MediaModel messageContent = (MediaModel) object.getMessage_content();

            OnDownloadListener onDownloadListener = super.getDownloadListener(position, object, messageContent);

            Log.d(TAG, "bindData: " + messageContent.getFile_meta().getThumbnail());
            Glide.with(context).load(messageContent.getFile_meta().getThumbnail()).into(binding.chatLeftViewImage);
            binding.chatLeftViewTime.setText(object.getMessage_on());
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickDownload(object, messageContent, object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING, onDownloadListener);
                }
            });
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }

//		void bindData(int position) {
//
//
//			ChatModel object = getDataInPosition(position);
//			MediaModel messageContent = (MediaModel) object.getMessage_content();
//
//
//			@Nullable DownloadRequest task = DownloadUtility.downloadList.get(MD5.stringToMD5(messageContent.getFile_url()));
//
//
//			if (task != null) {
//				object.setDownloadStatus(ChatModel.DownloadStatus.DOWNLOADING);
//
//			} else {
//				boolean isAppDownloaded = false;
//				try {
//					URL url = new URL(messageContent.getFile_url());
//					String downloadFileName = FilenameUtils.getName(url.getPath());
//					File downloadDir = context.getExternalFilesDir(null);
//
//					File downloadFile = new File(downloadDir.getAbsolutePath() + "/" + downloadFileName);
//					isAppDownloaded = downloadFile.exists();
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//				if (isAppDownloaded) {
//					object.setDownloadStatus(ChatModel.DownloadStatus.DOWNLOADED);
//				} else {
//					object.setDownloadStatus(ChatModel.DownloadStatus.PENDING);
//				}
//
//			}
//
//
//			if (object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING) {
//				getChatDownloadStatus().setImageResource(R.drawable.ic_chat_loading);
//				RotateAnimation rotateAnimation = new RotateAnimation(
//						0f, 359f,
//						Animation.RELATIVE_TO_SELF, 0.5f,
//						Animation.RELATIVE_TO_SELF, 0.5f
//
//				);
//				rotateAnimation.setDuration(1000);
//				rotateAnimation.setRepeatCount(Animation.INFINITE);
//
//				getChatDownloadStatus().startAnimation(rotateAnimation);
//			} else if (object.getDownloadStatus() == ChatModel.DownloadStatus.PENDING) {
//				getChatDownloadStatus().setImageResource(R.drawable.ic_chat_download);
//				getChatDownloadStatus().clearAnimation();
//				getChatDownloadPercentage().setText("Download");
//
//			} else if (object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADED) {
//				getChatDownloadStatus().setImageResource(R.drawable.ic_chat_open_file);
//				getChatDownloadStatus().clearAnimation();
//				getChatDownloadPercentage().setText("Open");
//			}
//
//
//			context.registerReceiver(new BroadcastProgress(getChatDownloadPercentage()), new IntentFilter(BroadCastConstants.INTENT_REFRESH + MD5.stringToMD5(messageContent.getFile_url())));
//			OnDownloadListener onDownloadListener = new OnDownloadListener() {
//
//				@Override
//				public void onDownloadComplete() {
//
//					File downloadDir = context.getExternalFilesDir(null);
//					String fileName = messageContent.getFile_url();
//					File downloadFile = new File(downloadDir.getAbsolutePath() + "/" + fileName);
//
//
//					DownloadUtility.downloadList.remove(MD5.stringToMD5(messageContent.getFile_url()));
//					object.setDownloadStatus(ChatModel.DownloadStatus.DOWNLOADED);
//					notifyDataSetChanged();
//					Log.d(TAG, "onDownloadComplete: ");
//				}
//
//				@Override
//				public void onError(Error error) {
//					DownloadUtility.downloadList.remove(MD5.stringToMD5(messageContent.getFile_url()));
////					items.get(getAdapterPosition()).setDownloadStatus(AppListModel.Level.PENDING);
//					object.setDownloadStatus(ChatModel.DownloadStatus.PENDING);
//					notifyDataSetChanged();
//					Log.e(TAG, "onError: ");
//					System.out.println(error);
//				}
//			};
//
//
//			Log.d(TAG, "bindData: " + messageContent.getFile_meta().getThumbnail());
//			Glide.with(context).load(messageContent.getFile_meta().getThumbnail()).into(binding.chatLeftViewImage);
////			binding.chatRightTextMessage.setText(object.getTitle());
//			this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					chatCallbacks.onClickDownload(object, messageContent, object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING, onDownloadListener);
//				}
//			});
//
//		}

        public TextView getChatDownloadPercentage() {
            return binding.chatLeftDownloadPercentage;
        }

        public ImageView getChatDownloadStatus() {
            return binding.chatLeftDownloadStatus;
        }
    }

    class LeftDocViewHolder extends MediaViewHolder {
        private static final String TAG = "LeftDocViewHolder";
        RowLeftChatDocBinding binding;

        LeftDocViewHolder(RowLeftChatDocBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


//			itemView.setOnClickListener(v -> chatRowClicked.docClicked(items.get(getAdapterPosition())));
        }

        @Override
        void bindData(int position) {

            ChatModel object = getDataInPosition(position);
            MediaModel messageContent = (MediaModel) object.getMessage_content();

            OnDownloadListener onDownloadListener = super.getDownloadListener(position, object, messageContent);

            Log.d(TAG, "bindData: " + messageContent.getFile_meta().getThumbnail());
//			Glide.with(context).load(messageContent.getFile_meta().getThumbnail()).into(binding.chatLeftViewImage);
            binding.chatLeftDocTime.setText(object.getMessage_on());
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallbacks.onClickDownload(object, messageContent, object.getDownloadStatus() == ChatModel.DownloadStatus.DOWNLOADING, onDownloadListener);
                }
            });
            this.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatCallbacks.onLongClick(object);
                    return true;
                }
            });
        }

        public TextView getChatDownloadPercentage() {
            return binding.chatLeftDocDownloadPercentage;
        }

        public ImageView getChatDownloadStatus() {
            return binding.chatLeftDocDownloadStatus;
        }
    }
}
