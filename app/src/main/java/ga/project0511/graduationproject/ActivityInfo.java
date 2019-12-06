package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.datatype.Gardening;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public class ActivityInfo extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_ACTIVITY = 201;
    public static final int REQUEST_CODE_ACTIVITY_INFO = 202;

    public static final String KEY_USER_DATA = "user";
    public static final String KEY_ACTIVITY_DATA = "activity";

    User login_user; // 로그인 사용자 정보
    Gardening activity; // 원예 활동 정보

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    ListView listView;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_info);

        // Image, Text, EditText, Button View
        ImageView button_back = findViewById(R.id.activity_info_back);

        TextView textView_name_activity = findViewById(R.id.activity_info_name_activity);

        EditText editText_createdAt = findViewById(R.id.activity_info_text_createdAt);
        EditText editText_participatedAt = findViewById(R.id.activity_info_text_participatedAt);
        EditText editText_participants = findViewById(R.id.activity_info_text_participants);

        // Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        // 리스트 뷰
        listView = findViewById(R.id.activity_info_records_listview);

        Intent intent = getIntent();

        // 회원 정보 가져오기
        login_user = User.getUserInfoFromIntent(intent);

        // 활동 정보 가져오기
        activity = Gardening.getGardeningInfoFromIntent(intent);

        // 가지고 온 정보를 화면에 뿌림
        textView_name_activity.setText(activity.getActivityName());
        editText_createdAt.setText(activity.getCreatedAt());
        editText_createdAt.setInputType(InputType.TYPE_NULL);
        editText_participatedAt.setText(activity.getCreatedAt());
        editText_participatedAt.setInputType(InputType.TYPE_NULL);
        editText_participants.setText(activity.getParticipantsID());
        editText_participants.setInputType(InputType.TYPE_NULL);


        // 뒤로 가기 버튼 리스너 설정
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}
