package ga.project0511.graduationproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.adapter.GardeningAdapter;
import ga.project0511.graduationproject.datatype.Gardening;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class listing_plants extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_ACTIVITY = 201;
    public static final int REQUEST_CODE_ACTIVITY_INFO = 202;

    public static final String KEY_USER_DATA = "user";
    public static final String KEY_ACTIVITY_DATA = "activity";

    User login_user; // 로그인 사용자 정보
    public static ArrayList<Gardening> activity; // 사용자의 참여 활동 정보 객체 ArrayList

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    ListView listView; // ArrayList를 listing 하기 위한 리스트 뷰
    GardeningAdapter gardeningAdapter; // 리스트 뷰 어댑터

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_plants);

        // Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        // 리스트 뷰
        listView = findViewById(R.id.listing_plants_contents);

        // 회원 정보 가져오기
        Intent intent = getIntent();
        login_user = User.getUserInfoFromIntent(intent);

        // DB로부터 진행 중인 모든 참여 활동 정보를 가져와 activity에 저장
        this.listinProgressActivity(login_user.getId());

        // 리스트 뷰 이벤트 리스너 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        gardeningAdapter.getItem(position).getActivityName(),
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(listing_plants.this, ActivityInfo.class);
                intent.putExtra(KEY_USER_DATA, login_user);
                intent.putExtra(KEY_ACTIVITY_DATA, activity.get(position));
                startActivityForResult(intent, REQUEST_CODE_ACTIVITY_INFO);

            }
        });

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
    protected void onResume() {
        super.onResume();
        if(gardeningAdapter != null)
            gardeningAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "상세 정보에서 돌아 오는 도중 오류 발생",
                    Toast.LENGTH_SHORT).show();
        }

        if(requestCode == REQUEST_CODE_ACTIVITY_INFO) {

        }
    }

    // DB로부터 진행 중인 모든 참여 활동 정보를 가져와 activity에 저장하는 함수
    public void listinProgressActivity(String id) {

        activity = new ArrayList<Gardening>();

        compositeDisposable.add(iMyService.searchAllActivities(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i=0; i<jsonArray.length(); i++) {

                            JSONObject json = jsonArray.getJSONObject(i);

                            String name_activity = json.getString("name_activity");
                            String created_at = json.getString("created_at");
                            String ended_at = json.getString("ended_at");
                            String name_plant = json.getString("name_plant");
                            String id_manager = json.getString("id_manager");
                            String id_participants = json.getString("id_participants");


                            Gardening activity_info = new Gardening(name_activity,created_at,ended_at,
                                    name_plant,id_manager,id_participants);

                            activity.add(activity_info);
                        }

                        // 뷰 어댑터
                        gardeningAdapter = new GardeningAdapter(getApplicationContext(), activity);

                        // 리스트 뷰에 어댑터 등록
                        listView.setAdapter(gardeningAdapter);

                    }
                }));
    }

}
