import java.io.IOException;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.Subscriber;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import okhttp3.ResponseBody;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class RxGql {
  interface GraphQLService {
    @GET("/gql/")
    Observable<Response<ResponseBody>> query(@Query("query") String query);
  }

  public static void main (String[] args) throws IOException, InterruptedException {

    Retrofit rt = new Retrofit.Builder()
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .baseUrl("http://core-graphql.dev.waldo.photos/")
      .build();

    GraphQLService gql = rt.create(GraphQLService.class);
    gql.query("query { account(id: \"1\") { id, username }}")
    .subscribeOn(Schedulers.newThread())
    .observeOn(Schedulers.immediate())
    .subscribe(new Subscriber<Response<ResponseBody>>() {
      @Override
      public final void onCompleted () {}

      @Override
      public final void onError(Throwable e) {
        System.out.println("Fail: " + e.getMessage());
      }

      @Override
      public final void onNext (Response<ResponseBody> response) {
        try {
          JsonParser jp = new JsonParser();
          JsonObject jo = jp.parse(response.body().string()).getAsJsonObject();
          System.out.println(jo.getAsJsonObject("data").getAsJsonObject("account"));
        } catch (IOException e) {
          System.out.println("Fail: " + e.getMessage());
        }
      }
    });

    Thread.sleep(5000);
  }
}
