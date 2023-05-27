package com.hanaa.confessions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;/*
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements ConfessionsAdapter.OnItemClickListener{

    RecyclerView rvList;
    DatabaseReference databaseReference;
    List<Confession> list;
    public static ConfessionsAdapter myAdapter;
    ArrayList<String> favourites;
    ProgressBar myProgress;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here


        rvList = view.findViewById( R.id.rvConfessions );
        rvList.setHasFixedSize( true );
        rvList.setLayoutManager( new LinearLayoutManager( getContext() ) );

        myProgress = view.findViewById( R.id.myProgress );

        list = new ArrayList<>();
        favourites = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("All_Messages");
        Collections.reverse( list );
        myAdapter = new ConfessionsAdapter( getContext(), list );
        rvList.setAdapter( myAdapter );
        myAdapter.setOnItemClickListener( HomeFragment.this );

        myProgress.setVisibility( View.VISIBLE );
        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Confession confession = data.getValue(Confession.class);
                    confession.setmKey( data.getKey() );
                    list.add(0, confession );
                   // Collections.reverse( list );


                }
                myAdapter.notifyDataSetChanged();
                myProgress.setVisibility( View.GONE );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                myProgress.setVisibility( View.GONE );
            }});

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();
        String Email = email.replace( ".", ","  );
        DatabaseReference getListRef = FirebaseDatabase.getInstance().getReference("Favourites").child( Email );
        getListRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                favourites.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String exfav = data.getValue(String.class);
                    favourites.add( exfav );

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_home, container, false );
    }

    @Override
    public void onItemClick(int position) {

        Confession conf = list.get( position );
        String key = conf.getmKey();

        Intent intent = new Intent(getContext(), ConfessionDetails.class);
        intent.putExtra( "Confession", conf.getConfession());
        intent.putExtra( "Key", key );
        startActivity( intent );
    }

    @Override
    public void onReportClick(int position) {

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Reports");
        String reportKey = list.get( position ).getmKey();
        String Email = list.get( position ).getUserEmail();
        String Confes = list.get( position ).getConfession();
        reff.child( Email ).child( reportKey ).setValue(Confes);
        Toast.makeText( getContext(), "Thanks for your feedback, we will look into it :)", Toast.LENGTH_LONG ).show();

    }

    @Override
    public void onFavouriteClick(int position) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();
        String Email = email.replace( ".", ","  );



        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Favourites");
        String confessionTxt = list.get( position ).getConfession();

        favourites.add( confessionTxt );

        Confession conf = list.get( position );
        String key = conf.getmKey();

        Intent intent = new Intent(getContext(), FavouriteFragment.class);
        intent.putExtra( "Confession", conf.getConfession());
        intent.putExtra( "Key", key );

        Confession con = new Confession();
        con.setFavList( favourites );


        reff.child(Email).setValue( favourites );
        Toast.makeText( getContext(), "Added to favourite succesfully", Toast.LENGTH_LONG ).show();

    }

}