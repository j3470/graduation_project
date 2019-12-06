package ga.project0511.graduationproject.Retrofit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IMyService {
    @POST("/api/users/register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("id") String id,
                                    @Field("password") String password,
                                    @Field("email") String email,
                                    @Field("name") String name);

    @POST("/api/users/login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("id") String id,
                                    @Field("password") String password);

    @POST("/api/plants/comment")
    @FormUrlEncoded
    Observable<String> insertComment(@Field("name") String plantName,
                                         @Field("author") String author,
                                         @Field("content") String content);

    @POST("/api/plants/searchcomment")
    @FormUrlEncoded
    Observable<String> searchComments(@Field("name") String plantName);

    @POST("/api/activities/register")
    @FormUrlEncoded
    Observable<String> registerActivity(@Field("name_activity") String name_activity,
                                        @Field("name_plant") String name_plant,
                                        @Field("created_at") String createdAt,
                                        @Field("ended_at") String endedAt,
                                        @Field("id_manager") String id_manager,
                                        @Field("id_participants") String id_participants,
                                        @Field("imgPath") String imgPath
                                        );

    @POST("/api/activities/searchall")
    @FormUrlEncoded
    Observable<String> searchAllActivities(@Field("id_manager") String id);

    @POST("/api/activities/update")
    @FormUrlEncoded
    Observable<String> updateActivity(@Field("name_activity") String name_activity,
                                      @Field("created_at") String createdAt,
                                      @Field("ended_at") String endedAt,
                                      @Field("name_plant") String name_plant,
                                      @Field("id_manager") String id_manager,
                                      @Field("id_participants") String[] id_participants,
                                      @Field("imgPath") String imgPath,
                                      @Field("sensor") String sensorPath);

    @POST("/api/fs/delete")
    @FormUrlEncoded
    Observable<String> deleteImgFile(@Field("path") String filePath);

    @POST("/api/images/upload")
    @Multipart
    Observable<String> uploadImage(@Part MultipartBody.Part file, @Part("name")RequestBody requestBody);

    @POST("/api/images/show")
    @FormUrlEncoded
    Observable<ResponseBody> downloadImage(@Field("path") String filePath);

    @POST("/api/plants/index")
    @FormUrlEncoded
    Observable<String> searchPlantInform(@Field("noentry") String noentry);

    @POST("/api/images/request")
    @Multipart
    Observable<String> requestImageSearch(@Part MultipartBody.Part file, @Part("name")RequestBody requestBody);


}
