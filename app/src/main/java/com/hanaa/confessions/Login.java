package com.hanaa.confessions;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    EditText etUserName, etPassword;
    Button btnLogin;
    EditText etNameR, etEmailR, etPasswordR, etRePasswordR;
    Button btnSignUp;
    TextView txtClick, already, txtForgot;
    FirebaseAuth firebaseAuth;
    LinearLayout goneLayout, shownLayout;
    private FirebaseAuth mAuth;
    ProgressBar myProgres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login_layout );

        myProgres = findViewById( R.id.myProgres );
        firebaseAuth = FirebaseAuth.getInstance();
        txtClick = findViewById( R.id.txtClick );
        already = findViewById( R.id.already );
        txtForgot = findViewById( R.id.txtForgot );
        String txtF = "Forgot your password? Reset Password";
        String text = "Don't have an account? SignUp";
        String text2 = "Already have an accound? Sign In";
        SpannableString ssss = new SpannableString( txtF );
        //Reset Password
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if( etUserName.getText().toString().isEmpty() ){
                    Toast.makeText( Login.this, "Please Enter your email" , Toast.LENGTH_SHORT).show();
                }else{
                    shownLayout.setVisibility( View.GONE );
                    goneLayout.setVisibility( View.GONE );
                    myProgres.setVisibility( View.VISIBLE );
                    firebaseAuth.sendPasswordResetEmail( etUserName.getText().toString().trim() ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText( Login.this, "Please Check your Email address", Toast.LENGTH_LONG ).show();
                        myProgres.setVisibility( View.GONE );
                        shownLayout.setVisibility( View.VISIBLE );
                        goneLayout.setVisibility( View.VISIBLE );
                    }}).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( Login.this, "Somwthing went wrong.. " , Toast.LENGTH_SHORT).show();
                        myProgres.setVisibility( View.GONE );
                    }
                });}}
        };
        //This one for forgot your password
        ssss.setSpan( clickableSpan2, 22,36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        txtForgot.setText( ssss );
        txtForgot.setMovementMethod( LinkMovementMethod.getInstance() );

        SpannableString ss = new SpannableString( text );

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                shownLayout.animate().translationXBy(-1000f).setDuration(500);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Do something after timing is done
                        goneLayout.animate().translationXBy( -1500f ).setDuration( 350 );
                        //   cv2.animate().translationXBy( -1000f ).setDuration( 350 );

                    }
                }, 20);
            }};

        SpannableString sss = new SpannableString( text2 );
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                shownLayout.animate().translationXBy(1000f).setDuration(500);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Do something after timing is done
                        goneLayout.animate().translationXBy( 1500f ).setDuration( 350 );
                        //   cv2.animate().translationXBy( -1000f ).setDuration( 350 );

                    }
                }, 20);
            }
        };

        sss.setSpan( clickableSpan, 25,32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        already.setText( sss );
        already.setMovementMethod( LinkMovementMethod.getInstance() );

        ss.setSpan( clickableSpan1, 22, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtClick.setText( ss );
        txtClick.setMovementMethod( LinkMovementMethod.getInstance() );

        goneLayout = findViewById( R.id.goneLayout );
        shownLayout = findViewById( R.id.shownLayout );
      //  cv2 = findViewById( R.id.cv2 );

        goneLayout.setTranslationX( 1500f );
     //   cv2.setTranslationX( 1000f );

        etUserName = findViewById( R.id.etUserName );
        etPassword = findViewById( R.id.etPassword );
        btnLogin = findViewById( R.id.btnLogin );
    //    btnRegister = findViewById( R.id.btnRegister );

        etNameR = findViewById( R.id.etName );
        etEmailR = findViewById( R.id.etEmail );
        etPasswordR = findViewById( R.id.etPasswordRegister );
        etRePasswordR = findViewById( R.id.etRePassword );
        btnSignUp = findViewById( R.id.btnSignUp );

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()){
            updateUI(currentUser);
        }else{

        }
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etUserName.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()){
                    Toast.makeText( Login.this, "Please Fill all fields", Toast.LENGTH_LONG ).show();
                }else {

                    myProgres.setVisibility( View.VISIBLE );
                    String email = etUserName.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI( user );
                                        myProgres.setVisibility( View.GONE );
                                    }else{
                                        Toast.makeText( Login.this, "Please verify your Email", Toast.LENGTH_LONG ).show();
                                        myProgres.setVisibility( View.GONE );
                                    }
                                }}
                        }).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( Login.this, e.getMessage(), Toast.LENGTH_LONG ).show();
                        myProgres.setVisibility( View.GONE );
                    }
                });}}});

        //Register an app for the first time.
        btnSignUp.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = etEmailR.getText().toString().trim();
                String password = etPasswordR.getText().toString().trim();
                String rePassword = etRePasswordR.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {

                    Toast.makeText( Login.this, "Please fill all fields", Toast.LENGTH_LONG ).show();
                } else {
                    if (password.equals( rePassword )){

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information

                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener( new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    updateUIR( user );
                                                    Toast.makeText( Login.this, "Account was created, Check your email!", Toast.LENGTH_LONG ).show();
                                                }else {
                                                        Toast.makeText( Login.this, "Something went wrong!", Toast.LENGTH_LONG ).show();
                                                    }}
                                            } );


                                        }}
                                }).addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( Login.this, e.getMessage(), Toast.LENGTH_LONG ).show();
                                updateUIR( null );
                            }});


                    }else {
                        Toast.makeText( Login.this, "Please make sure passwords are equal", Toast.LENGTH_LONG ).show();
                    }
                }}
        });

    }
    private void updateUIR(FirebaseUser currentUser) {

        if(currentUser != null){

            goneLayout.animate().translationXBy( 1000f ).setDuration( 500 );

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Do something after timing is done
                    shownLayout.animate().translationXBy( 1000f ).setDuration( 350 );
                    //   cv2.animate().translationXBy( -1000f ).setDuration( 350 );

                }
            }, 20);

        }}


     private void updateUI(FirebaseUser currentUser) {

        if(currentUser != null){
        Intent intent = new Intent( Login.this, MainActivity.class );
        startActivity( intent );
        Login.this.finish();
    }}

}
