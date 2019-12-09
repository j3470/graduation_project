package ga.project0511.graduationproject;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.datatype.Plant;
import ga.project0511.graduationproject.datatype.PlantClassification;
import ga.project0511.graduationproject.datatype.PlantComments;
import ga.project0511.graduationproject.datatype.PlantGuide;
import ga.project0511.graduationproject.datatype.PlantInformation;
import ga.project0511.graduationproject.datatype.PlantRequirement;
import ga.project0511.graduationproject.datatype.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class image_search_result extends AppCompatActivity {

    public static final int REQUEST_CODE_INFO = 301;

    // RxJava & Retrofit
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyservice;

    Plant plant;
    User login_user;

    BitmapDrawable bitmap;
    Button button_info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search_result);

        // Retrofit Client
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyservice = retrofitClient.create(IMyService.class);



        // 인텐트로부터 회원 정보 결과 값 얻기
        Intent intent = getIntent();

        login_user = User.getUserInfoFromIntent(intent);

        Bundle bundle = intent.getExtras();
        String plantName = bundle.getString("plantName");
        double value = bundle.getDouble("prediction");
        String predictions = Double.toString(value);

        // view init
        ImageView back = findViewById(R.id.imagesearchresult_back);
        ImageView repImg = findViewById(R.id.imagesearchresult_image);
        TextView textView_plantName = findViewById(R.id.imagesearchresult_plantname_txt);
        TextView textView_predictions = findViewById(R.id.imagesearchresult_probability_txt);
        button_info = findViewById(R.id.imagesearchresult_button_info);

        // set Rep. image

        int drawableId;
        String plant;

        if(plantName.equals("alocasia")) {
            drawableId = R.drawable.alocasia;
            plant = "알로카시아";
        }
        else if(plantName.equals("adiantum")) {
            drawableId = R.drawable.adiantum;
            plant = "아디안텀";
        }
        else if(plantName.equals("agave")) {
            drawableId = R.drawable.agave;
            plant = "아가베";
        }
        else if(plantName.equals("avandula")) {
            drawableId = R.drawable.avandula;
            plant = "라벤더";
        }
        else if(plantName.equals("benjamina")) {
            drawableId = R.drawable.benjamina;
            plant = "벤자민";
        }
        else if(plantName.equals("geranium")) {
            drawableId = R.drawable.geranium;
            plant = "제라늄";
        }
        else if(plantName.equals("ivy")) {
            drawableId = R.drawable.ivy;
            plant = "아이비";
        }
        else if(plantName.equals("narcissus")) {
            drawableId = R.drawable.narcissus;
            plant = "수선화";
        }
        else if(plantName.equals("pilea")) {
            drawableId = R.drawable.pilea;
            plant = "필레아페페";
        }
        else if(plantName.equals("rose")) {
            drawableId = R.drawable.rose;
            plant = "장미";
        }
        else {
            drawableId = R.drawable.login;
            plant = "매칭 결과 없음";
        }


        Resources res = getResources();
        bitmap = (BitmapDrawable)res.getDrawable(drawableId, null);
        repImg.setImageDrawable(bitmap);

        // set textview
        DecimalFormat format = new DecimalFormat();
        format.applyLocalizedPattern("0.0000");
        textView_plantName.setText(plantName);
        textView_predictions.setText(format.format(value));


        // 검색 리스너
        final String str = plant;
        button_info.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){

                compositeDisposable.add(iMyservice.searchPlant(str)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                JSONObject jsonObject = new JSONObject(response);

                                Boolean success = jsonObject.getBoolean("success");

                                if(success) {

                                    JSONObject json = jsonObject.getJSONObject("data");

                                    String id = json.getString("id");
                                    String name = json.getString("name");
                                    String image = json.getString("image");

                                    /*********************************************************************************************/
                                    JSONObject json_information = json.getJSONObject("information");

                                    JSONObject json_information_classification = json_information.getJSONObject("classification");

                                    String inf_cla_phylum = json_information_classification.getString("phylum");
                                    String inf_cla_class = json_information_classification.getString("class");
                                    String inf_cla_order = json_information_classification.getString("order");
                                    String inf_cla_family = json_information_classification.getString("family");
                                    String inf_cla_genus = json_information_classification.getString("genus");
                                    String inf_cla_species = json_information_classification.getString("species");

                                    String inf_flowering = json_information.getString("flowering");
                                    String inf_sowing = json_information.getString("sowing");
                                    //String inf_habitat = json_information.getString("habitat");
                                    JSONArray jsonArray1 = json_information.getJSONArray("habitat");
                                    String inf_habitat = jsonArray1.getString(0);
                                    String inf_scientificname = json_information.getString("scientificName");
                                    String inf_size = json_information.getString("size");

                                    PlantClassification plantClassification =
                                            new PlantClassification(inf_cla_phylum,inf_cla_class,inf_cla_order
                                                    ,inf_cla_family,inf_cla_genus,inf_cla_species);

                                    PlantInformation plantInformation =
                                            new PlantInformation(plantClassification, inf_flowering, inf_sowing
                                                    ,inf_habitat, inf_scientificname, inf_size);

                                    /*********************************************************************************************/

                                    JSONObject json_requirement = json.getJSONObject("requirement");

                                    String req_size = json_requirement.getString("size");

                                    JSONObject json_requirement_temperature = json_requirement.getJSONObject("temperature");
                                    int req_tem_min = json_requirement_temperature.getInt("min");
                                    int req_tem_max = json_requirement_temperature.getInt("max");

                                    JSONArray json_requirement_light = json_requirement.getJSONArray("light");
                                    String[] req_light = new String[json_requirement_light.length()];
                                    for(int index = 0; index < json_requirement_light.length(); index++) {
                                        req_light[index] = (String)json_requirement_light.get(index);
                                    }


                                    JSONArray json_requirement_soil = json_requirement.getJSONArray("soil");
                                    String[] req_soil = new String[json_requirement_soil.length()];
                                    for(int index = 0; index < json_requirement_soil.length(); index++) {
                                        req_soil[index] = (String)json_requirement_soil.get(index);
                                    }

                                    JSONArray json_requirement_ph = json_requirement.getJSONArray("ph");
                                    String[] req_ph = new String[json_requirement_ph.length()];
                                    for(int index = 0; index < json_requirement_ph.length(); index++) {
                                        req_ph[index] = (String)json_requirement_ph.get(index);
                                    }

                                    int req_difficulty = json_requirement.getInt("difficulty");
                                    String req_drainage = json_requirement.getString("drainage");

                                    PlantRequirement plantRequirement = new PlantRequirement(req_size, req_tem_max
                                            ,req_tem_min, req_light, req_soil, req_ph, req_difficulty, req_drainage);

                                    /*********************************************************************************************/

                                    JSONObject json_guide = json.getJSONObject("guide");

                                    String gd_propagation = json_guide.getString("propagation");
                                    String gd_disease = json_guide.getString("disease");
                                    String gd_climate = json_guide.getString("climate");
                                    String gd_soil = json_guide.getString("soil");
                                    String gd_water = json_guide.getString("water");
                                    String gd_fertilizer = json_guide.getString("fertilizer");

                                    PlantGuide plantGuide = new PlantGuide(gd_propagation, gd_disease, gd_climate
                                            ,gd_soil, gd_water, gd_fertilizer);

                                    /*********************************************************************************************/


                                    JSONArray json_comment = json.getJSONArray("comments");


                                    PlantComments[] comments = new PlantComments[json_comment.length()];
                                    for (int index = 0; index < json_comment.length(); index++) {
                                        JSONObject commentObject =(JSONObject) json_comment.get(index);

                                        String date = commentObject.getString("date");
                                        date = date.substring(0,10);
                                        JSONObject json_author = commentObject.getJSONObject("author");
                                        String author = json_author.getString("id");
                                        String content = commentObject.getString("content");

                                        comments[index] = new PlantComments(date,author,content);
                                    }


                                    /*********************************************************************************************/
                                    // 식물이미지 Integer plant_image = json.getInt("plant_image");

                                    Plant plant = new Plant(id, name, image, Plant.NOT_DOWNLOADED_YET
                                            ,plantInformation, plantRequirement, plantGuide, comments);

                                    Intent intent = new Intent(image_search_result.this, SearchResult.class);
                                    intent.putExtra(User.KEY_USER_DATA, login_user);
                                    intent.putExtra(Plant.KEY_DATA_PLANT, plant);
                                    startActivityForResult(intent, REQUEST_CODE_INFO);


                                }
                            }
                        }));
            }
        });

        // 뒤로가기 리스너
        back.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }


}