package com.dokajajob.skipphelp.Data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dokajajob.skipphelp.Model.Post;
import com.dokajajob.skipphelp.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;

    public RecyclerAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Post post = postList.get(position);
        String imageUrl = null;

        holder.title.setText(post.getTitle());
        holder.desc.setText(post.getDesc());

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(post.getTimestamp())).getTime());

        holder.timestamp.setText(formattedDate);

        imageUrl = post.getImage();
        Log.d("imageUrl ",  imageUrl);

        //TODO: Use Picasso library to load image
        Picasso.get()
                .load(imageUrl)
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView desc;
        public TextView timestamp;
        public ImageView image;
        String userid;


        public ViewHolder(View view, Context ctx) {

            super(view);
            context = ctx;

            title = (TextView) view.findViewById(R.id.postTitleList);
            desc = (TextView) view.findViewById(R.id.postTextList);
            image = (ImageView) view.findViewById(R.id.postImageList);
            timestamp = (TextView) view.findViewById(R.id.timestampList);

            userid = null;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Go to next activity
                }
            });

        }
    }
}
