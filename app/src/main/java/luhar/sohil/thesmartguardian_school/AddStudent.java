package luhar.sohil.thesmartguardian_school;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Common.Common;
import Model.Message;
import Model.Parent;
import Model.Student;

public class AddStudent extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbParent,dbStudent,dbMessage;

    EditText stdname,stdid,stddiv,stdstandard,stdParentPhone;
    Button addstd;

    String  parentphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbParent = firebaseDatabase.getReference("Parent");
        dbStudent = firebaseDatabase.getReference("Student");
        dbMessage = firebaseDatabase.getReference("Message");



        stdParentPhone=(EditText)findViewById(R.id.etstudentParentPhone);
        stdname=(EditText)findViewById(R.id.etStudentName);
        stdid=(EditText)findViewById(R.id.etStudentId);
        stddiv=(EditText)findViewById(R.id.etStudentDivison);
        stdstandard=(EditText)findViewById(R.id.etStudentStd);

        addstd=(Button) findViewById(R.id.btnStudentRegistration);


        Intent intent=getIntent();
        parentphone=intent.getStringExtra("parentPhone");
        stdParentPhone.setText(parentphone);

        addstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParent();
            }
        });
    }

    private void checkParent() {

        String phone1=stdParentPhone.getText().toString();
        final String phone=phone1.replaceAll("\\s+","");
        Log.d("Parent Phone",phone);


        dbParent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(phone).exists() && !phone.isEmpty()){
                    addStudenttodb();
                }else {
                    Toast.makeText(AddStudent.this, "Parent not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addStudenttodb() {
        String phone1=stdParentPhone.getText().toString();
       final String phone=phone1.replaceAll("\\s+","");
        final String name=stdname.getText().toString();
        final String id=stdid.getText().toString();
        final String div=stddiv.getText().toString();
        final String std=stdstandard.getText().toString();
        final String status="A";

        if(name.isEmpty()||id.isEmpty()||div.isEmpty()||std.isEmpty()){
            Toast.makeText(AddStudent.this, "Please fill all details ", Toast.LENGTH_SHORT).show();
        }
        else{

            dbStudent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if(dataSnapshot.child(id).exists()){
                        Toast.makeText(AddStudent.this, "Student exist", Toast.LENGTH_SHORT).show();
                    }else {

                        Student student=new Student(div,id,name,phone,std,status);

                        dbStudent.child(id).setValue(student);


                        Toast.makeText(AddStudent.this, "Student Added ", Toast.LENGTH_SHORT).show();
                        //finish();
                        addmessageStudent(id);
                        Intent intent=new Intent(AddStudent.this,AddStudentPhoto.class);
                        intent.putExtra("studentId",id);
                        startActivity(intent);
                        finish();


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void addmessageStudent(final String id) {

        dbMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Message msg=new Message("Date","message","photo");
                dbMessage.child(id).setValue(msg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
