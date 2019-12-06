package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGIN = 101;
    public static final String KEY_USER_DATA = "user";

    TextView txt_create_account;
    MaterialEditText edt_login_id, edt_login_password;
    Button btn_login;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    User user;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //Init view
        edt_login_id = (MaterialEditText)findViewById(R.id.edt_id);
        edt_login_password = (MaterialEditText)findViewById(R.id.edt_pwd);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edt_login_id.getText().toString(), edt_login_password.getText().toString());

            }
        });

        txt_create_account = (TextView)findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View register_layout = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.register_layout, null);

                new MaterialStyledDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_person)
                        .setTitle("회원 등록")
                        .setDescription("형식에 맞게 회원정보를 입력해주세요")
                        .setCustomView(register_layout)
                        .setNegativeText("취소")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("등록")
                        .onPositive(new MaterialDialog.SingleButtonCallback(){
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which){

                                MaterialEditText edt_register_id = (MaterialEditText)register_layout.findViewById(R.id.edt_id);
                                MaterialEditText edt_register_pwd = (MaterialEditText)register_layout.findViewById(R.id.edt_pwd);
                                MaterialEditText edt_register_email = (MaterialEditText)register_layout.findViewById(R.id.edt_email);
                                MaterialEditText edt_register_nickname = (MaterialEditText)register_layout.findViewById(R.id.edt_nickname);
                                //MaterialEditText edt_register_phone = (MaterialEditText)register_layout.findViewById(R.id.edt_phone);

                                Boolean isFormatted;
                                String emailFormat = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
                                String email = edt_register_email.getText().toString();

                                // ID Validation check
                                if(TextUtils.isEmpty(edt_register_id.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "ID값을 입력해주세요", Toast.LENGTH_SHORT).show();
                                    isFormatted = false;
                                }

                                else if( (edt_register_id.getText().length() < 4) | (edt_register_id.getText().length() > 12)) {
                                    Toast.makeText(MainActivity.this, "ID의 길이는 4~12글자여야 합니다", Toast.LENGTH_SHORT).show();
                                    isFormatted = false;
                                }

                                // Password Validation check
                                else if(TextUtils.isEmpty(edt_register_pwd.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                                    isFormatted = false;
                                }

                                // Email Validation check
                                else if(TextUtils.isEmpty(edt_register_email.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                                    isFormatted = false;
                                }

                                else if(!email.matches(emailFormat)) {
                                    Toast.makeText(MainActivity.this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_LONG).show();
                                    isFormatted = false;
                                }

                                // Nickname Validation check
                                else if(TextUtils.isEmpty(edt_register_nickname.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "별명을 입력해주세요", Toast.LENGTH_SHORT).show();
                                    isFormatted = false;
                                }

                                else if( (edt_register_nickname.getText().length() < 4) | (edt_register_nickname.getText().length() > 12)) {
                                    Toast.makeText(MainActivity.this, "별명의 길이는 4~12글자여야 합니다", Toast.LENGTH_SHORT).show();
                                    isFormatted = false;
                                }
                                else
                                    isFormatted = true;
/*
                                if(TextUtils.isEmpty(edt_register_phone.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "PHONE cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                registerUser(edt_register_id.getText().toString(),
                                        edt_register_pwd.getText().toString(),
                                        edt_register_email.getText().toString(),
                                        edt_register_nickname.getText().toString(),
                                        edt_register_phone.getText().toString());

 */
                                if(isFormatted) {
                                    registerUser(edt_register_id.getText().toString(),
                                            edt_register_pwd.getText().toString(),
                                            edt_register_email.getText().toString(),
                                            edt_register_nickname.getText().toString());
                                }

                            }
                        }).show();
            }
        });
    }

    private void registerUser(String id, String pwd, String email, String nickname){
        compositeDisposable.add(iMyService.registerUser(id, pwd, email, nickname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) {

                                try {
                                    JSONObject json = new JSONObject(response);

                                    boolean success = json.getBoolean("success");

                                    if(success)
                                        Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_LONG).show();
                                    else {
                                        String err = json.getString("message");
                                        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                                    }

                                } catch(JSONException e) {
                                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }


                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                String message = throwable.getMessage();

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            }
                        }));
    }

    private void loginUser(String id, String password)  {


        if(TextUtils.isEmpty(id))
        {
            Toast.makeText(this, "ID cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }


        compositeDisposable.add(iMyService.loginUser(id, password)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Consumer<String>() {
                 @Override
                 public void accept(String response) throws Exception {

                      JSONObject jsonObject = new JSONObject(response);
                      JSONObject jsonObject_data;

                      Boolean success = jsonObject.getBoolean("success");

                      if (success) {
                                           jsonObject_data = jsonObject.getJSONObject("data");

                                           String user = jsonObject_data.getString("id");
                                           String name = jsonObject_data.getString("name");
                                           String email = jsonObject_data.getString("email");
                                           User login_user = new User(user, name, email);

                                           Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                           intent.putExtra(KEY_USER_DATA, login_user);
                                           startActivityForResult(intent, REQUEST_CODE_LOGIN);
                                       } else
                       Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                 }},
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                            }}
                       )
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        edt_login_id.setText("");
        edt_login_password.setText("");
        Toast.makeText(getApplicationContext(), "Logout Complete", Toast.LENGTH_LONG).show();
    }
}

