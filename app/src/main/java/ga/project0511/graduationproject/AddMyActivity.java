package ga.project0511.graduationproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.datatype.Gardening;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;


public class AddMyActivity extends AppCompatActivity {

    public static final int PICK_FROM_ALBUM = 201;
    public static final int PICK_FROM_CAMERA = 202;

    public static final String KEY_USER_DATA = "user"; //

    public static final String NO_PARTICIPANTS = "No participants";
    public static final String NOT_ENDED_YET = "Not ended yet";
    public static final String ENDED_ACTIVITY = "Activity ended";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    User login_user;

    ImageView button_back;
    Button button_add, button_album, button_camera;
    EditText editText_activity_name, editText_plant_name, editText_createdAt,
            editText_manager_id, editText_participants_id;

    // 활동 대표 사진 이미지 파일 관련 객체
    private File tempFile;
    private MultipartBody.Part file;
    private String imgPath_server;

    // 활동 정보 업로드 성공 여부 & 이미지 업로드 여부 플래그
    private boolean isUploaded;
    private boolean isInserted;


    @Override
    protected void onDestroy() {
        // 활동 정보 등록 없이 액티비티 종료 시 업로드되었던 이미지 파일 삭제
        if(!isUploaded & isInserted) {
            compositeDisposable.add(iMyService.deleteImgFile(imgPath_server)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {

                        }
                    })
            );
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my);

        // ted permission
        tedPermission();

        // Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        // Get user infomation
        Intent intent = getIntent();
        login_user = User.getUserInfoFromIntent(intent);

        // Set isUploaded & isNotImageUploaded flag to false
        isUploaded = false;
        isInserted = false;

        // Init view
        button_back = findViewById(R.id.addActivity_back);
        button_add = findViewById(R.id.addActivity_add);
        button_album = findViewById(R.id.addActivity_button_getFromAlbum);
        button_camera = findViewById(R.id.addActivity_button_getByCamera);

        editText_activity_name = findViewById(R.id.addActivity_editText_activityName);
        editText_plant_name = findViewById(R.id.addActivity_editText_plantName);
        editText_createdAt = findViewById(R.id.addActivity_editText_createdAt);
        editText_manager_id = findViewById(R.id.addActivity_editText_manager);
        editText_participants_id = findViewById(R.id.addActivity_editText_participants);

        // 팀장 속성 EditText 수정 불가
        editText_manager_id.setText(login_user.getId());
        editText_manager_id.setInputType(InputType.TYPE_NULL);

        // 앨범에서 이미지 추가 버튼 리스너 등록
        button_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbum();
            }
        });

        // 카메라에서 사진 촬영하기 버튼 리스너 등록
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

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

                else if(imgPath_server == null) {
                    Toast.makeText(AddMyActivity.this, "대표 이미지를 지정해주세요", Toast.LENGTH_SHORT).show();
                    isNotEmpty = false;
                }

                else if(TextUtils.isEmpty(participants_id)) {
                    // 참가자 구현 시 이후 수정 필요
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
                            manager_id, participants_id, imgPath_server);

                    /*
                    * My 엑티비티의 listview 새로 고침을 하기 위한
                    * ArrayList에 항목 추가
                    */

                    listing_plants.activity.add(new Gardening(activity_name, createdAt, Gardening.NOT_ENDED_YET,
                            plant_name, manager_id, participants_id, imgPath_server, Gardening.NOT_DOWNLOADED_YET));
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if(tempFile.exists()) {
                    if(tempFile.delete()) {
                        //og.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if(requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();

            Cursor cursor = null;

            try{ // Uri 스키마를 content:/// 에서 file:/// 로 변경
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
                //Toast.makeText(getApplicationContext(), cursor.getString(column_index), Toast.LENGTH_SHORT).show();
            } finally {
                 if (cursor != null) {
                     cursor.close();
                 }
            }

            setImage();
            uploadImageFile();

        } else if(requestCode == PICK_FROM_CAMERA) {

            setImage();
            uploadImageFile();
        }
    }

    // 입력 정보를 DB의 activity의 컬렉션에 추가
    public void insertActivityInfo(String activity_name, String plant_name, String createdAt,
                                      String manager_id, String participants_id, String imgPath) {

        // 입력 정보 업로드 스케줄러 등록
        compositeDisposable.add(iMyService.registerActivity(activity_name, plant_name, createdAt,
                NOT_ENDED_YET, manager_id, participants_id, imgPath)
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
                            String imgPath_server = jsonObject_data.getString("imgPath");

                            Toast.makeText(AddMyActivity.this, activity_name + "\n"
                                    + plant_name + "\n"
                                    + createdAt + "\n"
                                    + endedAt + "\n"
                                    + manager_id + "\n"
                                    + participant_id + "\n"
                                    + imgPath_server, Toast.LENGTH_SHORT).show();

                            //삽입 후 활동 추가 화면 종료
                            isUploaded = true;
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
    // ted permission 설정
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    // 앨범에서 이미지 가져오기
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    // 갤러리에서 받아온 이미지 넣기
    private void setImage() {

        ImageView imageView = findViewById(R.id.addActivity_imageView_repImage);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Bitmap resizeBm = Bitmap.createScaledBitmap(originalBm, 120, 120,  true);

        imageView.setImageBitmap(resizeBm);
    }

    // 카메라에서 이미지 가져오기
    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }

        if(tempFile != null) {

            if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "{package name}.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {
                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

    // 카메라에서 찍은 사진을 저장할 파일 생성
    private File createImageFile() throws IOException {

        // Image 파일 이름 : activity_{timestamp}_

        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "activity_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 : activityRepImage
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/activityRepImage");
        if(!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    private void uploadImageFile() {

        // 다른 이미지로 바꿔 업로드 할 경우 기존에 업로드되었던 파일 삭제 요청
        if(imgPath_server != null) {
            compositeDisposable.add(iMyService.deleteImgFile(imgPath_server)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {

                        }
                    })
            );
        }

        if(tempFile != null) {
            // 파일과 이미지 타입을 갖고 request body 객체 생성
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), tempFile);

            // request body와 파일 이름, part 이름을 갖고 MultipartBody.part 객체 생성
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", tempFile.getName(), fileReqBody);

            // 부연 설명 텍스트와 텍스트 타입을 갖고 request body 객체 생성
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "activity representation image");

            // 이미지 업로드 요청 스케줄러 등록
            compositeDisposable.add(iMyService.uploadImage(part, description)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                //Toast.makeText(getApplicationContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), imgPath_server, Toast.LENGTH_SHORT).show();

                                JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                                imgPath_server = jsonObject_data.getString("path");
                                isInserted = true;

                            }
                            else
                                Toast.makeText(getApplicationContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show();

                        }
                    }));
        }
    }


}
