package com.example.studyroom.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studyroom.R;
import com.example.studyroom.model.Library;

import java.util.List;

public class LibraryRecyclerViewAdapter extends RecyclerView.Adapter<LibraryRecyclerViewAdapter.ViewHolder> {

    private List<Library> libraries;
    private Context context;

    public LibraryRecyclerViewAdapter(Context context, List<Library> libraries) {
        this.context = context;
        this.libraries = libraries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Library library = libraries.get(position);
        holder.libraryNameTextView.setText(library.getName());
        holder.libraryAddressTextView.setText(library.getAddress().getDistrict() + ", " + library.getAddress().getLocality());

        Glide.with(context)
                .load(library.getImageUrl())
                .placeholder(R.drawable.app_background_image)
                .into(holder.libraryImageView);
    }

    @Override
    public int getItemCount() {
        return libraries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView libraryImageView;
        TextView libraryNameTextView;
        TextView libraryAddressTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            libraryImageView = itemView.findViewById(R.id.image_library);
            libraryNameTextView = itemView.findViewById(R.id.text_library_name);
            libraryAddressTextView = itemView.findViewById(R.id.text_library_address);
        }
    }
}
