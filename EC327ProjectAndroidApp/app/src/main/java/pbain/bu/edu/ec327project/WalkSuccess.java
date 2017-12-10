package pbain.bu.edu.ec327project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.xml.xpath.XPath;

public class WalkSuccess extends AppCompatActivity {

    private Button homeButton;
    private String distanceWalked;
    private String userName;
    private double totalXP;
    private double lastXP;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private double roundedDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_success);


        /**getting username**/
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            distanceWalked = extras.getString("key");
            userName = extras.getString("key2");
        }

        homeButton = findViewById(R.id.homeBttn);
        homeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(WalkSuccess.this, PlanWorkout.class);
                myIntent.putExtra("key", userName);
                WalkSuccess.this.startActivity(myIntent);
            }
        });


        /**pull values from database and update the TextView objects in the screen**/
        /**trying this out **/
        ref.child(userName).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        userID grabbedUser = dataSnapshot.getValue(userID.class);

                        grabbedUser.XP = dataSnapshot.child("XP").getValue(String.class);
                        lastXP = Double.parseDouble(grabbedUser.XP);

                        grabbedUser.XP = dataSnapshot.child("XP").getValue(String.class);
                        //

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        /**empty for now**/
                    }
                });

        /**calculating XP using proprietary algorithm, writes to database**/


        totalXP = Math.round(lastXP + (Double.parseDouble(distanceWalked) * 1.5 + 12));
        ref.child(userName).child("XP").setValue(String.valueOf(totalXP));

        roundedDistance = Math.round(Double.parseDouble(distanceWalked) / 100);
        /**sets value of distance walked, writes to database**/
        ref.child(userName).child("distanceWalked").setValue(String.valueOf(roundedDistance));

    }
}
