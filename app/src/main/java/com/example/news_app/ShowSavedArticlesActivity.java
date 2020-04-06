package com.example.news_app;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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



        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saved").child(firebaseUser.getUid()).child("Articles");


        articleList = new ArrayList<>();



        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                System.out.println("inside ondatachange");
                System.out.println("children count: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    //System.out.println("datasnapshot: " + dataSnapshot);
                    Article article = snapshot.getValue(Article.class);

                    System.out.println("*********************************************");
                    System.out.println("article: " + article.getAuthor());
                    System.out.println("title: " + article.getTitle());
                    System.out.println("source: " + article.getSource());
                    System.out.println("url: " + article.getUrl());
                    System.out.println("image: " + article.getUrlToImage());
                    System.out.println("datepublished: " + article.getDatePublished());
                    System.out.println("descriptioni: " + article.getDescription());
                    System.out.println("content: " + article.getContent());
                    System.out.println("*********************************************");


                    articleList.add(article);
                    System.out.println("article added: ");

                }

                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });

        System.out.println("article list size: " + articleList.size());


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
                            case R.id.home:
                                Intent intentHome = new Intent(ShowSavedArticlesActivity.this, MainActivity.class);
                                startActivity(intentHome);
                                return true;

                            case R.id.viewSaved:
                                Intent intentViewSaved = new Intent(ShowSavedArticlesActivity.this, ShowSavedArticlesActivity.class);
                                startActivity(intentViewSaved);
                                return true;

                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(ShowSavedArticlesActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
