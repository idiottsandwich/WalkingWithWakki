package pbain.bu.edu.ec327project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlanWorkout extends AppCompatActivity {

    /**creating variables for objects within layout file**/
    private Button workoutButton;
    private String userName;
    private TextView petNameDisplay;
    private TextView XPDisplay;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String XP;
    private String petNamePull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_workout);

        /**need to add auth state listeners**/


        /**getting username**/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("key");
            //The key argument here must match that used in the other activity
        }

        petNameDisplay = (TextView) findViewById(R.id.petNameTextView);
        XPDisplay = (TextView) findViewById(R.id.xpTextView);


        /**pull values from database and update the TextView objects in the screen**/
        /**trying this out **/
         ref.child(userName).addListenerForSingleValueEvent(
         new ValueEventListener()
         {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                userID grabbedUser = dataSnapshot.getValue(userID.class);

                grabbedUser.PetName = dataSnapshot.child("PetName").getValue(String.class);
                grabbedUser.XP = dataSnapshot.child("XP").getValue(String.class);
                petNamePull = grabbedUser.PetName;
                XP = grabbedUser.XP;

                /**updating the names of the TextView objects**/
                XPDisplay.setText(XP);
                petNameDisplay.setText(petNamePull);
            }

             @Override
             public void onCancelled(DatabaseError databaseError)
             {
                 /**empty for now**/
             }
         });

         /**temp output**/
        Toast.makeText(PlanWorkout.this, userName,
                Toast.LENGTH_SHORT).show();


        /**pressing the workoutButton opens the MapsActivity, which runs on the Google Maps API**/
        workoutButton = (Button) findViewById(R.id.workoutBttn);
        workoutButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(PlanWorkout.this, MapsActivity.class);
                myIntent.putExtra("key", userName);
                PlanWorkout.this.startActivity(myIntent);
            }
        });

    }
}
