package com.example.popularmovies;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Intent;
import android.os.AsyncTask;
import java.net.URL;
import org.json.JSONObject;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import java.net.HttpURLConnection;
import java.util.Scanner;
import android.support.v7.app.AppCompatActivity;





public class DetailActivity extends AppCompatActivity {
    String api_key = "a3e41c7c180bb5bc68c2fb333aae38f0";







    TextView title, user_rating, release_date, syno;
    Integer id;
    ImageView poster_image;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         Intent intent = getIntent();
         id = intent.getIntExtra("ID", 0);
         title=findViewById(R.id.title);
          user_rating=findViewById(R.id.user_rating);
         release_date=findViewById(R.id.release_date);
         syno =findViewById(R.id.synopsis);
         poster_image=findViewById(R.id.poster_image);



        FetchMovieDetails fetchMovieDetails=new FetchMovieDetails();
        fetchMovieDetails.execute();
    }
    public class FetchMovieDetails extends AsyncTask<Void, Void, Void> {
        String original_title, release, syno, poster_path;
        Double rating;
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader bufferReaded;
            String json ;
            try {
                         URL url=new URL("https://api.themoviedb.org/3/movie/" + Integer.toString(id) + "?api_key=" + api_key);
                         urlConnection=(HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();
                Scanner scan;
                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                bufferReaded = new BufferedReader(new InputStreamReader(inputStream));
                scan= new Scanner(bufferReaded);
                while(scan.hasNextLine()) {
                    buffer.append(scan.nextLine()+"\n");
                }
                json=buffer.toString();
                JSONObject main=new JSONObject(json);
                original_title = main.getString("original_title");
                release = main.getString("release_date");
                rating = main.getDouble("vote_average");
                syno = main.getString("overview");
                poster_path = "http://image.tmdb.org/t/p/w185" +main.getString("poster_path");
            }catch(Exception e){
            }
            urlConnection.disconnect();
            return null;
        }
        public void onPostExecute(Void result) {
            title.setText(original_title);
            user_rating.setText("User Ratings: "+Double.toString(rating));
            release_date.setText("Release Date: "+ release);
            DetailActivity.this.syno.setText(syno);
            Picasso.with(DetailActivity.this).load(poster_path).into(poster_image);
        }
    }
}