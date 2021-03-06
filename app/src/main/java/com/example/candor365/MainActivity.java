package com.example.candor365;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.ErrorCodes;
import java.util.Arrays;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 472;
    @BindView(android.R.id.content) View mRootView;

    private Button googleSignInButton;

    private View.OnClickListener googleSignInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            googleSignInButtonClicked();
        }
    };

    private void googleSignInButtonClicked(){
        signIn();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        googleSignInButton = (Button) findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(googleSignInListener);
        if (mAuth.getCurrentUser() != null) {
            //signed in
//            Toast.makeText(getApplicationContext(),"Already Signed In", Toast.LENGTH_SHORT).show();
            startActivity(SignedInActivity.createIntent(this, null));
        } else {
            // not signed in
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build())).build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "Result Code "+ resultCode, Toast.LENGTH_SHORT).show();
                startActivity(SignedInActivity.createIntent(this, response));
//                Toast.makeText(this, "Logging IN", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
//                    Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();

                    return;
                }

//                Toast.makeText(this, "It just Barfed " + resultCode, Toast.LENGTH_SHORT).show();
                //Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, MainActivity.class);
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    private void signIn(){
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build())).build(),
                RC_SIGN_IN);
    }
}
