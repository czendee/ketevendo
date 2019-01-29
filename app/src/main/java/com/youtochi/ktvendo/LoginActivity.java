package com.youtochi.ktvendo;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

//    @BindView(R.id.input_email) EditText _emailText;
//    @BindView(R.id.input_password) EditText _passwordText;
//    @BindView(R.id.btn_login) Button _loginButton;
//    @BindView(R.id.link_signup) TextView _signupLink;
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;

    public DataDescriptorSocio socioobtenido;
    public String socioStr = " ";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ButterKnife.bind(this);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText= (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);
        System.out.println("login oncretae 01 ");
// logic to get the phone number of this device, permissions are needed
   //start
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        _emailText.setText(mPhoneNumber);
   //end
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Signupctivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
        System.out.println("login  02 ");
        if (!validate()) {
            System.out.println("login  03 failed ");
            onLoginFailed();
            return;
        }
        System.out.println("login   ok 04 ");
        _loginButton.setEnabled(false);
        System.out.println("login  05 ");
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        System.out.println("login  06 ");
        String mobile = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        ////////////////////////////////////////////////////////////////////////////


        //comando que indica se hara videocall en whatsapp, asi controlara el robot remotoe
        CRUDSocioGetOneSocio th=new CRUDSocioGetOneSocio();
        Constants constantito= new Constants();
        th.parametroURL=constantito.URL_ADD_NEW_SOCIO+"/"+mobile;


        socioobtenido=th.socioobtenido;
        socioStr=th.socioStr;

        TextView txtText=null;
        System.out.println("get one Socio 3");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)

            th.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            th.execute();
        System.out.println("get one Socio - 4");


        ////////////////////////////////////////////////////////////////////////77777777


        // TODO: Implement your own authentication logic here.
        System.out.println("login  07 ");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("login onActivityResult 01 ");
        if (requestCode == REQUEST_SIGNUP) {
            System.out.println("login onActivityResult 02 ");
            if (resultCode == RESULT_OK) {

                System.out.println("login onActivityResult 0333 ");
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
//                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                Intent i = new Intent(LoginActivity.this, SplashActivity.class);


                startActivity(i);
//                this.finish();
            }
        }
        System.out.println("login onActivityResult 04 ");
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        System.out.println("login  onLoginSuccess 01 ");
        _loginButton.setEnabled(true);
//que no termine, sino uqe haga lo de ir al intent
       finish();
//        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        Intent i = new Intent(LoginActivity.this, SplashActivity.class);

        startActivity(i);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        System.out.println("login validate 01 ");

        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        System.out.println("login validate 02 ");
 /*       if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
*/
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        System.out.println("login validate 03 ");
        return valid;
    }
}
