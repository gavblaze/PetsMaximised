package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by Gavin on 28-Jun-17.
 */

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;


    public PetAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public PetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PetAdapter.ViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        int nameIndex = mCursor.getColumnIndex(PetContract.PetEntry.PET_NAME);
        int breedIndex = mCursor.getColumnIndex(PetContract.PetEntry.PET_BREED);
        int genderIndex = mCursor.getColumnIndex(PetContract.PetEntry.PET_GENDER);
        int weightIndex = mCursor.getColumnIndex(PetContract.PetEntry.PET_WEIGHT);



            holder.mNameTextView.setText(mCursor.getString(nameIndex));
            holder.mBreedTextView.setText(mCursor.getString(breedIndex));
//            holder.mGenderTextView.setText(String.valueOf(mCursor.getInt(genderIndex)));
//            holder.mGenderTextView.setText(String.valueOf(mCursor.getInt(weightIndex)));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(CatalogActivity catalogActivity, Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public TextView mBreedTextView;
//        public TextView mGenderTextView;
//        public TextView mWeightTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.rv_name_tv);
            mBreedTextView = (TextView) itemView.findViewById(R.id.rv_breed_tv);
//            mGenderTextView = (TextView) itemView.findViewById(R.id.rv_gender_tv);
//            mWeightTextView = (TextView) itemView.findViewById(R.id.rv_weight_tv);
        }
    }
}
