package ga.project0511.graduationproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import okhttp3.ResponseBody;
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

    private String imgPath;
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

        // DB로부터 진행 중인 모든 참여 활동 정보를 가져와 리스트 뷰 형태로 출력
        listinProgressActivity(login_user.getId());

        /*
        * 리스트 뷰 이벤트 리스너 설정.
        * 이벤트 발생 시 활동 상세 정보 액티비티로 이동
        */
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

    // DB로부터 진행 중인 모든 참여 활동 정보를 가져와 리스트 뷰 어댑터에 담음
    public void listinProgressActivity(String id) {

        activity = new ArrayList<Gardening>();

        compositeDisposable.add(iMyService.searchAllActivities(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if(success) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (jsonArray != null) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject json = jsonArray.getJSONObject(i);

                                    String name_activity = json.getString("name_activity");
                                    String created_at = json.getString("created_at");
                                    String ended_at = json.getString("ended_at");
                                    String name_plant = json.getString("name_plant");
                                    String id_manager = json.getString("id_manager");
                                    String id_participants = json.getString("id_participants");
                                    String imgPath_server = json.getString("imgPath");


                                    Gardening activity_info = new Gardening(name_activity, created_at, ended_at,
                                            name_plant, id_manager, id_participants, imgPath_server, Gardening.NOT_DOWNLOADED_YET);

                                    downImgFile(imgPath_server, name_activity, i);
                                    activity.add(activity_info);
                                }
                            }

                        }

                        // 뷰 어댑터
                        gardeningAdapter = new GardeningAdapter(getApplicationContext(), activity);

                        // 리스트 뷰에 어댑터 등록
                        listView.setAdapter(gardeningAdapter);
                    }
                }));
    }

    private void downImgFile(String filePath, final String activityName, final int position) {
        // download image from server
        compositeDisposable.add(iMyService.downloadImage(filePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        writeResponseBodyToDisk(responseBody, activityName, position);
                    }
                }));
    }

    private boolean writeResponseBodyToDisk(final ResponseBody body, final String activityName, final int position) {

        try {
            // todo change the file location/name
            File storageDir = new File(Environment.getExternalStorageDirectory() +
                    "/activityDownloadImage");
            if(!storageDir.exists()) storageDir.mkdirs();

            String imageFileName = "repImg_"+activityName+"_";
            File imgFile = File.createTempFile(imageFileName, ".jpg", storageDir);

            activity.get(position).setImgPath_local(imgFile.getAbsolutePath());
            //Toast.makeText(getApplicationContext(), activity.get(position).getImgPath_local(), Toast.LENGTH_SHORT).show();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(imgFile);

                while(true) {
                    int read = inputStream.read(fileReader);

                    if ( read == -1 ) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if(inputStream != null) {
                    inputStream.close();
                }

                if(outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
