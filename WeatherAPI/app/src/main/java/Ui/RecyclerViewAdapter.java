package Ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dokajajob.weatherapi.BuildConfig;
import com.dokajajob.weatherapi.R;

import java.util.List;

import Model.CityItems;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private final List<CityItems> cityItemsList;

    public RecyclerViewAdapter(Context context, List<CityItems> cityItemsList) {
        this.cityItemsList = cityItemsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CityItems cityItems = cityItemsList.get(position);
        holder.name.setText(cityItems.getCityName());
        holder.temperature.setText(cityItems.getCityTemperature());
        holder.dateAdded.setText(cityItems.getDateItemAdded());
        holder.png.setImageBitmap(cityItems.getImage());

/*
        //png
        holder.dateAdded.setText(cityItems.getDateItemAdded());
        String pngUrl = cityItems.getImageView();
        Glide.with(context)

                .load(pngUrl)

                .into(holder.png);
*/


    }

    @Override
    public int getItemCount() {
        return cityItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView temperature;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public ImageView png;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            temperature = itemView.findViewById(R.id.temperature);
            dateAdded = itemView.findViewById(R.id.dateAdded);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            png = itemView.findViewById(R.id.png);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Get position in List
                    int position = getAdapterPosition();

                    //CityItems cityItems = cityItemsList.get(position);

                }
            });

        }

        @Override
        public void onClick(View view) {

        }
    }
}
