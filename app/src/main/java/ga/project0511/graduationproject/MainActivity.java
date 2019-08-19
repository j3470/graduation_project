package ga.project0511.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText edt_login_id, edt_login_password;
    Button btn_login;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

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
                loginUser(edt_login_id.getText().toString(),
                        edt_login_password.getText().toString());
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
                        .setTitle("REGISTRATION")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback(){
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which){

                                MaterialEditText edt_register_id = (MaterialEditText)register_layout.findViewById(R.id.edt_id);
                                MaterialEditText edt_register_pwd = (MaterialEditText)register_layout.findViewById(R.id.edt_pwd);
                                MaterialEditText edt_register_email = (MaterialEditText)register_layout.findViewById(R.id.edt_email);
                                MaterialEditText edt_register_nickname = (MaterialEditText)register_layout.findViewById(R.id.edt_nickname);
                                MaterialEditText edt_register_phone = (MaterialEditText)register_layout.findViewById(R.id.edt_phone);

                                if(TextUtils.isEmpty(edt_register_id.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "ID cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(edt_register_pwd.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "PASSWORD cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(edt_register_email.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "EMAIL cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(edt_register_nickname.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "NICKNAME cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(edt_register_phone.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "PHONE cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registerUser(edt_register_id.getText().toString(),
                                        edt_register_pwd.getText().toString(),
                                        edt_register_email.getText().toString(),
                                        edt_register_nickname.getText().toString(),
                                        edt_register_phone.getText().toString());

                            }
                        }).show();
            }
        });
    }

    private void registerUser(String id, String pwd, String email, String nickname, String phone){
        compositeDisposable.add(iMyService.registerUser(id, pwd, email, nickname, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void loginUser(String id, String password) {
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

        compositeDisposable.add(iMyService.loginUser(id,password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String response) throws Exception {
                Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }));
    }
}

