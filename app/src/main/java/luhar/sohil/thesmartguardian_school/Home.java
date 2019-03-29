package luhar.sohil.thesmartguardian_school;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Common.Common;

public class Home extends AppCompatActivity {


    TextView wlcmUser;
    Button addParent;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference student, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        wlcmUser = (TextView) findViewById(R.id.wlcmUser);
        addParent = (Button) findViewById(R.id.addParent);


        if (Common.haveInternet(this)) {
            String msg = "Hello ! " + Common.currentAdmin.getName();
            wlcmUser.setText(msg);

            addParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Home.this,AddParent.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please check your internet connection! ", Toast.LENGTH_SHORT).show();
        }


    }
}