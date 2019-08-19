package ga.project0511.graduationproject.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("id") String id,
                                    @Field("password") String password,
                                    @Field("email") String email,
                                    @Field("nickname") String nickname,
                                    @Field("phone") String phone);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("id") String id,
                                    @Field("password") String password);
}
