package ir.peivandyar.ui.adapter;



import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.roomwordssample.R;

import java.util.List;
import ir.peivandyar.database.entity.SchoolInfo;
import ir.peivandyar.ui.util.OnListInteractionsListener;


public class SchoolInfoListAdapter extends RecyclerView.Adapter<SchoolInfoListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView schoolToChange;
        private final TextView addressToChange;
        private final TextView phoneToChange;
        private final ImageView clickImage;
        private final AppCompatImageView schoolPic;
        private final CardView heart;


        private WordViewHolder(View itemView) {
            super(itemView);
            schoolToChange = itemView.findViewById(R.id.schoolName);
            addressToChange = itemView.findViewById(R.id.schoolAddress);
            phoneToChange = itemView.findViewById(R.id.schoolPhone);
            clickImage = itemView.findViewById(R.id.schoolCardClick);
            heart = itemView.findViewById(R.id.heartCircle);
            schoolPic = itemView.findViewById(R.id.schoolPicture);
        }
    }

    private final LayoutInflater mInflater;
    private List<SchoolInfo> mSchools; // Cached copy of schoolInfos
    private Context context;
    private OnListInteractionsListener listener;

    public SchoolInfoListAdapter(Context context, OnListInteractionsListener listener) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener  = listener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.school_card, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        SchoolInfo current = mSchools.get(position);
        holder.schoolToChange.setText(current.getSchoolName());
        holder.phoneToChange.setText(current.getPhoneNumber());
        holder.addressToChange.setText(current.getAddress());
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStateChanged(1);
            }
        });
        holder.clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStateChanged(2);
            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(5));
        Glide.with(this.context)
                .load(current.getImageUrl())
                .apply(requestOptions)
                .into(holder.schoolPic);
    }

    public void setSchool(List<SchoolInfo> schoolInfos){
        mSchools = schoolInfos;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mSchools has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mSchools != null)
            return mSchools.size();
        else return 0;
    }
}


