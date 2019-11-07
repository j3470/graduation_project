package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ga.project0511.graduationproject.datatype.User;

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

    User login_user;

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

        //전달 된 인텐트에서 회원 정보 가지고 오기
        login_user = User.getUserInfoFromIntent(intent);

        //화면 좌상단에 환영 문구 텍스트 띄우기
        if(login_user.getId() != null) {
            menu_name_main.setText("환영합니다 "+login_user.getId()+"님");
        }
        else {
            menu_name_main.setText("환영합니다 no User님");
        }

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
                        intent_dict.putExtra(KEY_USER_DATA, login_user);
                        startActivityForResult(intent_dict, REQUEST_CODE_DICT);
                        break;

                        //선호 작물 추천
                    case R.id.main_button_menu_recommend:
                        Intent intent_recommend = new Intent(MainMenu.this, plant_recommend.class);
                        intent_recommend.putExtra(KEY_USER_DATA, login_user);
                        startActivityForResult(intent_recommend, REQUEST_CODE_RECOMMEND);
                        break;

                        //식물 이미지 검색
                    case R.id.main_button_menu_imagesearch:
                        Intent intent_imagesearch = new Intent(MainMenu.this, ImageSearch.class);
                        intent_imagesearch.putExtra(KEY_USER_DATA, login_user);
                        startActivityForResult(intent_imagesearch, REQUEST_CODE_IMAGESEARCH);
                        break;

                        //작물 관리
                    case R.id.main_button_menu_gwanli:
                        Intent intent_gwanli = new Intent(MainMenu.this, listing_plants.class);
                        intent_gwanli.putExtra(KEY_USER_DATA, login_user);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
