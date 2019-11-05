package ga.project0511.graduationproject.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

    @POST("/api/activities/register")
    @FormUrlEncoded
    Observable<String> registerActivity(@Field("name_activity") String name_activity,
                                        @Field("name_plant") String name_plant,
                                        @Field("created_at") String createdAt,
                                        @Field("ended_at") String endedAt,
                                        @Field("id_manager") String id_manager,
                                        @Field("id_participants") String id_participants
                                        );

    @POST("/api/activities/search")
    @FormUrlEncoded
    Observable<String> searchAllActivities(@Field("id") String id);

    @POST("/api/activities/update")
    @FormUrlEncoded
    Observable<String> updateActivity(@Field("name_activity") String name_activity,
                                      @Field("created_at") String createdAt,
                                      @Field("ended_at") String endedAt,
                                      @Field("name_plant") String name_plant,
                                      @Field("id_manager") String id_manager,
                                      @Field("id_participants") String[] id_participants,
                                      @Field("image") String imgPath,
                                      @Field("sensor") String sensorPath);


}
