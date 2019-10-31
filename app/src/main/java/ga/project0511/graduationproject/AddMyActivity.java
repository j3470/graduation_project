package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AddMyActivity extends AppCompatActivity {
    public static final String KEY_USER_DATA = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my);

        Intent intent = getIntent();
        User login_user = User.getUserInfoFromIntent(intent);

        ImageView button_back = findViewById(R.id.addActivity_back);

        ImageView.OnClickListener onClickListener_button_back = new ImageView.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        button_back.setOnClickListener(onClickListener_button_back);
    }

}
