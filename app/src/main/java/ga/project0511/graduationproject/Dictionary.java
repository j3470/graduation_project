package ga.project0511.graduationproject;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import ga.project0511.graduationproject.adapter.DictionaryAdapter;
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

public class Dictionary extends AppCompatActivity {

    public static final int REQUEST_CODE_SEARCH_PLANT = 301;
    public static final int REQUEST_CODE_INFORM_PLANT = 302;
    public static final int TEST = 303;

    User login_user;
    ArrayList<Plant> plantInform;
    ArrayList<Plant> plantInform_copy;
    int count;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    EditText editSearch;
    ListView listView;
    DictionaryAdapter dictionaryAdapter;

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        // Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        // 리스트 뷰
        listView = findViewById(R.id.listing_search_contents);

        // 리스트 뷰 ArrayList
        plantInform = new ArrayList<Plant>();
        plantInform_copy = new ArrayList<Plant>();

        // editSearch 뷰
        editSearch = (EditText)findViewById(R.id.dict_searchbar);

        // 사용자 정보 가져오기
        Intent intent = getIntent();
        login_user = User.getUserInfoFromIntent(intent);

        // DB로부터 식물 정보를 가져와 activity에 저장
        this.listingPlantInform();

        // 리스트 뷰 검색 이벤트 리스너 설정
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input 창에 문자를 입력할 때마다 호출
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        // 리스트 뷰 터치 이벤트 리스너 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
               // Toast.makeText(getApplicationContext(),dictionaryAdapter.getItem(position).getName(),Toast.LENGTH_LONG).show();

                Plant plant = dictionaryAdapter.getItem(position);
                Intent intent = new Intent(Dictionary.this, SearchResult.class);
                intent.putExtra(User.KEY_USER_DATA, login_user);
                intent.putExtra(Plant.KEY_DATA_PLANT, plant);
                startActivityForResult(intent, REQUEST_CODE_INFORM_PLANT);

            }
        });

        ImageView button = findViewById(R.id.dict_back);

        ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        button.setOnClickListener(onClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // DB로부터 모든 식물 정보를 가져와 저장하는 함수
    public void listingPlantInform(){


        compositeDisposable.add(iMyService.searchPlantInform("noentry")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Boolean success = jsonObject.getBoolean("success");

                        if(success){
                            // 응답으로 온 JSON 객체 파싱
                            for(int i=0; i<jsonArray.length(); i++) {

                                JSONObject json = jsonArray.getJSONObject(i);

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

                                plantInform.add(plant);

                            }
                        }
                        else{
                            Toast.makeText(Dictionary.this, "can't find plants", Toast.LENGTH_SHORT).show();
                        }

                        // 뷰 어댑터
                        dictionaryAdapter = new DictionaryAdapter(getApplicationContext(), plantInform);

                        Resources res = getResources();

                        BitmapDrawable bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_agave, null);
                        dictionaryAdapter.setBitmapDrawable("agave", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_adiantum, null);
                        dictionaryAdapter.setBitmapDrawable("adiantum", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_alocasia, null);
                        dictionaryAdapter.setBitmapDrawable("alocasia", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_avandula, null);
                        dictionaryAdapter.setBitmapDrawable("lavandula", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_benjamina, null);
                        dictionaryAdapter.setBitmapDrawable("benjamina", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_geranium, null);
                        dictionaryAdapter.setBitmapDrawable("geranium", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_ivy, null);
                        dictionaryAdapter.setBitmapDrawable("ivy", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_narcissus, null);
                        dictionaryAdapter.setBitmapDrawable("narcissus", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_pilea, null);
                        dictionaryAdapter.setBitmapDrawable("pilea", bitmap);

                        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.rep_rose, null);
                        dictionaryAdapter.setBitmapDrawable("rose", bitmap);

                        // 리스트 뷰에 어댑터 등록
                        listView.setAdapter(dictionaryAdapter);
                        plantInform_copy.addAll(plantInform);

                    }
                }));
    }

    // ArrayList에서 입력한 문자열을 포함하는 식물 검색
    public void search(String charText) {

        // 문자 입력 시마다 리스트를 지우고 새로 뿌려줌
        plantInform.clear();

        // 문자 입력이 없을 때는 모든 데이터를 보여준다
        if(charText.length() == 0) {
            plantInform.addAll(plantInform_copy);
        }

        // 문자 입력을 할 때...
        else {
            // 리스트의 모든 데이터를 검색
            for(int i=0; i < plantInform_copy.size(); i++) {

                // plantInform의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환
                if(plantInform_copy.get(i).getName().toLowerCase().contains(charText)){

                    // 검색된 데이터를 리스트에 추가
                        plantInform.add(plantInform_copy.get(i));
                }

            }
        }

        // 어댑터 갱신
        dictionaryAdapter.notifyDataSetChanged();

    }
}

