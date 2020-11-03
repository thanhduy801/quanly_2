package com.example.appquanli12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appquanli12.Interface.ItemClickListener;
import com.example.appquanli12.Model.Category;
import com.example.appquanli12.Model.Food;
import com.example.appquanli12.ViewHolder.CategoryViewHolder;
import com.example.appquanli12.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Food_Menu extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference foods;

    RecyclerView recyclerfood;
    RecyclerView.LayoutManager layoutManager;

    String categoryId="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__menu);

        Toolbar toolbar=findViewById(R.id.toolbar_food);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Danh sách món ăn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Load menu
        recyclerfood = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerfood.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerfood.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() !=null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty()&&categoryId!=null)
        {
            loadListFood (categoryId);
        }
    }

    private void loadListFood(String categoryId) {
        FirebaseRecyclerOptions<Food> options= new FirebaseRecyclerOptions.Builder<Food>().setQuery(foods.orderByChild("MenuId").equalTo(categoryId),Food.class).build();
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.txtFoodName.setText(model.getName());
                holder.txtFoodMoney.setText(model.getPrice()+" đ");
                Picasso.with(getBaseContext()).load(model.getImg()).into(holder.imgfood);
                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Food_Menu.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent, false);
                FoodViewHolder viewHolder=  new FoodViewHolder(view);
                return viewHolder;
            }
        };


        recyclerfood.setAdapter(adapter);
        adapter.startListening();
    }
}