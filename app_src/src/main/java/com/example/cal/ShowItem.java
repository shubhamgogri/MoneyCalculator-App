package com.example.cal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowItem extends AppCompatActivity {
    private TextView prev_money_text;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        prev_money_text = findViewById(R.id.prev_money_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("List").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data ="";
                        for (QueryDocumentSnapshot snapshots: queryDocumentSnapshots){
                            Bank bank = snapshots.toObject(Bank.class);
                            String title = bank.getTitle();
                            String money = bank.getMoney();
                            String date = bank.getDate();
                            data += title.toUpperCase() + ":- " + money + " | "+ date + "\n\n";
                        }
                        prev_money_text.setText(data);
                    }
                });
    }
}
