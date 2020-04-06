package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.news_app.Adapters.ArticleAdapter;
import com.example.news_app.Models.Article;
import com.example.news_app.Models.News;
import com.example.news_app.NewsApi.NewsApiInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private final String LANGUAGE = "us";
    private News news;

    private SearchView search;
    private ImageView options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        options = findViewById(R.id.options);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);



        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);




        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleList);
        recyclerView.setAdapter(articleAdapter);




        onLoadingSwipeRefresh("");



        search.setQueryHint("Search news here...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if (query.length() > 2)
                {
                    onLoadingSwipeRefresh(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                onLoadingSwipeRefresh(newText);
                return false;
            }
        });



        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.viewSaved:
                                Intent intent = new Intent(MainActivity.this, ShowSavedArticlesActivity.class);
                                startActivity(intent);
                                return true;

                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                return true;

                            default:
                                return false;
                        }
                    }
                });


                popupMenu.inflate(R.menu.options_menu);

                popupMenu.show();
            }
        });
    }




    private void loadJSON(String keyword)
    {
        Call<News> call;


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        NewsApiInterface newsApiInterface = retrofit.create(NewsApiInterface.class);

        swipeRefreshLayout.setRefreshing(true);


        if (keyword.length() > 0)
        {
            System.out.println("Key word: " + keyword);
            call = newsApiInterface.getNewsSearch(keyword, BuildConfig.ApiKey);
        }
        else
        {
            call = newsApiInterface.getTopHeadlinesNews("us", BuildConfig.ApiKey);
        }

        call.enqueue(new Callback<News>()
        {
            @Override
            public void onResponse(Call<News> call, Response<News> response)
            {
                if (response.isSuccessful())
                {
                    if (!articleList.isEmpty())
                    {
                        articleList.clear();
                    }

                    articleList = response.body().getArticlesList();
                    articleAdapter = new ArticleAdapter(MainActivity.this, articleList);
                    recyclerView.setAdapter(articleAdapter);
                    articleAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);


                }
                else
                {
                    Toast.makeText(MainActivity.this, "Unable to retrieve news", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t)
            {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }





    // 2 functions to hide keyboard after clicking somewhere else
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            View view = getCurrentFocus();

            if (view != null && view instanceof EditText)
            {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    hideSoftKeyboard(MainActivity.this);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }



    // swipe refresh layout listener
    @Override
    public void onRefresh()
    {
        loadJSON("");
    }

    private void onLoadingSwipeRefresh(final String keyword)
    {
        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                loadJSON(keyword);
            }
        });
    }
}
