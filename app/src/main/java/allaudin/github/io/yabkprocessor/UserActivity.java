package allaudin.github.io.yabkprocessor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import allaudin.github.io.yabkprocessor.model.UserModel;


public class UserActivity extends AppCompatActivity {

    public static final String USER = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        UserModel user = getIntent().getParcelableExtra(USER);
        ((TextView)findViewById(R.id.result)).setText(user.toString());
    }
}
