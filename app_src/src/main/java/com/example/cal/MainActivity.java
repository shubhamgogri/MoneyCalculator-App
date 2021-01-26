package com.example.cal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener , View.OnClickListener {
    private EditText alert_title , edit_2000 ,edit_200 ,edit_500, edit_100;
    private TextView alert_money ,text_2000, text_500, text_200, text_100, total;
    public final int five_hundred = 500;
    public final int two_thousand = 2000;
    public final int two_hundred = 200;
    public final int one_hundred = 100;
    private Button save_money , show_list, alert_save;

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  AlertDialog.Builder builder ;
    private AlertDialog dialog;
    Bank bank =new Bank();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save_money = findViewById(R.id.save_money);
        show_list = findViewById(R.id.show_list);

        edit_2000 = findViewById(R.id.edit_2000);
        edit_200 = findViewById(R.id.edit_200);
        edit_500 = findViewById(R.id.edit_500);
        edit_100 = findViewById(R.id.edit_100);

        text_100 = findViewById(R.id.text_100);
        text_200 = findViewById(R.id.text_200);
        text_500 = findViewById(R.id.text_500);
        text_2000 = findViewById(R.id.text_2000);
        total = findViewById(R.id.text_total);

        edit_2000.setOnKeyListener(this);
        edit_500.setOnKeyListener(this);
        edit_200.setOnKeyListener(this);
        edit_100.setOnKeyListener(this);
        total.setOnClickListener(this);
        show_list.setOnClickListener(this);
        save_money.setOnClickListener(this);

    }

    private int five_hundred_calc() {

        if (!edit_500.getText().toString().isEmpty()){
            int five_00 = Integer.parseInt(edit_500.getText().toString().trim());
            return (five_00 * five_hundred);
        }else
            return 0;
    }

    private int two_hundred_calc() {
        if(!edit_200.getText().toString().isEmpty()){
            int two_00 = Integer.parseInt(edit_200.getText().toString().trim());
            return (two_00 * two_hundred);
        }else
            return 0;
    }

    private int one_hundred_calc(){
        if(!edit_100.getText().toString().trim().isEmpty()) {
            int one_00 = Integer.parseInt(edit_100.getText().toString().trim());
            return one_00 *one_hundred;
        }else
            return 0;
    }

    private int two_thousand_calc(){
        if (!edit_2000.getText().toString().isEmpty()){
            int two_000 = Integer.parseInt(edit_2000.getText().toString().trim());
            return two_000 * two_thousand;
        }else
            return 0;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)){
            switch (v.getId()){
                case R.id.edit_100:
                    text_100.setText(numberFormat.format(one_hundred_calc()));
                    break;
                case R.id.edit_200:
                    text_200.setText(numberFormat.format(two_hundred_calc()));
                    break;
                case R.id.edit_500:
                    text_500.setText(numberFormat.format(five_hundred_calc()));
                    break;
                case R.id.edit_2000:
                    text_2000.setText(numberFormat.format(two_thousand_calc()));
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.text_total:
                total.setText(numberFormat.format(one_hundred_calc() + two_hundred_calc() + five_hundred_calc() + two_thousand_calc()));
                break;

            case R.id.save_money:
                if (!total.getText().toString().trim().equals("total"))
                    openDialog();
                else
                    Toast.makeText(MainActivity.this, "Calculate First",Toast.LENGTH_SHORT).show();
                break;

            case R.id.show_list:
                show_list_activity();
                break;

            case R.id.save_alert:
                save_in_firestore();
                break;
        }
    }

    private void save_in_firestore() {

        if (!alert_title.getText().toString().isEmpty()){
            bank.setMoney(alert_money.getText().toString().trim());
            bank.setTitle(alert_title.getText().toString());
            bank.setDate(java.text.DateFormat.getDateTimeInstance().format(new Date()));
            db.collection("List")
                    .add(bank)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "Success",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this , e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            dialog.dismiss();
        }else {
            Toast.makeText(this,"Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
        }
    }

    private void show_list_activity() {
        Intent intent =new Intent(MainActivity.this, ShowItem.class);
        startActivity(intent);
    }

    private void openDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_save, null);
        alert_title = view.findViewById(R.id.title);
        alert_money = view.findViewById(R.id.dialog_money_text);
        alert_save = view.findViewById(R.id.save_alert);

        alert_money.setText(total.getText());
        alert_save.setOnClickListener(this);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }
}
