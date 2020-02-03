package allaudin.github.io.yabkprocessor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import allaudin.github.io.yabkprocessor.model.User;


public class UserActivity extends AppCompatActivity {

    public static final String USER = "user";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        User user = getIntent().getParcelableExtra(USER);

        TextView result = (TextView) findViewById(R.id.result);
        result.setText(
                user.getId() + " " + user.getName()
        );

    }
}
