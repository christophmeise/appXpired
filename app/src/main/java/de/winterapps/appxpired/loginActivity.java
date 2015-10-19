package de.winterapps.appxpired;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by D062332 on 18.10.2015.
 */
public class loginActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        final Button buttonLogin = (Button) findViewById(R.id.loginLoginButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, menuActivity.class);
                startActivity(intent);
            }
        });
    }
}
