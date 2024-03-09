package com.example.srec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.pdfreader.FileinModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText edit;
    Button uploadBTn;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    private EditText edtCopiesBlackAndWhite;
    private EditText edtCopiesColor;

    private CheckBox checkboxBlackAndWhite;
    private CheckBox checkboxColor;
    private CheckBox checkboxSpiral;
    private CheckBox checkboxCaligo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCopiesBlackAndWhite = findViewById(R.id.edtCopiesBlackAndWhite);
        edtCopiesColor = findViewById(R.id.edtCopiesColor);

        checkboxBlackAndWhite = findViewById(R.id.checkboxBlackAndWhite);
        checkboxColor = findViewById(R.id.checkboxColor);
        checkboxSpiral = findViewById(R.id.checkboxSpiral);
        checkboxCaligo = findViewById(R.id.checkboxCaligo);


        edit = findViewById(R.id.editText);
        uploadBTn = findViewById(R.id.btn);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("pdfs");

        //set the upload btn disable first without selecting the pdf file
        uploadBTn.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });

    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf files"),101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri =data.getData();
            String uriString  = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;

            if (uriString.startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri,null,null,null,null);
                    if (cursor != null && cursor.moveToFirst()){
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else  if (uriString.startsWith("file://")){
                displayName = myFile.getName();
            }

            uploadBTn.setEnabled(true);
            edit.setText(displayName);

            uploadBTn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadPDF(data.getData());
                }
            });
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("File Uploading..");
        pd.show();

        final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");

        // Get current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Fetch current user's information from the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String userName = dataSnapshot.child("name").getValue(String.class);

                    // Calculate total cost
                    int totalCost = calculateTotalCost();

                    // Store the information in the database
                    reference.putFile(data).addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        // Get the number of copies from EditText fields
                        int copiesBlackAndWhite = Integer.parseInt(edtCopiesBlackAndWhite.getText().toString().trim());
                        int copiesColor = Integer.parseInt(edtCopiesColor.getText().toString().trim());

                        FileinModel fileinModel = new FileinModel(edit.getText().toString(), uri.toString(),
                                checkboxBlackAndWhite.isChecked(), checkboxColor.isChecked(),
                                checkboxSpiral.isChecked(), checkboxCaligo.isChecked(),
                                copiesBlackAndWhite, copiesColor, totalCost, userName, userEmail);
                        databaseReference.child(databaseReference.push().getKey()).setValue(fileinModel);
                        Toast.makeText(MainActivity.this, "File Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }).addOnProgressListener(snapshot -> {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded : " + (int) percent + "%");
                    });
                } else {
                    Toast.makeText(MainActivity.this, "User information not found", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private int calculateTotalCost() {
        // Get the text from EditText fields
        String textCopiesBlackAndWhite = edtCopiesBlackAndWhite.getText().toString().trim();
        String textCopiesColor = edtCopiesColor.getText().toString().trim();

        // Check if the EditText fields are empty
        if (textCopiesBlackAndWhite.isEmpty() || textCopiesColor.isEmpty()) {
            Toast.makeText(this, "Please enter the number of copies", Toast.LENGTH_SHORT).show();
            return 0;
        }

        // Convert the text to integers
        int copiesBlackAndWhite = Integer.parseInt(textCopiesBlackAndWhite);
        int copiesColor = Integer.parseInt(textCopiesColor);

        int totalCost = 0;

        if (checkboxBlackAndWhite.isChecked()) {
            totalCost += copiesBlackAndWhite * 1; // Rs. 1 per page
        }

        if (checkboxColor.isChecked()) {
            totalCost += copiesColor * 10; // Rs. 10 per page
        }

        if (checkboxSpiral.isChecked()) {
            totalCost += 40; // Rs. 40
        }

        if (checkboxCaligo.isChecked()) {
            totalCost += 60; // Rs. 60
        }

        return totalCost;
    }



    public void retrievePDFs(View view) {
        // now here we will extract those pdf files
        startActivity(new Intent(MainActivity.this, RetrievePDFActivvity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}