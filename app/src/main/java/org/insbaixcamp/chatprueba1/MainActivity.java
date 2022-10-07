package org.insbaixcamp.chatprueba1;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String DATABASE_NAME = "https://chat-b1b1d-default-rtdb.europe-west1.firebasedatabase.app/";
    FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_NAME);
    DatabaseReference databaseRoot = database.getReference("Chats");
    RecyclerAdapter recyclerAdapter;
    List<Mensaje> chatList;
    LinearLayoutManager mLayoutManager;
    Button btAutentificacion;
    Usuario usuario;
    ImageView ivEnviar;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = Autentificacion.getUsuario();
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rvMensajes);
        ivEnviar = findViewById(R.id.IbEnviar);
        ivEnviar.setOnClickListener(this);
        chatList = new ArrayList();
        btAutentificacion = findViewById(R.id.btAutentificacion);
        btAutentificacion.setOnClickListener(this);
        recyclerAdapter = new RecyclerAdapter(chatList, MainActivity.this, usuario);

        recyclerAdapter = new RecyclerAdapter(chatList, MainActivity.this, usuario);
        mRecyclerView.setAdapter(recyclerAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    Mensaje mensaje = item.getValue(Mensaje.class);
                    chatList.add(mensaje);
                }

                recyclerAdapter.notifyDataSetChanged();

                setScrollbar(recyclerAdapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseRoot.addValueEventListener(postListener);

    }
    public void setScrollbar(int itemCount){
        mRecyclerView.scrollToPosition(itemCount);
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == ivEnviar.getId()){
            String text = "fallé";
            EditText etMensaje = findViewById(R.id.etMensaje);
            if(etMensaje.getText() != null){
                writeText(etMensaje.getText().toString());
                etMensaje.setText("");
            }
        } else {
            Intent intent = new Intent(this, Autentificacion.class);
            intent.putExtra(EXTRA_MESSAGE, "sing out");
            startActivity(intent);
        }

    }


    public void writeText(String text){
        String messageKey = databaseRoot.push().getKey();

        databaseRoot.child(messageKey).setValue( new Mensaje(messageKey, usuario.getName(),text));
    }




    /*  @Override
  public void onClick(View view) {

        String text = "fallé";
        EditText etMensaje = findViewById(R.id.etMensaje);
        if(etMensaje.getText() != null){
            text = etMensaje.getText().toString();
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_NAME);
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue(text);
    }*/
}