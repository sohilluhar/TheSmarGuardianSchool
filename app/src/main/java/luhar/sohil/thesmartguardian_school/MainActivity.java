package luhar.sohil.thesmartguardian_school;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Common.Common;
import Model.Admin;

public class MainActivity extends AppCompatActivity {


    EditText uPhone,uPwd;
    Button btnLogin;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    RelativeLayout rellay1,rellay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000);



        uPhone=(EditText)findViewById(R.id.txtPhone);
        uPwd=(EditText)findViewById(R.id.txtPwd);

        btnLogin=(Button)findViewById(R.id.btnLogin);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admin");


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                check();
            }
        });



    }


    private void check() {

        final String usrPhone=uPhone.getText().toString();
        final String usrpwd=uPwd.getText().toString();
        if(usrpwd.isEmpty()||usrPhone.isEmpty()){
            Toast.makeText(MainActivity.this, "Please fill all details ", Toast.LENGTH_SHORT).show();
        }
        else{

            if(Common.haveInternet(this)) {
                databaseReference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(usrPhone).exists()) {
                            Admin user = dataSnapshot.child(usrPhone).getValue(Admin.class);
                            if (user.getPassword().equals(usrpwd)) {

                                Common.currentAdmin = user;
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "User Not Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else {
                Toast.makeText(this, "Please check your internet connection! ", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
