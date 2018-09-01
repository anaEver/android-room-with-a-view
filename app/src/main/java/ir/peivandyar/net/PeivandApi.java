package ir.peivandyar.net;

/**
 * Created by nasl_ on 20/06/2018.
 */


import java.util.List;

import ir.peivandyar.database.entity.SchoolInfo;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface PeivandApi {
    //i.e. http://localhost/api/institute/Students

    ///------------------     Get User Login --------------------
    //===========================================================
//    @GET("/api/Logins/")
//    void GetLogin(@Query("username") String username, @Query("password") String password, Callback<Login> callback);

    @GET("/api/Logins")
    void GetLogins(@Query("username") String username, Callback<List<String>> callback);

    @GET("/api/values/")
    void Get(Callback<List<SchoolInfo>> callback);

    @GET("/api/values/")
    void GetSchoolDetails(@Query("id") int schoolId, Callback<String> callback);


}
