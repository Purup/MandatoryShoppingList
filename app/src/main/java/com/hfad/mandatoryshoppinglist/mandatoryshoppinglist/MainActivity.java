package com.hfad.mandatoryshoppinglist.mandatoryshoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "shoppinglist";
    ArrayAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<Product>();

    public ArrayAdapter getMyAdapter()
    {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.i(TAG, "onCreate");
        Button addButton = (Button) findViewById(R.id.addButton);
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        Button clearList = (Button)findViewById(R.id.clearListButton);
        final EditText editText = (EditText)findViewById(R.id.editText);
        final EditText qtyText = (EditText)findViewById(R.id.editText2);



        if (savedInstanceState!=null)
        {
            bag = savedInstanceState.getParcelableArrayList("savedArray");
            //we need to set the text field
            //try to comment the above line out and
            //see the effect after orientation change (after saving some name)
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int test = Integer.valueOf(qtyText.getText().toString()).intValue();
                bag.add(new Product(editText.getText().toString(), test));
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });





        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview
        adapter =  new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_checked,bag );

        //setting the adapter on the listview
        listView.setAdapter(adapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItem = listView.getCheckedItemPosition();


                bag.remove(selectedItem);
                getMyAdapter().notifyDataSetChanged();
            }
        });




    }
    public void clearList(View v) {
        //showing our dialog.
        DialogClearList dialog = new DialogClearList() {
            @Override
            protected void positiveClick() {
                //Here we override the methods and can now
                //do something
                bag.clear();
                getMyAdapter().notifyDataSetChanged();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "List cleared", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            protected void negativeClick() {
                //Here we override the method and can now do something
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No changes made", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "MyFragment");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Test");
        Log.i(TAG, "onStart");
    }

    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    //This method is called before our activity is destroyed
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
		/* Here we put code now to save the state */
        outState.putParcelableArrayList("savedArray", bag);

    }
    //this is called when our activity is recreated, but
    //AFTER our onCreate method has been called
    //EXTREMELY IMPORTANT DETAIL
    //This is an alternative place to restore the data
    //This can also be done in the onCreate method like in the book.
    protected void onRestoreInstanceState(Bundle savedState) {
        //MOST UI elements will automatically store the information
        //if we call the super.onRestoreInstaceState
        //but other data will be lost.
        super.onRestoreInstanceState(savedState);
        Log.i(TAG, "k");
		/*Here we restore any state */
        ListView lV = (ListView) findViewById(R.id.list);
        //TextView savedName = (TextView) findViewById(R.id.name);
        //in the line below, notice key value matches the key from onSaved
        //this is of course EXTREMELY IMPORTANT
        this.bag = savedState.getParcelableArrayList("savedArray");

        //since this method is called AFTER onCreate
        //we need to set the text field
        //try to comment the line out and
        //see the effect after orientation change (after saving some name)
        //savedName.setText("Saved Name:"+name);

    }
}
