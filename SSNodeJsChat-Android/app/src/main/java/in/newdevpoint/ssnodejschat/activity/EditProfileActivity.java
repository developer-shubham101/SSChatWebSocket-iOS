package in.newdevpoint.ssnodejschat.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.io.File;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.ActivityEditProfileBinding;
import in.newdevpoint.ssnodejschat.utility.PermissionClass;
import in.newdevpoint.ssnodejschat.utility.Watting;

public class EditProfileActivity extends AppCompatActivity implements PermissionClass.PermissionRequire, View.OnClickListener {
    private static final int REQUEST_CODE_CLASS = 1;
    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private static final int FLAG_GALLERY = 1;


    private PermissionClass permissionClass;
    private Watting mWaitingDialog;
    private File pic;
    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tutor_edit_profile);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);


        imagePickerClass = new ImagePickerClass(this, this);
        permissionClass = new PermissionClass(this, this);
        mWaitingDialog = new Watting(this);

        classesAdapter = new ClassesAdapter(this);

        binding.tutorEditProfileClassesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.tutorEditProfileClassesRecycler.hasFixedSize();
        binding.tutorEditProfileClassesRecycler.setAdapter(classesAdapter);
        subscribe();

        binding.tutorEditProfilePicBtn.setOnClickListener(this);
        binding.tutorEditProfileBtnBack.setOnClickListener(this);
        binding.tutorEditProfileSave.setOnClickListener(this);
        binding.tutorEditProfileClassesBtn.setOnClickListener(this);

    }

    private void subscribe() {
        viewModel = ViewModelProviders.of(this).get(TutorEditProfileViewModel.class);
        viewModel.getReviewModelResponse().observe(this, profileModelResponseModel -> {
            mWaitingDialog.dismiss();
            if (profileModelResponseModel != null) {

                if (profileModelResponseModel.getCode() == 200) {
                    ProfileModel data = profileModelResponseModel.getData();
                    binding.tutorEditProfileName.setText(data.getUsername());
                    binding.tutorEditProfileCity.setText(data.getLocation());
                    binding.tutorEditProfileTitle.setText(data.getTitle());
                    binding.tutorEditProfileDescription.setText(data.getDescription());
                    classesAdapter.addAll(data.getClass_list());
                    Glide.with(TutorEditProfileActivity.this).load(data.getProfile_picture()).into(binding.tutorEditProfileProfilePic);
                    if (data.getGender().equals("MALE")) {
                        binding.tutorEditProfileGenderMale.setChecked(true);
                    } else {
                        binding.tutorEditProfileGenderFemale.setChecked(true);
                    }

                } else {
                    Toast.makeText(TutorEditProfileActivity.this, profileModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TutorEditProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getTutorEditProfileModelResponse().observe(this, tutorEditProfileModelResponseModel -> {
            mWaitingDialog.dismiss();
            if (tutorEditProfileModelResponseModel != null) {
                if (tutorEditProfileModelResponseModel.getCode() == 200) {
                    Toast.makeText(TutorEditProfileActivity.this, tutorEditProfileModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    mWaitingDialog.show();
                    viewModel.getMyProfile();
                } else {
                    Toast.makeText(TutorEditProfileActivity.this, tutorEditProfileModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(TutorEditProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getClassModelResponse().observe(this, arrayListResponseModel -> {
            mWaitingDialog.dismiss();
            if (arrayListResponseModel != null) {
                SelectClassData.list = arrayListResponseModel.getData();

            } else {
                Log.e(TAG, "onChanged: null value error");
            }
        });
        mWaitingDialog.show();
        viewModel.getClassList("");

        viewModel.getMyProfile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionClass.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CLASS) {
            if (resultCode == Activity.RESULT_OK) {
                String id = data.getStringExtra(SelectClassesActivity.INTENT_EXTRA_ID);
                String name = data.getStringExtra(SelectClassesActivity.INTENT_EXTRA_NAME);
                classesAdapter.add(new ClassModel(id, name));
            }
        } else {
            if (imagePickerClass != null)
                imagePickerClass.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tutorEditProfilePicBtn:
                permissionClass.askPermission(FLAG_GALLERY);
                break;
            case R.id.back_btnImage:
                finish();
                break;
            case R.id.tutorEditProfileSave:
                submit();
                break;
            case R.id.tutorEditProfileClassesBtn:

                Intent intent = new Intent(this, SelectClassesActivity.class);
                intent.putExtra(SelectClassesActivity.INTENT_EXTRA_SHOW_ADD, true);
                startActivityForResult(intent, REQUEST_CODE_CLASS);
                break;

        }
    }

    @Override
    public void fileUrl(File file) {
        Uri uri = Uri.fromFile(file);
        binding.tutorEditProfileProfilePic.setImageURI(uri);
        pic = file;
    }

    @Override
    public void permissionDeny() {

    }

    @Override
    public void permissionGranted(int flag) {

    }

    @Override
    public String[] listOfPermission() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @NonNull
    private String getSelectedGender() {
        return (binding.tutorEditProfileGenderGroup.getCheckedRadioButtonId() == R.id.tutorEditProfileGenderMale) ? "MALE" : "FEMALE";
    }

    private boolean isRadioSelected(RadioGroup registerAccountTypeGroup) {
        return registerAccountTypeGroup.getCheckedRadioButtonId() == -1;
    }

    private void submit() {
        String username = binding.tutorEditProfileName.getText().toString();
        String location = binding.tutorEditProfileCity.getText().toString();
        String description = binding.tutorEditProfileDescription.getText().toString();
        String title = binding.tutorEditProfileTitle.getText().toString();
        String specialize = "Nothing";
        StringBuilder selectedClass = null;
        for (ClassModel item : classesAdapter.getItems()) {
            if (selectedClass == null) {
                selectedClass = new StringBuilder();
                selectedClass.append(item.getId());
            } else {
                selectedClass.append(",").append(item.getId());
            }

        }
        if (username.isEmpty()) {
            Toast.makeText(TutorEditProfileActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
        } else if (isRadioSelected(binding.tutorEditProfileGenderGroup)) {
            Toast.makeText(TutorEditProfileActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
        } else if (location.isEmpty()) {
            Toast.makeText(TutorEditProfileActivity.this, "Please Enter Near Location", Toast.LENGTH_SHORT).show();
        } else if (title.isEmpty()) {
            Toast.makeText(TutorEditProfileActivity.this, "Please Enter Title", Toast.LENGTH_SHORT).show();
        } else if (description.isEmpty()) {
            Toast.makeText(TutorEditProfileActivity.this, "Please Enter Some description", Toast.LENGTH_SHORT).show();
        } else if (selectedClass == null) {
            Toast.makeText(TutorEditProfileActivity.this, "Please Enter Classes", Toast.LENGTH_SHORT).show();
        } else {
            mWaitingDialog.show();
            if (pic == null) {
                viewModel.profileEdit(username, getSelectedGender(), location, description, specialize, title, selectedClass.toString());
            } else {
                viewModel.profileEdit(username, getSelectedGender(), location, description, specialize, title, selectedClass.toString(), pic);
            }


        }
    }
}
