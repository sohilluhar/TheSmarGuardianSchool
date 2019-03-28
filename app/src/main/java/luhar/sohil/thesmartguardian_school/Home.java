package luhar.sohil.thesmartguardian_school;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Common.Common;

public class Home extends AppCompatActivity {


    TextView wlcmUser;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference student, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        wlcmUser = (TextView) findViewById(R.id.wlcmUser);


        if (Common.haveInternet(this)) {
            String msg = "Hello ! " + Common.currentAdmin.getName();
            wlcmUser.setText(msg);
        }
        else
        {
            Toast.makeText(this, "Please check your internet connection! ", Toast.LENGTH_SHORT).show();
        }
    }
}