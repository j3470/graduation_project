package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    public static final int REQUEST_CODE_DICT = 102;
    public static final int REQUEST_CODE_RECOMMEND = 103;
    public static final int REQUEST_CODE_IMAGESEARCH = 104;
    public static final int REQUEST_CODE_MENUGWANLI = 105;

    public static final String KEY_USER_DATA = "user";

    TextView menu_name_main;
    Button main_button_logout;
    Button main_button_menu_dict;
    Button main_button_menu_recommend;
    Button main_button_menu_imagesearch;
    Button main_button_menu_gwanli;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menu_name_main = findViewById(R.id.menu_name_main);
        main_button_logout = findViewById(R.id.main_button_logout);
        main_button_menu_dict = findViewById(R.id.main_button_menu_dict);
        main_button_menu_recommend = findViewById(R.id.main_button_menu_recommend);
        main_button_menu_imagesearch = findViewById(R.id.main_button_menu_imagesearch);
        main_button_menu_gwanli = findViewById(R.id.main_button_menu_gwanli);

        Intent intent = getIntent();
        processIntent(intent);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                        //로그아웃 버튼
                    case R.id.main_button_logout:
                        Intent intent_logout = new Intent();
                        setResult(RESULT_OK, intent_logout);
                        finish();
                        break;
                        //식물 정보 사전
                    case R.id.main_button_menu_dict:
                        Intent intent_dict = new Intent(MainMenu.this, Dictionary.class);
                        startActivityForResult(intent_dict, REQUEST_CODE_DICT);
                        //startActivity(intent_dict);
                        break;
                        //선호 작물 추천
                    case R.id.main_button_menu_recommend:
                        Intent intent_recommend = new Intent(MainMenu.this, plant_recommend.class);
                        startActivityForResult(intent_recommend, REQUEST_CODE_RECOMMEND);
                        break;
                        //식물 이미지 검색
                    case R.id.main_button_menu_imagesearch:
                        Intent intent_imagesearch = new Intent(MainMenu.this, ImageSearch.class);
                        startActivityForResult(intent_imagesearch, REQUEST_CODE_IMAGESEARCH);
                        break;
                        //작물 관리
                    case R.id.main_button_menu_gwanli:
                        Intent intent_gwanli = new Intent(MainMenu.this, listing_plants.class);
                        startActivityForResult(intent_gwanli, REQUEST_CODE_MENUGWANLI);
                        break;
                }
            }

        };

        main_button_logout.setOnClickListener(onClickListener);
        main_button_menu_dict.setOnClickListener(onClickListener);
        main_button_menu_recommend.setOnClickListener(onClickListener);
        main_button_menu_imagesearch.setOnClickListener(onClickListener);
        main_button_menu_gwanli.setOnClickListener(onClickListener);

    }

    private void processIntent(Intent intent){
        if(intent!=null) {
            Bundle bundle = intent.getExtras();
            User userInfo = bundle.getParcelable(KEY_USER_DATA);
            if(intent!=null) {
                menu_name_main.setText("환영합니다 "+userInfo.getId()+"님");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
