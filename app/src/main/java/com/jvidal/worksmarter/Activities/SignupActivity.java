package com.jvidal.worksmarter.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jvidal.worksmarter.R;

public class SignupActivity extends AppCompatActivity {


    private EditText edtEmail;
    private EditText edtName;
    private TextInputLayout etPasswordLayout;
    private TextInputEditText edtPassword;
    private EditText edtNumber;
    private Button btnCreate;
    private CircularProgressView progressBar;

    private LinearLayout ll_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        edtEmail = (EditText) findViewById(R.id.edt_email);
        etPasswordLayout = (TextInputLayout) findViewById(R.id.etPasswordLayout);
        edtPassword = (TextInputEditText) findViewById(R.id.edt_password);
        edtNumber = (EditText) findViewById(R.id.edt_number);
        edtName = (EditText) findViewById(R.id.edt_name);
        btnCreate = (Button) findViewById(R.id.btn_create);
        progressBar = (CircularProgressView) findViewById(R.id.progress_view);
        progressBar.setVisibility(View.INVISIBLE);
        ll_login = findViewById(R.id.ll_login);
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SingInActivity.class));
                finish();
            }
        });


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar.setVisibility(View.VISIBLE);

                BackendlessUser user = new BackendlessUser();
                user.setEmail(edtEmail.getText().toString());
                user.setPassword(edtPassword.getText().toString());
                user.setProperty("contact", edtNumber.getText().toString());
                user.setProperty("name", edtName.getText().toString());


                Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "User has been registered", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    public Context getContext() {
        return this;
    }
}
