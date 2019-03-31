package luhar.sohil.thesmartguardian_school;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import Model.DataSet;
import Model.Message;

public class AddStudentPhoto extends AppCompatActivity {

    EditText stdId;
    Button clickPhoto,uploadPhoto,choosePhoto;
    ImageView imageView;


    byte data1[];
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    public static  final int CAMERA_REQ_CODE= 1;


    FirebaseStorage storage ;
    StorageReference storageRef;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbStudent,dbDataSet;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_photo);

        stdId=(EditText)findViewById(R.id.etstudentPhotoId);
        clickPhoto=(Button) findViewById(R.id.clickphoto);
        uploadPhoto=(Button) findViewById(R.id.uploadStudentPhoto);
        choosePhoto=(Button) findViewById(R.id.choosePhoto);
        imageView=(ImageView) findViewById(R.id.imgstdPhoto);

        progressDialog=new ProgressDialog(this);

        storage  = FirebaseStorage.getInstance();
                // Create a storage reference from our app
        storageRef = storage.getReference().child("StudentPhotos");

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbStudent = firebaseDatabase.getReference("Student");
        dbDataSet = firebaseDatabase.getReference("Dataset");

        Intent intent=getIntent();
        String id=intent.getStringExtra("studentId");
        stdId.setText(id);

        clickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click photo from camera
                filePath=null;
                Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1,CAMERA_REQ_CODE);
            }
        });


        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose from media
                data1=null;
                chooseImage();
            }
        });


        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload photo
                uplodImage();
            }
        });
    }

    private void uplodImage() {


        String std=stdId.getText().toString();
        final String finalid=std.replaceAll("\\s+","");


        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(finalid).exists() && !finalid.isEmpty()){
                    //uploadtodatabase(finalid);
                    if(filePath != null)
                    {

                        final ProgressDialog progressDialog = new ProgressDialog(AddStudentPhoto.this);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        final StorageReference ref = storageRef.child(UUID.randomUUID().toString());
                        ref.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddStudentPhoto.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Uri downloadUrl = uri;
                                                Log.d("STRING  url in main",downloadUrl.toString());
                                                addDataSet(downloadUrl,finalid);
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddStudentPhoto.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                });
                    }
                    if(data1!=null){

                        final ProgressDialog progressDialog = new ProgressDialog(AddStudentPhoto.this);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        final StorageReference ref = storageRef.child(UUID.randomUUID().toString());
                        ref.putBytes(data1)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddStudentPhoto.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Uri downloadUrl = uri;
                                                Log.d("STRING  url in main",downloadUrl.toString());
                                                addDataSet(downloadUrl,finalid);
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddStudentPhoto.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                });

                    }

                }
                else {
                    Toast.makeText(AddStudentPhoto.this, "Student not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void addDataSet(final Uri downloadUrl, final String studentid) {

        dbDataSet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String strurl=downloadUrl.toString();
                Log.d("STRING URL",strurl);
                Vector v=new Vector();
                v.add(strurl);
                v.add("Test");
                DataSet data=new DataSet(v);
                dbDataSet.child(studentid).setValue(data);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        if (requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK
                ){

            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data1 = baos.toByteArray();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}
