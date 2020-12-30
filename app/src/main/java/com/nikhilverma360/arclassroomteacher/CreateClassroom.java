package com.nikhilverma360.arclassroomteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateClassroom extends AppCompatActivity {
    Button create;
    EditText className, section, room, subject;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);

        create = findViewById(R.id.createbtn);
        className = findViewById(R.id.classnametxt);
        section = findViewById(R.id.sectiontxt);
        room = findViewById(R.id.roomtxt);
        subject = findViewById(R.id.subjecttxt);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClassroom();
            }
        });

    }

    private void createClassroom() {
        final Map<String, Object> classroom = new HashMap<>();
        classroom.put("classname", className.getText().toString());
        classroom.put("section", section.getText().toString());
        classroom.put("room", room.getText().toString());
        classroom.put("subject", subject.getText().toString());

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        db.collection("classes")
                .add(classroom)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        addClassCode(documentReference.getId(),classroom,pDialog);

                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(CreateClassroom.this, "Classroom Created", Toast.LENGTH_SHORT).show();

                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                        Toast.makeText(CreateClassroom.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        new SweetAlertDialog(CreateClassroom.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Something went wrong!")
                                .show();
                    }
                });
    }

    private void addClassCode(String code, Map<String, Object> classroom, final SweetAlertDialog pDialog) {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        assert signInAccount != null;
        db.collection("users").document(Objects.requireNonNull(signInAccount.getId())).collection("classes")
                .document(code)
                .set(classroom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        pDialog.dismiss();
                        new SweetAlertDialog(CreateClassroom.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Classroom has been added successfully").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

    }
}