package com.example.news_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.news_app.Models.Article;
import com.example.news_app.R;
import com.example.news_app.ShowArticleActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    private Context context;
    private List<Article> articleList;
    private List<Article> savedArticleList = new ArrayList<>();


    public ArticleAdapter(Context context, List<Article> articleList)
    {
        this.context = context;
        this.articleList = articleList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);

        System.out.println("create viewHolder for adapter called");

        return new ArticleAdapter.ViewHolder(view);
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        final Article article = articleList.get(position);



        Glide.with(context).load(article.getUrlToImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder)).into(holder.image);


        Calendar calendar = getFormattedPublishedDateAndTime(article);

        final String calendarDate = calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" +  calendar.get(Calendar.YEAR);
        final String calendarTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        String elapsedTime = getElapsedTime(article);


        holder.author.setText(article.getAuthor());
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getContent());
        holder.date.setText(calendarDate);
        holder.publishedTime.setText(elapsedTime);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saved").child(firebaseUser.getUid()).child("Articles");

        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Article a = snapshot.getValue(Article.class);

                    if (a.getTitle().equals(article.getTitle()))
                    {
                        holder.save.setBackgroundResource(R.drawable.ic_save_filled);
                        holder.save.setTag("saved");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = firebaseUser.getUid();


                if (holder.save.getTag().equals("save"))
                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saved").child(userId).child("Articles");

                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("source", article.getSource());
                    hashMap.put("author", article.getAuthor());
                    hashMap.put("title", article.getTitle());
                    hashMap.put("url", article.getUrl());
                    hashMap.put("urlToImage", article.getUrlToImage());
                    hashMap.put("datePublished", article.getDatePublished());
                    hashMap.put("content", article.getContent());
                    hashMap.put("description", article.getDescription());


                    reference.push().setValue(hashMap);


                    holder.save.setBackgroundResource(R.drawable.ic_save_filled);
                    holder.save.setTag("saved");
                }
                else
                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saved").child(userId).child("Articles");
                    reference.removeValue();

                    holder.save.setBackgroundResource(R.drawable.ic_save_unfilled);
                    holder.save.setTag("save");
                }
            }
        });




        holder.image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ShowArticleActivity.class);
                intent.putExtra("author", article.getAuthor());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("content", article.getContent());
                intent.putExtra("date", calendarDate);
                intent.putExtra("publishedTime", calendarTime);
                intent.putExtra("source", article.getSource().getSourceName());
                intent.putExtra("url", article.getUrl());


                context.startActivity(intent);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ShowArticleActivity.class);
                intent.putExtra("author", article.getAuthor());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("content", article.getContent());
                intent.putExtra("date", calendarDate);
                intent.putExtra("publishedTime", calendarTime);
                intent.putExtra("source", article.getSource().getSourceName());
                intent.putExtra("url", article.getUrl());

                context.startActivity(intent);
            }
        });

        holder.description.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ShowArticleActivity.class);
                intent.putExtra("author", article.getAuthor());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("content", article.getContent());
                intent.putExtra("date", calendarDate);
                intent.putExtra("publishedTime", calendarTime);
                intent.putExtra("source", article.getSource().getSourceName());
                intent.putExtra("url", article.getUrl());

                context.startActivity(intent);
            }
        });



        System.out.println("Author: " + article.getAuthor());
        System.out.println("Description: " + article.getDescription());
    }


    @Override
    public int getItemCount()
    {
        return articleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView image;
        public ImageView save;
        public TextView author;
        public TextView date;
        public TextView title;
        public TextView description;
        public TextView publishedTime;
        public ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            save = itemView.findViewById(R.id.save);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            publishedTime = itemView.findViewById(R.id.publishedTime);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private Calendar getFormattedPublishedDateAndTime(Article article)
    {
        Instant instant = Instant.parse(article.getDatePublished());
        Date dateFromInstant = Date.from(instant);

        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        Date dateFromFormatter = null;
        try
        {
            dateFromFormatter = (Date) formatter.parse(dateFromInstant.toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFromFormatter);




        return calendar;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getElapsedTime(Article article)
    {
        Instant instant = Instant.parse(article.getDatePublished());
        Date dateFromInstant = Date.from(instant);

        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        Date dateFromFormatter = null;
        try
        {
            dateFromFormatter = (Date) formatter.parse(dateFromInstant.toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        PrettyTime prettyTime = new PrettyTime(article.getDatePublished());
        String elapsedTime = prettyTime.format(dateFromFormatter);
        System.out.println("Time elapsed: " + elapsedTime);

        return elapsedTime;
    }
}
