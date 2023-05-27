package com.hanaa.confessions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

    ListView lvFavourites;

    ArrayList<String> list;

    ArrayAdapter myAdapter;

    DatabaseReference databaseReference;

    DatabaseReference myreff;

    ArrayList<String> keys;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_favourite, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        lvFavourites = view.findViewById( R.id.lvFavourites );

        myreff = FirebaseDatabase.getInstance().getReference("All_Messages");

        list = new ArrayList<>();
        keys = new ArrayList<>();

        myAdapter = new ArrayAdapter<String>( getContext(), R.layout.listrow ,R.id.textView2, list );

        lvFavourites.setAdapter( myAdapter );

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();
        String Email = email.replace( ".", ","  );



        databaseReference = FirebaseDatabase.getInstance().getReference("Favourites").child( Email );

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String fav = data.getValue(String.class);
                    list.add( fav );
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        lvFavourites.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder myAlert = new AlertDialog.Builder( getContext() );
                myAlert.setTitle( "Delete item" )
                        .setMessage( "Are you sure you want to delete this item from your favourite list?" )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child( String.valueOf( position ) ).removeValue();
                                list.remove( position );
                                myAdapter.notifyDataSetChanged();

                            }
                        } )
                        .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        } ).create();
                myAlert.show();



                return false;
            }
        } );

        lvFavourites.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        } );

    }
}
