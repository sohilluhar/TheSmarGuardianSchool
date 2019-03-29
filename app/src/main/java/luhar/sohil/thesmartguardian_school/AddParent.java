package luhar.sohil.thesmartguardian_school;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import Model.Parent;

public class AddParent extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbParent;

    EditText pname,pphone,pmail,ppass;
    Button addp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);


        firebaseDatabase = FirebaseDatabase.getInstance();
        dbParent = firebaseDatabase.getReference("Parent");


        pname=(EditText)findViewById(R.id.etParentName);
        pphone=(EditText)findViewById(R.id.etParentPhone);
        pmail=(EditText)findViewById(R.id.etParentEmail);
        ppass=(EditText)findViewById(R.id.etParentPass);

        addp=(Button) findViewById(R.id.btnParentRegister);

        addp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParenttodb();
            }
        });



    }

    private void addParenttodb() {

        final String name=pname.getText().toString();
        final String phone=pphone.getText().toString();
        final String mail=pmail.getText().toString();
        final String pass=ppass.getText().toString();
        if(name.isEmpty()||phone.isEmpty()||mail.isEmpty()||pass.isEmpty()){
            Toast.makeText(AddParent.this, "Please fill all details ", Toast.LENGTH_SHORT).show();
        }
        else{
            dbParent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(phone).exists()){

                        Toast.makeText(AddParent.this, "User Already Exists", Toast.LENGTH_SHORT).show();

                    }else {

                        Parent user=new Parent(name,mail,pass,phone);

                        dbParent.child(phone).setValue(user);
                        Common.currentParent=user;

                        Toast.makeText(AddParent.this, "Parent Added ", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
