package com.example.srec;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RetrievePDFActivvity extends AppCompatActivity {

    RecyclerView pdfRecyclerView;
    private DatabaseReference pRef;
    Query query;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_pdfactivvity);

        displayPdfs();
    }

    private void displayPdfs() {

        pRef = FirebaseDatabase.getInstance().getReference().child("pdfs");
        pdfRecyclerView = findViewById(R.id.recyclerView);
        pdfRecyclerView.setHasFixedSize(true);
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        query = pRef.orderByChild("fileName");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    showPdf();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RetrievePDFActivvity.this, "No PDFs found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RetrievePDFActivvity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPdf() {
        FirebaseRecyclerOptions<FileinModel> options = new FirebaseRecyclerOptions.Builder<FileinModel>()
                .setQuery(query, FileinModel.class)
                .build();
        FirebaseRecyclerAdapter<FileinModel, ViewHolder> adapter;
        adapter = new FirebaseRecyclerAdapter<FileinModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FileinModel model) {

                progressBar.setVisibility(View.GONE);
                holder.pdfTitle.setText(model.getFileName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("application/pdf");
                        intent.setData(Uri.parse(model.getFileUrl()));
                        startActivity(intent);
                    }
                });

                // Set other text views
                holder.blackAndWhiteChecked.setText("Black and White: " + model.isBlackAndWhiteChecked());
                holder.caligoChecked.setText("Caligo Checked: " + model.isCaligoChecked());
                holder.colorChecked.setText("Color Checked: " + model.isColorChecked());
                holder.copiesBlackAndWhite.setText("Copies Black and White: " + model.getCopiesBlackAndWhite());
                holder.copiesColor.setText("Copies Color: " + model.getCopiesColor());
                holder.spiralChecked.setText("Spiral Checked: " + model.isSpiralChecked());
                holder.totalCost.setText("Total Cost: " + model.getTotalCost());
                holder.userEmail.setText("User Email: " + model.getUserEmail());
                holder.userName.setText("User Name: " + model.getUserName());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item, parent, false);
                return new ViewHolder(view);
            }
        };

        pdfRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pdfTitle;
        TextView blackAndWhiteChecked;
        TextView caligoChecked;
        TextView colorChecked;
        TextView copiesBlackAndWhite;
        TextView copiesColor;
        TextView spiralChecked;
        TextView totalCost;
        TextView userEmail;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfTitle = itemView.findViewById(R.id.pdf_name);
            blackAndWhiteChecked = itemView.findViewById(R.id.black_and_white_checked);
            caligoChecked = itemView.findViewById(R.id.caligo_checked);
            colorChecked = itemView.findViewById(R.id.color_checked);
            copiesBlackAndWhite = itemView.findViewById(R.id.copies_black_and_white);
            copiesColor = itemView.findViewById(R.id.copies_color);
            spiralChecked = itemView.findViewById(R.id.spiral_checked);
            totalCost = itemView.findViewById(R.id.total_cost);
            userEmail = itemView.findViewById(R.id.user_email);
            userName = itemView.findViewById(R.id.user_name);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
