package ir.peivandyar.net;

import retrofit.RestAdapter;

public class RestService {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different URL than we have here
    private static final String URL = "http://192.168.1.50/WebApplication1/";
    private RestAdapter restAdapter;
    private PeivandApi apiService;

    public RestService()
    {

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(PeivandApi.class);
    }



    public PeivandApi getService()
    {
        return apiService;
    }
}

