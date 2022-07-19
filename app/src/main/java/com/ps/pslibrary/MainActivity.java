package com.ps.pslibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvMyText = findViewById(R.id.tvMyText);
        String strToColor = "Text: This is my text";
        String before = strToColor.substring(0, strToColor.indexOf(":"));
//        tvMyText.setText(PSMethods.colorStr("This is my text", PSColor.GREEN, PSRegex.AFTER, PSRegex.SPACE));

        Spannable spannable = new SpannableString(strToColor);
        spannable.setSpan(new ForegroundColorSpan(PSColor.GREEN), before.length() + 1, strToColor.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMyText.setText(spannable);
    }
}