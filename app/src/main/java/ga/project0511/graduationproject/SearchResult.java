package ga.project0511.graduationproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.adapter.CommentAdapter;
import ga.project0511.graduationproject.datatype.Plant;
import ga.project0511.graduationproject.datatype.PlantComments;
import ga.project0511.graduationproject.datatype.PlantGuide;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SearchResult extends AppCompatActivity {

    private Plant plant;
    private User login_user;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Retrofit retrofitClient = RetrofitClient.getInstance();
    private IMyService iMyService = retrofitClient.create(IMyService.class);

    private ArrayList<PlantComments> arrayList;
    private CommentAdapter commentAdapter;
    private Button comment_list;

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        // get plant Info
        Intent intent = getIntent();
        plant = Plant.getPlantInfoFromIntent(intent);
        login_user = User.getUserInfoFromIntent(intent);


        // view init

        ImageView back = findViewById(R.id.searchresult_back);
        ImageView repImage = findViewById(R.id.searchresult_resultimage);

        TextView textView_plantname = findViewById(R.id.searchresult_plantname);
        TextView textView_botanical_name = findViewById(R.id.searchresult_botanical_name);
        TextView textView_botanical_kingdom = findViewById(R.id.searchresult_botanical_kingdom);
        TextView textView_botanical_division = findViewById(R.id.searchresult_botanical_division);
        TextView textView_botanical_class = findViewById(R.id.searchresult_botanical_class);
        TextView textView_botanical_order = findViewById(R.id.searchresult_botanical_order);
        TextView textView_botanical_family = findViewById(R.id.searchresult_botanical_family);
        TextView textView_botanical_genus = findViewById(R.id.searchresult_botanical_genus);
        TextView textView_origin = findViewById(R.id.searchresult_origin);
        TextView textView_flowering_time = findViewById(R.id.searchresult_flowering_time);
        TextView textView_sowing_period = findViewById(R.id.searchresult_sowing_period);
        TextView textView_plantsize = findViewById(R.id.searchresult_plantsize);
        TextView textView_difficulty = findViewById(R.id.searchresult_difficulty);
        TextView textView_min_temp = findViewById(R.id.searchresult_min_temp);
        TextView textView_max_temp = findViewById(R.id.searchresult_max_temp);
        TextView textView_light_amount = findViewById(R.id.searchresult_light_amount);
        TextView textView_soil_type = findViewById(R.id.searchresult_soil_type);
        TextView textView_soil_ph = findViewById(R.id.searchresult_soil_ph);
        TextView textView_soil_drainage = findViewById(R.id.searchresult_soil_drainage);

        Button guide = findViewById(R.id.searchresult_button_guide);
        Button comment = findViewById(R.id.searchresult_button_comment);
        comment_list = findViewById(R.id.searchresult_button_comment_list);
        comment_list.setEnabled(false);


        // get comments
        getComments();

        // set rep image
        int drawableID;
        String id = plant.getId();

        if(id.equals("adiantum"))
            drawableID = R.drawable.adiantum;

        else if(id.equals("agave"))
            drawableID = R.drawable.adiantum;

        else if(id.equals("alocasia"))
            drawableID = R.drawable.alocasia;

        else if(id.equals("lavandula"))
            drawableID = R.drawable.avandula;

        else if(id.equals("benjamina"))
            drawableID = R.drawable.benjamina;

        else if(id.equals("geranium"))
            drawableID = R.drawable.geranium;

        else if(id.equals("ivy"))
            drawableID = R.drawable.ivy;

        else if(id.equals("narcissus"))
            drawableID = R.drawable.narcissus;

        else if(id.equals("pilea"))
            drawableID = R.drawable.pilea;

        else if(id.equals("rose"))
            drawableID = R.drawable.rose;

        else
            drawableID = R.drawable.login;

        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(drawableID, null);
        repImage.setImageDrawable(bitmapDrawable);


        // set values to textView
        textView_plantname.setText(plant.getName());
        textView_botanical_name.setText(plant.getInformation().getScientificName());
        textView_botanical_kingdom.setText(plant.getInformation().getClassification().getPhylum());
        textView_botanical_division.setText(plant.getInformation().getClassification().getPlantClass());
        textView_botanical_class.setText(plant.getInformation().getClassification().getOrder());
        textView_botanical_order.setText(plant.getInformation().getClassification().getFamily());
        textView_botanical_family.setText(plant.getInformation().getClassification().getGenus());
        textView_botanical_genus.setText(plant.getInformation().getClassification().getSpecies());
        textView_origin.setText(plant.getInformation().getHabitat());
        textView_flowering_time.setText(plant.getInformation().getFlowering());
        textView_sowing_period.setText(plant.getInformation().getSowing());

        textView_plantsize.setText(plant.getRequirement().getSize());

        String difficulty = Integer.toString(plant.getRequirement().getDifficulty());
        textView_difficulty.setText(difficulty);

        String tmp_min = Integer.toString(plant.getRequirement().getTmp_min());
        String tmp_max = Integer.toString(plant.getRequirement().getTmp_max());
        textView_min_temp.setText(tmp_min);
        textView_max_temp.setText(tmp_max);

        String str_light="";

        String[] light = plant.getRequirement().getLight();
        for(int i =0; i < light.length; i++) {
            str_light = str_light + light[i];

            if(i != light.length-1)
                str_light = str_light + ", ";
        }
        textView_light_amount.setText(str_light);

        String str_soil="";

        String[] soil = plant.getRequirement().getSoil();
        for(int i =0; i < soil.length; i++) {
            str_soil = str_soil + soil[i];

            if(i != soil.length-1)
                str_soil = str_soil + ", ";
        }
        textView_soil_type.setText(str_soil);

        String str_ph = "";
        String[] ph = plant.getRequirement().getPh();
        for(int i =0; i < ph.length; i++) {
            str_ph = str_ph + ph[i];

            if(i != ph.length-1)
                str_ph = str_ph + ", ";
        }
        textView_soil_type.setText(str_ph);

        textView_soil_drainage.setText(plant.getRequirement().getDrainage());


        // 가이드 보기 버튼 리스너
        Button.OnClickListener onClickListener_1 = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlantGuide plantGuide = plant.getGuide();

                final View guide = LayoutInflater.from(SearchResult.this)
                        .inflate(R.layout.activity_gardening_guide, null);

                TextView propagation = (TextView)guide.findViewById(R.id.gardening_guide_text_propagation);
                TextView disease = (TextView)guide.findViewById(R.id.gardening_guide_text_disease);
                TextView climate = (TextView)guide.findViewById(R.id.gardening_guide_text_climate);
                TextView soil = (TextView)guide.findViewById(R.id.gardening_guide_text_soil);
                TextView water = (TextView)guide.findViewById(R.id.gardening_guide_text_water);
                TextView fertilizer = (TextView)guide.findViewById(R.id.gardening_guide_text_fertilizer);

                propagation.setText(plantGuide.getPropagation());
                disease.setText(plantGuide.getDisease());
                climate.setText(plantGuide.getClimate());
                soil.setText(plantGuide.getSoil());
                water.setText(plantGuide.getWater());
                fertilizer.setText(plantGuide.getFertilizer());

                // 원예 가이드 다이얼로그
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResult.this);

                builder.setView(guide);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                final AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                    }
                });

                alertDialog.show();

            }
        };
        guide.setOnClickListener(onClickListener_1);


        // 후기 보기 버튼 리스너
        Button.OnClickListener onClickListener_3 = new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                // comments_list layout inflate
                 final View comment_list = LayoutInflater.from(SearchResult.this)
                        .inflate(R.layout.comments_list, null);

                // view init
                final ListView listView = (ListView)comment_list.findViewById(R.id.comments_list_listview);

                // 어댑터 등록
                listView.setAdapter(commentAdapter);

                // 다이얼로그
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResult.this);
                builder.setView(comment_list);
                builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                } );

                final AlertDialog alertDialog = builder.create();


                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(16);
                    }
                });


                alertDialog.show();

            }
        };
        comment_list.setOnClickListener(onClickListener_3);


        // 후기 작성 버튼 리스너
        Button.OnClickListener onClickListener_2 = new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                final View comment = LayoutInflater.from(SearchResult.this)
                        .inflate(R.layout.comment_layout, null);

                TextView textView_author = (TextView)comment.findViewById(R.id.comment_text_author);
                final EditText editText = (EditText)comment.findViewById(R.id.comment_text_content);

                String userId = login_user.getId();

                textView_author.setText(userId);
                textView_author.setTextSize(16);

                // 후기 작성 다이얼로그
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResult.this);

                builder.setView(comment);
                builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String name = plant.getName();
                        final String author = login_user.getId();
                        final String content = editText.getText().toString();

                        if(!content.isEmpty()) {

                            compositeDisposable.add(iMyService.insertComment(name, author, content)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                                   @Override
                                                   public void accept(String response) throws Exception {
                                                       JSONObject jsonObject = new JSONObject(response);
                                                       boolean success = jsonObject.getBoolean("success");

                                                       if (success) {
                                                           Toast.makeText(getApplicationContext(), "후기가 등록되었습니다"
                                                                   , Toast.LENGTH_SHORT).show();

                                                           Date data = new Date();
                                                           SimpleDateFormat simpleDateFormat =
                                                                   new SimpleDateFormat("yyyy-MM-dd");

                                                           String today = simpleDateFormat.format(data);

                                                           PlantComments comment = new PlantComments(today, author, content);
                                                           arrayList.add(comment);
                                                           commentAdapter.notifyDataSetChanged();
                                                       }

                                                       else {
                                                           JSONObject jsonObject1 = new JSONObject(response);
                                                           String msg = jsonObject1.getString("message");
                                                           Toast.makeText(getApplicationContext(), msg
                                                                   , Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               },
                                            new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    Toast.makeText(getApplicationContext(), "후기 등록 중 오류 발생"
                                                            , Toast.LENGTH_SHORT).show();
                                                }
                                            })
                            );
                        }

                        else {
                            Toast.makeText(getApplicationContext(), "후기 내용을 작성해주세요"
                                    ,Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                final AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(16);
                    }
                });

                alertDialog.show();
            }
        };

        comment.setOnClickListener(onClickListener_2);

        // 뒤로 가기 버튼 리스너
        ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        back.setOnClickListener(onClickListener);

    }

    // DB로부터 작성된 후기 DB 객채를 가져옴

    private void getComments() {

        compositeDisposable.add(iMyService.searchComments(plant.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        JSONObject jsonObject = new JSONObject(response);
                        Boolean success = jsonObject.getBoolean("success");

                        if(success) {

                            JSONArray commentObject = jsonObject.getJSONArray("data");

                            arrayList = new ArrayList<PlantComments>();

                            int size = commentObject.length();

                            for(int i = 0; i < size ; i++) {

                                JSONObject object = commentObject.getJSONObject(i);

                                String date = object.getString("date");
                                date = date.substring(0,10);

                                JSONObject authorObj = object.getJSONObject("author");
                                String author = authorObj.getString("id");
                                String content = object.getString("content");

                                PlantComments plantComments = new PlantComments(date, author, content);
                                arrayList.add(plantComments);
                            }

                            // 후기 Arraylist adapter
                            commentAdapter = new CommentAdapter(getApplicationContext(), arrayList);
                            comment_list.setEnabled(true);

                        }
                    }
                })
        );
    }
}

