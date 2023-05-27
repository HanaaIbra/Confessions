package com.hanaa.confessions;

/*import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

public class ConfessionDetails extends AppCompatActivity {

    TextView textView;
    EditText etComment;
    ListView lvComments;
    Button btnComment;
    ArrayList<String> commentsList;
    DatabaseReference databaseReference;
    ArrayAdapter<String> myAdapter;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_confession_details );

        commentsList = new ArrayList<>();

        lvComments = findViewById( R.id.lvComments );
        etComment = findViewById( R.id.etComment );
        textView = findViewById( R.id.editText2 );
        btnComment = findViewById( R.id.btnComment );

        textView.setMovementMethod( new ScrollingMovementMethod() );

        myAdapter = new ArrayAdapter<String>( ConfessionDetails.this, R.layout.listrow,R.id.textView2, commentsList );


        databaseReference = FirebaseDatabase.getInstance().getReference("All_Messages");


        final String conf = getIntent().getStringExtra( "Confession" );
        key = getIntent().getStringExtra( "Key" );
        textView.setText( conf );

        lvComments.setAdapter( myAdapter );



        databaseReference.child( key ).child( "commentsList" ).addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentsList.clear();

                for( DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    String comment = postSnapshot.getValue(String.class);
                    commentsList.add( comment );
                }

                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});

        btnComment.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String Email = currentUser.getEmail().replace( ".", ","  );
                String comment = etComment.getText().toString();
                commentsList.add( comment );
                Confession confession = new Confession();
                confession.setConfession( conf );
                confession.setUserEmail( Email );
                confession.setCommentsList(commentsList);
                databaseReference.child( key ).setValue( confession );
                etComment.setText( "" );
            }});



    }
}
