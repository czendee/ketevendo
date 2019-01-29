package com.youtochi.ktvendo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class StoreSignupctivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

/*    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
*/
    EditText _nameText;
    EditText _addressText;
    EditText _emailText;
    EditText _periscopeText;
    EditText _tiendaText;
    EditText _faceText;
    Button _signupButton;
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storesignupctivity);
//        ButterKnife.bind(this);
        _nameText = (EditText) findViewById(R.id.input_name);
//        _addressText = (EditText) findViewById(R.id.input_address);
//        _emailText = (EditText) findViewById(R.id.input_email);
        _tiendaText = (EditText) findViewById(R.id.input_tienda);


        _periscopeText = (EditText) findViewById(R.id.input_periscope);
        _faceText = (EditText) findViewById(R.id.input_face);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and display the map screen
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(StoreSignupctivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Abriendo mapa...");
        progressDialog.show();

        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
//        String email = _emailText.getText().toString();
        String tienda = _tiendaText.getText().toString();
        String periscope = _periscopeText.getText().toString();
        String face = _faceText.getText().toString();

/*

        CRUDTiendaKAddNew th=new CRUDTiendaKAddNew();
        System.out.println("add one Socio - 2");
        DataDescriptorTiendaK pasarLosDatos=  new DataDescriptorTiendaK("0","0",name,tienda,periscope,face,"activo");

        System.out.println("add one Socio - 3"+pasarLosDatos.getName());

        th.elNuevoTiendaK=pasarLosDatos;
        TextView txtText=null;

        txtText =(TextView) findViewById(R.id.textResultadoOperacion);
        System.out.println("add one Socio - 4");
//                th.execute(txtText); // here the result in text will be displayed
        System.out.println("add one Socio 3");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)

            th.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,txtText);
        else
            th.execute(txtText);
        System.out.println("add one Socio - 5");
*/
        // TODO: Implement your own signup logic here.
        // Finish the registration screen and display the map screen
        Intent intent = new Intent(getApplicationContext(),MiMapaUnNuevoTiendaActivity.class);

        intent.putExtra("cual_tienda", name + "");
        intent.putExtra("cual_tipo", tienda + "");
        intent.putExtra("cual_persicope", periscope + "");
        intent.putExtra("cual_face", face + "");
        startActivity(intent);
        finish();

/*        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
*/
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
//        String email = _emailText.getText().toString();
        String tienda = _tiendaText.getText().toString();
        String periscope = _periscopeText.getText().toString();
        String face = _faceText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (tienda.isEmpty() ) {
            _tiendaText.setError("Enter Valid Tienda Type");
            valid = false;
        } else if (tienda.equals("tiendas") ) {
            _tiendaText.setError(null);
            valid = true;
        }else if (tienda.equals("sucursal") ) {
            _tiendaText.setError(null);
            valid = true;
        } else if (tienda.equals("estetica") ) {
            _tiendaText.setError(null);
            valid = true;
        } else{

            _tiendaText.setError("Enter Valid Tienda Type");
            valid = false;
        }


        /*
        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }
*/
        return valid;
    }
}
