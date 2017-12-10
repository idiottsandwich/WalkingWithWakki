package pbain.bu.edu.ec327project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.jar.Attributes;

public class NamePet extends AppCompatActivity {

    /**create layout variables**/
    private Button homeButton;
    private EditText petNameInput;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_pet);

        /**need to add auth state listeners**/

        /**getting username**/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("key");
            //The key argument here must match that used in the other activity
        }

        homeButton = (Button)findViewById(R.id.homeBttn);
        petNameInput = (EditText)findViewById(R.id.nameEditText);

        homeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String petString = petNameInput.getText().toString();
                ref.child(userName).child("PetName").setValue(petString);

                /**change to next activity after naming pet**/
                Intent myIntent = new Intent(NamePet.this, PlanWorkout.class);
                myIntent.putExtra("key", userName); //Optional parameters
                NamePet.this.startActivity(myIntent);
                //startActivity( new Intent(NamePet.this.getActivity(), MapsActivity.class));
            }
        });
    }
}