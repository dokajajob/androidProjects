package com.dokajajob.skipphelp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dokajajob.skipphelp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton postImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private ImageButton submitPost;
    private StorageReference mStorage;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private Uri mImageUri;
    //public Uri uploadUrl;
    private static final int GALLERY_CODE = 1;
    private String TAG = "AddPostActivity ";
    private FirebaseFunctions mFunctions;
    public String mUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUID = mUser.getUid();
        mStorage = FirebaseStorage.getInstance().getReference();

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child(mUID);
        Log.d(TAG, "DatabaseReference : " + mUser.toString());
        Log.d(TAG, "DatabaseReference : " + mUID);


        postImage = (ImageButton) findViewById(R.id.postImage);
        mPostTitle = (EditText) findViewById(R.id.postTitleEt);
        mPostDesc = (EditText)findViewById(R.id.descriptionEt);
        submitPost = (ImageButton) findViewById(R.id.submitPost);


        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_CODE);

            }
        });



        submitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start Posting
                startPost();

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add:
                startPost();
                break;

            case R.id.action_back:
                startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
                finish();
                break;

            case  R.id.action_signout:
                mAuth.signOut();
                startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                mImageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(mImageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                postImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddPostActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(AddPostActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    private void startPost() {

        mProgress.setMessage("Posting ... ");
        mProgress.show();

        //mFunctions = FirebaseFunctions.getInstance("europe-west1");

        final String titleVal = mPostTitle.getText().toString().trim();
        final  String descVal = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && mImageUri != null) {

            //Start upload
            final StorageReference filePath = mStorage.child(mUID).child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri uploadUrl = uri;
                            Log.d("uploadUri :", uploadUrl.toString());
                            Toast.makeText(getBaseContext(), "Upload success! URL - " + uploadUrl.toString() , Toast.LENGTH_SHORT).show();

                            DatabaseReference newPost = mPostDatabase.push();

                            Map<String, String> dataToSave = new HashMap<>();
                            dataToSave.put("title", titleVal);
                            dataToSave.put("description", descVal);
                            dataToSave.put("image", uploadUrl.toString());
                            dataToSave.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));
                            dataToSave.put("user_id", mUser.getUid());
                            dataToSave.put("user_name", mUser.getEmail());

                            Log.d("dataToSave", dataToSave.toString());
                            newPost.setValue(dataToSave);
                            mProgress.dismiss();

                            startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
                            finish();

                        }
                    });


                }
            });

        } else {
            mProgress.dismiss();
            Toast.makeText(AddPostActivity.this, "Edit Post First", Toast.LENGTH_LONG).show();
        }

    }

}