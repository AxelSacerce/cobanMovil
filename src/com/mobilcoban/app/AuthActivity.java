package com.mobilcoban.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Android login screen Activity
 */
public class AuthActivity extends Activity implements LoaderCallbacks<Cursor> {

    private static final String DUMMY_CREDENTIALS = "as:as";

    private UserLoginTask userLoginTask = null;
    private View loginFormView;
    private View progressView;
    private EditText userText;
    private EditText passwordText;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        userText =  (EditText)findViewById(R.id.txtUsuario);
        loadAutoComplete();

        passwordText = (EditText) findViewById(R.id.txtPassword);
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    initLogin();
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) findViewById(R.id.btnLogin);
        Button salirButton = (Button) findViewById(R.id.btnExit);
        loginButton.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View view) {
                initLogin();
            }
        });
        
        
        salirButton.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
        
    }

    private void loadAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Validate Login form and authenticate.
     */
    public void initLogin() {
        if (userLoginTask != null) {
            return;
        }

        userText.setError(null);
        passwordText.setError(null);

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordText.setError(getString(R.string.invalid_password));
            focusView = passwordText;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(user)) {
            userText.setError(getString(R.string.field_required));
            focusView = userText;
            cancelLogin = true;
        } else if (!isUserValid(user)) {
            userText.setError(getString(R.string.invalid_user));
            focusView = userText;
            cancelLogin = true;
        }

        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            showProgress(true);
            userLoginTask = new UserLoginTask(user, password);
            userLoginTask.execute((Void) null);
        }
    }

    private boolean isUserValid(String user) {
        //add your own logic
        return user.length() >= 2;
    }

    private boolean isPasswordValid(String password) {
        //add your own logic
        //return password.length() > 4;
        return password.length() >= 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

       

    /**
     * Async Login Task to authenticate
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String userStr;
        private final String passwordStr;

        UserLoginTask(String user, String password) {
            userStr = user;
            passwordStr = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //this is where you should write your authentication code
            // or call external service
            // following try-catch just simulates network access
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            //using a local dummy credentials store to authenticate
            String[] pieces = DUMMY_CREDENTIALS.split(":");
            if (pieces[0].equals(userStr) && pieces[1].equals(passwordStr)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            userLoginTask = null;
            //stop the progress spinner
            showProgress(false);

            if (success) {
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                AuthActivity.this.startActivity(intent);
                AuthActivity.this.finish();
            } else {
                // login failure
                passwordText.setError(getString(R.string.incorrect_password));
                passwordText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
            showProgress(false);
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // TODO Auto-generated method stub
        
    }
}