package com.jayseeofficial.rssconsumer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.jayseeofficial.rssconsumer.R;
import com.jayseeofficial.rssconsumer.rest.FeedApi;
import com.jayseeofficial.rssconsumer.rest.ServerError;
import com.jayseeofficial.rssconsumer.rest.ServerResult;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewUserActivity extends ActionBarActivity {

    ProgressDialog dialog = null;

    private FeedApi.FeedRequestListener listener = new FeedApi.FeedRequestListener() {
        @Override
        public void onRequestCompleted(FeedApi.Request requestCode, ServerResult result) {
            // Filter for new user requests only
            if (requestCode.equals(FeedApi.Request.NEW_USER)) {
                dialog.hide();
                txtError.setText("Success!");
                finish();
            }
        }

        @Override
        public void onRequestError(FeedApi.Request request, ServerError error) {
            dialog.hide();
            txtError.setText("Somthing went wrong!\n" + error.getErrorMessage());
        }
    };

    @InjectView(R.id.txt_username)
    EditText txtUsername;
    @InjectView(R.id.txt_password)
    EditText txtPassword;
    @InjectView(R.id.txt_password_confirm)
    EditText txtPasswordConfirm;
    @InjectView(R.id.txt_error)
    TextView txtError;

    @OnClick(R.id.btn_new_user)
    void newUser() {
        if (txtPassword.getText().toString().equals(txtPasswordConfirm.getText().toString())) {
            FeedApi.getInstance(this).setUsername(txtUsername.getText().toString());
            FeedApi.getInstance(this).setPassword(txtPassword.getText().toString());
            FeedApi.getInstance(this).requestNewUser();
            dialog.show();
        } else {
            txtError.setText("Passwords don't match!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        ButterKnife.inject(this);
        FeedApi.getInstance(this).registerListener(listener);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Creating new user.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
