package allaudin.github.io.yabkprocessor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import allaudin.github.io.yabkprocessor.model.User;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        User model = new User();
        model.setId(100);
        model.setName("ali");
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.USER, model);
        startActivity(intent);
    }
}
