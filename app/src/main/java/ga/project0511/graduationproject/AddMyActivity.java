package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class AddMyActivity extends AppCompatActivity {
    public static final String KEY_USER_DATA = "user";

    public static final String NO_PARTICIPANTS = "No participants";
    public static final String NOT_ENDED_YET = "Not ended yet";
    public static final String ENDED_ACTIVITY = "Activity ended";

    User login_user;

    ImageView button_back;
    Button button_add;
    EditText editText_activity_name, editText_plant_name, editText_createdAt,
            editText_manager_id, editText_participants_id;


    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService imyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my);

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        imyService = retrofitClient.create(IMyService.class);

        //Get user infomation
        Intent intent = getIntent();
        login_user = User.getUserInfoFromIntent(intent);

        //Init view
        button_back = findViewById(R.id.addActivity_back);
        button_add = findViewById(R.id.addActivity_add);

        editText_activity_name = findViewById(R.id.addActivity_editText_activityName);
        editText_plant_name = findViewById(R.id.addActivity_editText_plantName);
        editText_createdAt = findViewById(R.id.addActivity_editText_createdAt);
        editText_manager_id = findViewById(R.id.addActivity_editText_manager);
        editText_participants_id = findViewById(R.id.addActivity_editText_participants);

        // 팀장 속성 EditText 수정 불가
        editText_manager_id.setText(login_user.getId());
        editText_manager_id.setInputType(InputType.TYPE_NULL);
        //editText_manager_id.setFocusable(false);
        //editText_manager_id.setClickable(false);

        // 등록하기 버튼 리스너 등록
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String activity_name = editText_activity_name.getText().toString();
                String plant_name = editText_plant_name.getText().toString();
                String createdAt = editText_createdAt.getText().toString();
                String manager_id = editText_manager_id.getText().toString();
                String participants_id = editText_participants_id.getText().toString();

                Boolean isNotEmpty = true;
                Boolean isFormattedData = true;

                // 텍스트 조건 검사
                // 1. 빈 항목들이 있는지 조사
                if(TextUtils.isEmpty(activity_name)) {
                    Toast.makeText(AddMyActivity.this, "활동 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    isNotEmpty = false;
                }

                else if(TextUtils.isEmpty(plant_name)) {
                    Toast.makeText(AddMyActivity.this, "식물명을 입력하세요", Toast.LENGTH_SHORT).show();
                    isNotEmpty = false;
                }

                else if(TextUtils.isEmpty(createdAt)) {
                    Toast.makeText(AddMyActivity.this, "활동 시작일을 입력하세요", Toast.LENGTH_SHORT).show();
                    isNotEmpty = false;
                }

                else if(TextUtils.isEmpty(participants_id)) {
                    //Toast.makeText(AddMyActivity.this, "참가자 ID를 입력하세요", Toast.LENGTH_SHORT).show();
                    participants_id = NO_PARTICIPANTS;
                    isNotEmpty = true;
                }

                // 2. 각 필드 별 추가 조건 검사
                if(isNotEmpty) {

                    boolean nextCondition = true;

                    // 입력된 시작 일자 문자열 형식 검사
                    if(nextCondition) {
                        String formattedDate = "(19|20)\\d{2}.(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[01])";
                        isFormattedData = createdAt.matches(formattedDate);

                        if(!isFormattedData) {
                            Toast.makeText(AddMyActivity.this, "형식에 맞게 활동 시작일을 입력하세요\nYYYY.MM.DD",
                                    Toast.LENGTH_SHORT).show(); }

                        nextCondition = false;

                    }

                    if(nextCondition) { }
                }

                if(isNotEmpty && isFormattedData) {
                    insertActivityInfo(activity_name, plant_name, createdAt,
                            manager_id, participants_id); }
            }
        });

        // 뒤로 가기 버튼 리스너 등록
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void insertActivityInfo(String activity_name, String plant_name, String createdAt,
                                      String manager_id, String participants_id) {

        compositeDisposable.add(imyService.registerActivity(activity_name, plant_name, createdAt,
                NOT_ENDED_YET, manager_id, participants_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean success = jsonObject.getBoolean("success");

                        if(success) {
                            JSONObject jsonObject_data = jsonObject.getJSONObject("data");

                            String activity_name = jsonObject_data.getString("name_activity");
                            String plant_name = jsonObject_data.getString("name_plant");
                            String createdAt = jsonObject_data.getString("created_at");
                            String endedAt = jsonObject_data.getString("ended_at");
                            String manager_id = jsonObject_data.getString("id_manager");
                            String participant_id = jsonObject_data.getString("id_participants");

                            Toast.makeText(AddMyActivity.this, activity_name + "\n"
                                    + plant_name + "\n"
                                    + createdAt + "\n"
                                    + endedAt + "\n"
                                    + manager_id + "\n"
                                    + participant_id, Toast.LENGTH_SHORT).show();

                            //삽입 후 활동 추가 화면 종료
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        else {
                            String msg = jsonObject.getString("message");

                            Toast.makeText(AddMyActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

}
