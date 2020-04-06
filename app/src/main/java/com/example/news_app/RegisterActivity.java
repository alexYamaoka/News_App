package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button register;
    private TextView goToLogin;

    private FirebaseAuth myAuth;
    private DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.register);
        goToLogin = findViewById(R.id.goToLogin);

        myAuth = FirebaseAuth.getInstance();


        goToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String usernameAsString = username.getText().toString().trim();
                String emailAsString = email.getText().toString().trim();
                String passwordAsString = password.getText().toString().trim();
                String confirmPasswordAsString = confirmPassword.getText().toString().trim();



                if (TextUtils.isEmpty(usernameAsString) || TextUtils.isEmpty(emailAsString) || TextUtils.isEmpty(passwordAsString) || TextUtils.isEmpty(confirmPasswordAsString))
                {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else if (passwordAsString.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                }
                else if ( ! passwordAsString.equals(confirmPasswordAsString))
                {
                    Toast.makeText(RegisterActivity.this, "Passwords must be the same!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    register(usernameAsString, emailAsString, passwordAsString);
                }
            }
        });
    }


    private void register(final String usernameAsString, String emailAsString, String passwordAsString)
    {
        myAuth.createUserWithEmailAndPassword(emailAsString, passwordAsString).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    // registration of user is successful
                    FirebaseUser firebaseUser = myAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", usernameAsString.toLowerCase());



                    // add the newly created user account to Firebase database
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                System.out.println("onComplete");
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
