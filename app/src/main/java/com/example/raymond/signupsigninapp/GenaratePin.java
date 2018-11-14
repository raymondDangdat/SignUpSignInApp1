package com.example.raymond.signupsigninapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Modell.Pin;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class GenaratePin extends AppCompatActivity {

    private Button btnGenerate;


    private FirebaseListAdapter<Pin> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genarate_pin);



        btnGenerate = findViewById(R.id.button);





        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Random random = new Random();
                    final int number = random.nextInt(919345996)*5;



                    Pin newPin = new Pin();
                    newPin.setPin(number);
                    newPin.setStatus("unused");
                    FirebaseDatabase.getInstance().getReference("Pins").push().setValue(newPin);


                Toast.makeText(GenaratePin.this, "Pin generated", Toast.LENGTH_SHORT).show();

//
//                    FirebaseDatabase.getInstance().getReference("Pins").push().setValue(new Pin(number, "unused")).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(GenaratePin.this, "PIN generated successfully" +number, Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(GenaratePin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });




            }
        });

        displayPins();
    }

    private void displayPins() {
        ListView listOfPins = findViewById(R.id.list_of_pins);
        adapter = new FirebaseListAdapter<Pin>(
                this,
                Pin.class,
                R.layout.pins_item,
                FirebaseDatabase.getInstance().getReference("Pins")
                ) {
            @Override
            protected void populateView(View v, Pin model, int position) {
                TextView pin, genTime;
                pin = v.findViewById(R.id.pin);
                genTime = v.findViewById(R.id.pin_time);

                pin.setText(model.getPin());
                genTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getPinTime()));




            }
        };
        //listOfPins.setAdapter(adapter);
    }
}
