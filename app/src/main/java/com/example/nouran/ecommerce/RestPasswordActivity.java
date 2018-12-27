package com.example.nouran.ecommerce;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;

public class RestPasswordActivity extends AppCompatActivity {

    private EditText mail, userName;
    private Button reset_btn;
    private DBHelper dbHelper;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);
        dbHelper = new DBHelper(RestPasswordActivity.this);

        mail = findViewById(R.id.reset_mail);
        userName = findViewById(R.id.reset_name);
        reset_btn = findViewById(R.id.reset_button);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean ISEXIST = dbHelper.forgetPass(userName.getText().toString(), mail.getText().toString());
                Cursor c = dbHelper.getPass(userName.getText().toString(), mail.getText().toString());
                while (!c.isAfterLast()) {
                    msg = c.getString(4);
                    c.moveToNext();
                }

                if (ISEXIST) {

                    GMailSender.withAccount("nouranmontaser988@gmail.com", "nomoneyyokaa98bloodtoz")
                            .withTitle("Reset Password")
                            .withBody("your new pass :"+msg)
                            .withSender(getString(R.string.app_name))
                            .toEmailAddress(mail.getText().toString()) // one or multiple addresses separated by a comma
                            .withListenner(new GmailListener() {
                                @Override
                                public void sendSuccess() {
                                    Toast.makeText(RestPasswordActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void sendFail(String err) {
                                    Toast.makeText(RestPasswordActivity.this, "Fail: " + err, Toast.LENGTH_SHORT).show();
                                    Log.i("MAILL", err);
                                }
                            })
                            .send();
                    Intent intent = new Intent(RestPasswordActivity.this, SignInActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RestPasswordActivity.this, "Please enter a valid mail and username", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
