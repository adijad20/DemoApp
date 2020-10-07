package com.example.demoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {
    private List<ImageFile> myImages;
    private ViewGroup parent;
    private  int THUMBSIZE = 512;
     public MyRecycleViewAdapter(List<ImageFile> imageList){
         this.myImages = new ArrayList<ImageFile>(imageList);
     }

    @NonNull
    @Override
    public MyRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_layout,null);
        this.parent = parent;
         ViewHolder viewHolder = new ViewHolder(itemLayoutView);
         return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtViewTitle.setText(myImages.get(position).getTitle());
        //Bitmap myBitmap = BitmapFactory.decodeFile(myImages.get(position).getPath());
        //holder.imgViewIcon.setImageBitmap(myBitmap);
        final Uri uri = myImages.get(position).getUri();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(parent.getContext().getContentResolver(), uri);
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bitmap,
                    THUMBSIZE, THUMBSIZE);
            holder.imgViewIcon.setImageBitmap(ThumbImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //holder.imgViewIcon.setImageURI(myImages.get(position).getUri());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(),ImageFullScreenActivity.class);
                intent.putExtra("imageUri", uri.toString());
                parent.getContext().startActivity(intent);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtViewTitle;
        public ImageView imgViewIcon;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.image_name);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.image);
        }
    }

    @Override
    public int getItemCount() {
        return myImages.size();
    }

}
