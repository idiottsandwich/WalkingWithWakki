package pbain.bu.edu.ec327project;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//need to basically add this block
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class CreateAccount extends AppCompatActivity {

    /** https://stackoverflow.com/questions/37886301/tag-has-private-access-in-android-support-v4-app-fragmentactivity
     * not sure what TAG does but this line fixes things **/

    private static final String TAG = "MainActivity";

    /**getting instances of firebase**/
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**getting references to each field in the database**/
    DatabaseReference ref = database.getReference();

    /** creating an authentication instance **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;

    /**creating button and texts test bench, not sure that these need to be private, and that they need to be initialized here**/
    private EditText testInputText;
    private Button uploadButton;
    private EditText emailInputText;
    private EditText passwordText;
    private TextView userXPOut;
    private TextView petNameOut;

    private String passTHIS;

    /**Function we might need to write for UI so when user does something/messes up
     * for example, this might print an error message if errors in user input occur **/
    private void updateUI(FirebaseUser user) {

        if (user != null) {
            //do a thing
        } else {
            //do a thing
        }
    }

    /**authentication isn't really working right now, so this variable stores the user's name**/
    String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        /**Text things on device screen**/

        emailInputText = (EditText) findViewById(R.id.emailEditText);
        passwordText = (EditText) findViewById(R.id.passwordEditText);
        testInputText = (EditText) findViewById(R.id.usernameEditText);

        /**Authentication setup**/
        mAuth = FirebaseAuth.getInstance();
        //mAuth.addAuthStateListener(mAuthListener);//--->this is crashing code, not sure why
        FirebaseUser currentUser = mAuth.getCurrentUser(); //this needs to be here to check if user is already signed in

        /**12/6/17-planning to make this work later--error checking for user**/
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //waitForDebugger();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }

                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        /**this button uploads the user input data to the database by calling the 'registerUser' method**/
        Button uploadButton = findViewById(R.id.createAccountBttn);

        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                /**why do we need to make this a listener? explained in here: https://stackoverflow.com/questions/40015323/how-to-retrieve-value-of-a-field-in-firebase **/
                passTHIS = registerUser(); //calls method defined below
                //mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent myIntent = new Intent(CreateAccount.this, NamePet.class);
                myIntent.putExtra("key", passTHIS); //Optional parameters
                CreateAccount.this.startActivity(myIntent);

            }//end of onclick
        });
    }

    private String registerUser() {

        String petName = "Wakki"; //dummy value for now
        double walkedDistance = 4;
        double userXP = walkedDistance * 12; //dummy value for now

        //getting email and password from edit texts
        System.out.println("Registering user");
        final String userName = testInputText.getText().toString();
        String email = emailInputText.getText().toString();
        String password = passwordText.getText().toString();

        //writing values to database
        ref.child(userName);
        ref.child(userName).child("Email").setValue(email);
        ref.child(userName).child("Password").setValue(password);
        ref.child(userName).child("PetName").setValue(petName);
        ref.child(userName).child("XP").setValue("0");
        ref.child(userName).child("DistanceTraveled").setValue("0");

        //USERNAME = userName;
            /*
            //checking if email and passwords are empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
                return;
            }
            */

        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if(currentUser!=null)
                            {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userName).build();
                                currentUser.updateProfile(profileUpdates);
                                updateUI(currentUser);
                                Toast.makeText(CreateAccount.this, "User Created.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


        return userName;
    }
}
