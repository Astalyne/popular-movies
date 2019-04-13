package com.example.popularmovies;
import java.util.ArrayList;
import android.content.Context;
import android.os.Bundle;
import java.net.HttpURLConnection;
import java.net.URL;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    String api_key = "a3e41c7c180bb5bc68c2fb333aae38f0"; // pls put your api key here
    ArrayList<String> posters;
    ArrayList<String> list;
    ArrayList<Integer> ids;
    GridView gridView;
    public void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main);
                gridView = findViewById(R.id.gridview);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("ID", ids.get(position));
                startActivity(intent);
            }
        });
       FetchMovies fetchMovies = new FetchMovies();
       fetchMovies.execute();
    }
    public class FetchMovies extends AsyncTask<String, Void, Void> {
         protected Void doInBackground(String... parameters) {
            posters = new ArrayList<>();
            ids = new ArrayList<>();
            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + "popular" + "?api_key=" + api_key);
                HttpURLConnection connecton = (HttpURLConnection) url.openConnection();
                connecton.setRequestMethod("GET");
                connecton.connect();
                InputStream inputStream = connecton.getInputStream();
                StringBuffer data = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                Scanner scan = new Scanner(reader);
                while(scan.hasNextLine()) {
                    data.append(scan.nextLine() + "\n");
                }
                String js = data.toString();
                JSONObject object = new JSONObject(js);
                JSONArray jsonArray = object.getJSONArray("results");
                JSONObject movie;
                for(int i=0; i < jsonArray.length(); i++) {
                    movie = jsonArray.getJSONObject(i);
                    ids.add(movie.getInt("id"));
                    posters.add(movie.getString("poster_path"));
                }
            }catch(Exception e){
            }
            return null;
        }
        public void onPostExecute(Void result) {
           ImageAdapter imageAdapter = new ImageAdapter(MainActivity.this, posters);
           gridView.setAdapter(imageAdapter);
        }
    }
}
class ImageAdapter extends BaseAdapter {
    private Context cont;
    private ArrayList<String> posters;
    private String[] paths;
    ImageAdapter(Context c, ArrayList<String> posters) {
            cont = c;
            this.posters = posters;
            paths = new String[this.posters.size()];
            for (int i = 0; i < this.posters.size(); i++) {

                paths[i] = "http://image.tmdb.org/t/p/w185" + this.posters.get(i); }
    }
    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }
    public int getCount() {
        return paths.length;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        imageView = new ImageView(cont);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(cont).load(paths[position]).into(imageView);
        return imageView;
    }
}