package com.example.tipcalculator;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener, View.OnClickListener {

    private String billAmountString = "";
    private float tipPercent = .15f;

    //define variables for the user interface control we want to interact with
    private EditText billAmountEditText;
    private TextView percentTextView;
    private Button percentUpButton;
    private Button percentDownButton;
    private TextView tipTextView;
    private TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get references to the UI controls
        billAmountEditText = findViewById(R.id.billAmountEditText);
        percentTextView = findViewById(R.id.percentTextView);
        percentUpButton = findViewById(R.id.percentUpButton);
        percentDownButton = findViewById(R.id.percentDownButton);
        tipTextView = findViewById(R.id.tipTextView);
        totalTextView = findViewById(R.id.TotalTextView);

        //set the listeners
        billAmountEditText.setOnEditorActionListener(this);
        percentDownButton.setOnClickListener(this);
        percentUpButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            //get the bill amount
            billAmountString = savedInstanceState.getString("billAmountString", "");
            tipPercent = savedInstanceState.getFloat("tipPercent", 0.15f);
        }

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("billAmountString", billAmountString);
        outState.putFloat("tipPercent", tipPercent);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            billAmountString = savedInstanceState.getString("billAmountString", "");
            tipPercent = savedInstanceState.getFloat("tipPercent", 0.15f);

            billAmountEditText.setText(billAmountString);
            calculateAndDisplay();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if (id == R.id.percentDownButton) {
//            tipPercent = Math.max(0, tipPercent - 0.01f);
//            updateUI();
//        } else if (id == R.id.percentUpButton) {
//            tipPercent += 0.01f;
//            updateUI();
//        }
        if (v.getId() == R.id.percentDownButton) {
            // Reduce the tip percentage with a minimum limit of 0
            if (tipPercent > 0.01f) {
                tipPercent -= 0.01f;
            } else {
                tipPercent = 0.0f;
            }
            calculateAndDisplay();
            updateUI();
        } else if (v.getId() == R.id.percentUpButton) {
            // Increase the tip percentage with a maximum limit of 100%
            if (tipPercent < 1.0f) {
                tipPercent += 0.01f;
            } else {
                tipPercent = 1.0f;
            }
            calculateAndDisplay();
            updateUI();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
            return true;
        }
        return false;
    }

    private void calculateAndDisplay() {
        billAmountString = billAmountEditText.getText().toString();
        float billAmount = 0;
        try {
            if (!billAmountString.isEmpty()) {
                billAmount = Float.parseFloat(billAmountString);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input! Please enter a valid number.", Toast.LENGTH_SHORT).show();
            billAmountEditText.setText("");
            return;
        }

        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));
    }

    private void updateUI() {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(tipPercent));
        calculateAndDisplay();
    }
}