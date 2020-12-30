package com.nikhilverma360.arclassroomteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ARClassroom extends AppCompatActivity {
    private TextView classname,section,room,subject,classcode;

    CardView mArzonecard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_r_classroom);

        classname= findViewById(R.id.arclassname);
        section=findViewById(R.id.arclasssection);
        room=findViewById(R.id.arclassroomnum);
        subject= findViewById(R.id.arclasssubject);
        classcode= findViewById(R.id.arclasscode);
        Intent i = getIntent();

        classname.setText(i.getStringExtra("classname"));
        section.setText(i.getStringExtra("section"));
        room.setText(i.getStringExtra("room"));
        subject.setText(i.getStringExtra("subject"));
        classcode.setText(i.getStringExtra("classcode"));

        mArzonecard = findViewById(R.id.arzonecard);
        mArzonecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ARZone.class);
                startActivity(intent);
            }
        });
    }
}