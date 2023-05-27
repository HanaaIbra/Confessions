package com.hanaa.confessions;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConfessionsAdapter extends RecyclerView.Adapter<ConfessionsAdapter.ViewHolder>{

    List<Confession> list;

    Context context;
    List<String> fav;


    //An object of the interface OnItemClickListener
    private OnItemClickListener mListener;



    public ConfessionsAdapter(Context context, List<Confession> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.rv_item, viewGroup, false );
        ViewHolder viewHolder = new ViewHolder( view );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Confession confession = list.get( i );
        viewHolder.confession.setText( confession.getConfession() );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        public TextView confession;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            confession = itemView.findViewById( R.id.itemTextView );

            itemView.setOnClickListener( this );
            itemView.setOnCreateContextMenuListener( this );
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()){
                        case 1:
                            mListener.onReportClick( position );
                            return true;

                        case 2:
                            mListener.onFavouriteClick( position );
                    }
                }}

            return false;
        }

        @Override
        public void onClick(View v) {

            if(mListener != null){
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick( position );
                }}}

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            MenuItem report = menu.add( Menu.NONE, 1,1, "Report Confession" );
            MenuItem favourite = menu.add( Menu.NONE, 2,2, "Add to favourite" );
            report.setOnMenuItemClickListener( this );
            favourite.setOnMenuItemClickListener( this );
        }}

    public interface OnItemClickListener{

        void onItemClick(int position);
        void onReportClick(int position);
        void onFavouriteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener = mListener;
    }}
