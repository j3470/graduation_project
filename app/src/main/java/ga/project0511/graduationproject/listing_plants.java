package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class listing_plants extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_ACTIVITY = 201;

    public static final String KEY_USER_DATA = "user";

    User login_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_plants);

        Intent intent = getIntent();
        login_user = User.getUserInfoFromIntent(intent);

        // 뒤로 가기 버튼
        ImageView button_back = findViewById(R.id.listing_plants_back);
        // 활동 추가 하기 버튼
        ImageView button_add_activity = findViewById(R.id.listing_plants_add);


        // 뒤로 가기 버튼 리스너 설정
        ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        // 활동 추가 하기 버튼 리스너 설정
        ImageView.OnClickListener onClickListener1 = new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listing_plants.this, AddMyActivity.class);
                intent.putExtra(KEY_USER_DATA, login_user);
                startActivityForResult(intent, REQUEST_CODE_ADD_ACTIVITY);

            }
        };

        // 리스너 등록
        button_back.setOnClickListener(onClickListener);
        button_add_activity.setOnClickListener(onClickListener1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
