package son.nt.hellochao.retrofit.esl;

import retrofit.Response;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Sonnt on 10/8/15.
 */
public interface IESLInterfaceAPI {
    @GET("index.htm")
    Call<Response> getESLList();


}
