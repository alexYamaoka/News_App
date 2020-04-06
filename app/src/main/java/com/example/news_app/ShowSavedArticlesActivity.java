package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.news_app.Adapters.ArticleAdapter;
import com.example.news_app.Models.Article;

import java.util.ArrayList;
import java.util.List;

public class ShowSavedArticlesActivity extends AppCompatActivity
{
    private ImageView options;
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_article);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowSavedArticlesActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleList);
        recyclerView.setAdapter(articleAdapter);



        if (!articleList.isEmpty())
        {
            articleList.clear();
        }

        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(ShowSavedArticlesActivity.this, articleList);
        recyclerView.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();









        options = findViewById(R.id.options);

        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(ShowSavedArticlesActivity.this, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.viewSaved:
                                Intent intent = new Intent(ShowSavedArticlesActivity.this, ShowSavedArticlesActivity.class);
                                startActivity(intent);
                                return true;

                            case R.id.logout:
                                // TODO: logout
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
}
