package in.truethic.hrmsapp.Retrofit;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance
{
    private static Retrofit retrofit = null;
//    private static String BASE_URL = "http://localhost:9021";
    private static String BASE_URL = "http://192.168.1.200:9021";
//    private static String BASE_URL = "http://194.233.89.164:3034";  //Online Ip

    private static String TAG = "RI";
    //private static String BASE_URL = "dummy";

   public static AppService getService()
   {

       try {
           if(retrofit == null){
               Log.e( TAG,BASE_URL );
               HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
               interceptor.setLevel( HttpLoggingInterceptor.Level.BODY );
               OkHttpClient client = new OkHttpClient.Builder()
                       .readTimeout( 60, TimeUnit.SECONDS )
                       .connectTimeout( 60, TimeUnit.SECONDS )
                       .addInterceptor( interceptor ).build();
               retrofit = new Retrofit.Builder()
                       .baseUrl( BASE_URL )
                       .addConverterFactory( GsonConverterFactory.create() )
                       .client( client )
                       .build();
               Log.e(TAG, "original" );
               return  retrofit.create( AppService.class );

           } else {
               return  retrofit.create( AppService.class );
           }

       }catch (Exception e)
       {
           Log.e(TAG,e.toString());
           e.printStackTrace();
           return null;
       }
   }
}
