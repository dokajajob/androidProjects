package com.dokajajob.recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;




public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private final int REQUEST_CODE_1 = 1;

    private ArrayList<String> mImageName = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context mContext;


    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageName, ArrayList<Integer> mImages) {
        this.mImageName = mImageName;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("TAG","onBindViewHolder called" + mImages);

        holder.imageName.setText(mImageName.get(position));
        //Glide.with(mContext).load(mImages.get(position)).into(holder.image);
        //holder.image.setImageResource(Integer.parseInt(mImages.get(position)));

        holder.image.setImageResource(mImages.get(position));


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "clicked " + mImageName.get(position), Toast.LENGTH_LONG).show();
                Intent detailsIntent = new Intent(mContext, Details.class);
                detailsIntent.putExtra("ImageName", mImageName.get(position));
                //mContext.startActivity(detailsIntent);

                ((Activity) mContext).startActivityForResult(detailsIntent, REQUEST_CODE_1);



            }
        });

    }

/*    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }*/



    @Override
    public int getItemCount() {
        return mImageName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
