package com.hanaa.confessions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteFragment extends Fragment {

    EditText etConfession;
    Button btnsend;

    DatabaseReference myRef;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        etConfession = view.findViewById(R.id.etConfession);
        btnsend = view.findViewById( R.id.btnSend );

        btnsend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String Email = currentUser.getEmail().replace( ".", ","  );

                myRef = FirebaseDatabase.getInstance().getReference("All_Messages");
                Confession message = new Confession(  );
                message.setConfession( etConfession.getText().toString() );
                message.setUserEmail( Email );

                String confessionId = myRef.push().getKey();
                myRef.child( confessionId ).setValue(message);
                etConfession.setText( "" );
                Toast.makeText( getContext(), "Confessions was sent succesfully", Toast.LENGTH_LONG ).show();
            }
        });}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_write, container, false );
    }
}
