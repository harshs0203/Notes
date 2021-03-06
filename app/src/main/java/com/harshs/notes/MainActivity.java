package com.harshs.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.harshs.notes", Context.MODE_PRIVATE);
        ListView listView =  findViewById(R.id.listView);
        TextView textView = findViewById(R.id.textView);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);

        if(set!=null){

            textView.setVisibility(View.INVISIBLE);

            notes = new ArrayList<>(set);
        }
        if(notes.size() == 0){

            textView.setVisibility(View.VISIBLE);

        }else{
            textView.setVisibility(View.INVISIBLE);
        }
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);

                intent.putExtra("noteId",position);

                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete Note")
                        .setMessage("Do you want to delete this note")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which = position;
                                notes.remove(which);
                                arrayAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();

                                HashSet<String> set = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                            }
                        })
                        .setNegativeButton("No",null).show();
                return true;
            }
        });
    }

    //MENU CODE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.addNote){

            Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Enter your note and press back when done :)",Toast.LENGTH_LONG).show();

            return true;
        }

        return false;
    }
}
