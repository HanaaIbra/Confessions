package com.hanaa.confessions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MyConfessions extends AppCompatActivity {


    DatabaseReference myreff;
    FirebaseUser currentUser;

    ArrayList<String> keys;

    ListView myConfessionsList;
    ArrayAdapter<String> myAdapter;
    ArrayList<String> myConfessionsL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_confessions );

        keys = new ArrayList<>();

        myConfessionsList = findViewById( R.id.myConfessionsList );
        myConfessionsL = new ArrayList<>();
        myAdapter = new ArrayAdapter<>( MyConfessions.this, R.layout.listrow, R.id.textView2, myConfessionsL  );
        myConfessionsList.setAdapter( myAdapter );

        myreff = FirebaseDatabase.getInstance().getReference("All_Messages");
        myreff.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String Email = currentUser.getEmail().replace( ".", ","  );

                for (DataSnapshot data: dataSnapshot.getChildren()){

                    Confession con = data.getValue(Confession.class);
                    String email = con.getUserEmail();
                    String confes = con.getConfession();
                    String mKey = data.getKey();
                    if(Email.equals( email )){
                        myConfessionsL.add(0, confes );
                        keys.add( mKey );
                        //Collections.reverse( myConfessionsL );

                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        myConfessionsList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = keys.get( position  );

                Intent intent = new Intent(MyConfessions.this, ConfessionDetails.class);
                intent.putExtra("Confession", myConfessionsL.get( position ));
                intent.putExtra( "Key", key );
                startActivity( intent );

            }
        } );

        myConfessionsList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                final AlertDialog.Builder myAlert = new AlertDialog.Builder( MyConfessions.this );
                myAlert.setTitle( "Delete item" )
                        .setMessage( "Are you sure you want to delete this item?" )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Collections.reverse( keys );
                                String key = keys.get( position );
                                myreff.child( key ).removeValue();
                                myConfessionsL.remove( position );
                                myAdapter.notifyDataSetChanged();
                                MyConfessions.this.finish();
                                startActivity( getIntent() );
                            }
                        })
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

    }
}
