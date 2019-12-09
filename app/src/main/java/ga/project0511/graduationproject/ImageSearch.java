package ga.project0511.graduationproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class ImageSearch extends AppCompatActivity {

    public static final int PICK_FROM_ALBUM = 201;
    public static final int PICK_FROM_CAMERA = 202;
    public static final int REQUEST_CODE_RESULT = 203;

    User login_user;

    // RxJava & Retrofit
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    // 활동 대표 사진 이미지 파일 관련 객체
    private File tempFile;
    private File tempFile2;
    private MultipartBody.Part file;

    // View
    private ImageView repImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        // ted permission
        tedPermission();

        // retrofit
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Intent intent = getIntent();
        login_user = User.getUserInfoFromIntent(intent);

        // view init
        repImg = (ImageView)findViewById(R.id.imagesearch_repImg);
        ImageView button_back = findViewById(R.id.imagesearch_back);
        Button button_album = findViewById(R.id.imagesearch_button_album);
        Button button_camera = findViewById(R.id.imagesearch_button_camera);
        Button button_search = findViewById(R.id.imagesearch_button_search);

        // 뒤로 가기 버튼 리스너 등록
        ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        button_back.setOnClickListener(onClickListener);

        // 앨범에서 이미지 추가 버튼 리스너 등록
        button_album.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });

        // 카메라에서 사진 촬영 버튼 리스너 등록
        button_camera.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        // 검색 버튼 리스너 등록
        button_search.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempFile == null) {
                    Toast.makeText(getApplicationContext(), "이미지를 지정해주세요", Toast.LENGTH_SHORT).show();
                }
                else
                    requestSearch();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if((resultCode == Activity.RESULT_OK) && (requestCode == REQUEST_CODE_RESULT)) {

            Resources res = getResources();
            BitmapDrawable bitmap = (BitmapDrawable)res.getDrawable(R.drawable.searchplant, null);
            repImg.setImageDrawable(bitmap);

            tempFile = null;
        }


        if((resultCode != Activity.RESULT_OK) && (requestCode == PICK_FROM_CAMERA )) {

            if(tempFile != null) {
                if(tempFile.exists()) {
                    if(tempFile.delete()) {
                        if(tempFile2 != null)
                            tempFile = tempFile2;
                        else
                            tempFile = null;
                    }
                }
            }

            return;
        }

        if((resultCode == Activity.RESULT_OK) && (requestCode == PICK_FROM_ALBUM)) {
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

        } else if((resultCode == Activity.RESULT_OK) && (requestCode == PICK_FROM_CAMERA)) {

            if(tempFile2 != null)
                if(tempFile2.exists())
                    tempFile2 = null;

            setImage();
        }


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

        ImageView imageView = findViewById(R.id.imagesearch_repImg);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Bitmap resizeBm = Bitmap.createScaledBitmap(originalBm, 300, 400,  true);

        imageView.setImageBitmap(resizeBm);
    }

    // 카메라에서 이미지 가져오기
    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {

            if(tempFile != null) {
                if(tempFile.exists())
                    tempFile2 = tempFile;
            }

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

    private void requestSearch() {

        if(tempFile == null) {

        }
        else {
            // 파일과 이미지 타입을 갖고 request body 객체 생성
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), tempFile);

            // request body와 파일 이름, part 이름을 갖고 MultipartBody.part 객체 생성
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", tempFile.getName(), fileReqBody);

            // 부연    설명 텍스트와 텍스트 타입을 갖고 request body 객체 생성
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "activity representation image");

            Toast.makeText(getApplicationContext(), "검색 중입니다. 잠시만 기다려주세요", Toast.LENGTH_LONG).show();
            // 이미지 업로드 요청 스케줄러 등록
            compositeDisposable.add(iMyService.requestImageSearch(part, description)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                //Toast.makeText(getApplicationContext(), "검색 성공", Toast.LENGTH_SHORT).show();
                                JSONObject result = jsonObject.getJSONObject("data");
                                JSONObject predictions = result.getJSONObject("predictions");


                                HashMap<String, Double> values = new HashMap<String, Double>();

                                double adiantum = predictions.getDouble("Adiantum");
                                double agave = predictions.getDouble("Agave attenuata");
                                double alocasia = predictions.getDouble("Alocasia macrorrhizos");
                                double benjamina = predictions.getDouble("Ficus benjamina");
                                double helix = predictions.getDouble("Hedra helix");
                                double lavandula = predictions.getDouble("Lavandula");
                                double narcissus = predictions.getDouble("Narcissus");
                                double pelagonium = predictions.getDouble("Pelargonium inquinans");
                                double pilea = predictions.getDouble("Pilea peperomioides");
                                double rosa = predictions.getDouble("Rosa");

                                values.put("alocasia", alocasia);
                                values.put("agave", agave);
                                values.put("adiantum", adiantum);
                                values.put("benjamina", benjamina);
                                values.put("avandula", lavandula);
                                values.put("narcissus", narcissus);
                                values.put("pilea", pilea);
                                values.put("rose", rosa);
                                values.put("geranium", pelagonium);
                                values.put("ivy", helix);

                                List<HashMap.Entry<String, Double>> list = new LinkedList<>(values.entrySet());

                                Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
                                    @Override
                                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                                        int comparison = (int)((o1.getValue() - o2.getValue()) * -1);
                                        return comparison == 0 ? o1.getKey().compareTo(o2.getKey()) : comparison;
                                    }
                                });

                                HashMap.Entry<String, Double> entry = list.get(0);

                                String plantName = entry.getKey();
                                Double prediction = entry.getValue();

                                Intent intent = new Intent(ImageSearch.this, image_search_result.class);
                                intent.putExtra("plantName", plantName);
                                intent.putExtra("prediction", prediction);
                                intent.putExtra(User.KEY_USER_DATA, login_user);

                                startActivityForResult(intent, REQUEST_CODE_RESULT);

                            }
                            else
                                Toast.makeText(getApplicationContext(), "검색 실패", Toast.LENGTH_SHORT).show();

                        }
                    }));
        }
    }
}
