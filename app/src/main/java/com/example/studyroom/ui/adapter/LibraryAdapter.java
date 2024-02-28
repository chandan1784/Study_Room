package com.example.studyroom.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide; // Assuming you're using Glide for image loading
import com.example.studyroom.R;
import com.example.studyroom.model.Library;

import java.util.List;

public class LibraryAdapter extends ArrayAdapter<Library> {

    private List<Library> libraries;
    private Context context;

    public LibraryAdapter(Context context, List<Library> libraries) {
        super(context, 0, libraries);
        this.context = context;
        this.libraries = libraries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_library, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.libraryImageView = convertView.findViewById(R.id.image_library);
            viewHolder.libraryNameTextView = convertView.findViewById(R.id.text_library_name);
            viewHolder.libraryAddressTextView = convertView.findViewById(R.id.text_library_address);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Library library = libraries.get(position);

        // Load library image using Glide
        Glide.with(context)
                .load(library.getImageUrl()) // Assuming Library object has a method getImageUrl() returning image URL
                .placeholder(R.drawable.library_placeholder)
                .into(viewHolder.libraryImageView);

        // Set library name and address
        viewHolder.libraryNameTextView.setText(library.getName());
        viewHolder.libraryAddressTextView.setText(library.getAddress().getDistrict() + ", " + library.getAddress().getLocality());

        // Load library image using Glide (assuming you have a method to get the image URL from Library object)
        // Glide.with(context).load(library.getImageUrl()).placeholder(R.drawable.library_placeholder).into(viewHolder.libraryImageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView libraryImageView;
        TextView libraryNameTextView;
        TextView libraryAddressTextView;
    }
}
