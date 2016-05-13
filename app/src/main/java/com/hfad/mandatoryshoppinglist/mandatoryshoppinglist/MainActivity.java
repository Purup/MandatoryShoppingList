package com.hfad.mandatoryshoppinglist.mandatoryshoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "shoppinglist";
    ListView listView;
    int lastDeletedPosition;
    Product lastDeletedProduct;
    Firebase mRef;
    FirebaseListAdapter<Product> firebaseAdapter;

    EditText editText;
    EditText qtyText;
    EditText priceText;
    TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRef = new Firebase("https://shoppinglistmobile.firebaseio.com/lists");
        firebaseAdapter = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.simple_list_item_checked, mRef) {
            @Override
            protected void populateView(View view, Product product, int i) {
            TextView tView = (TextView)view.findViewById(android.R.id.text1);
                tView.setText(product.toString());
            }
        };

        Log.i(TAG, "onCreate");
        Button addButton = (Button) findViewById(R.id.addButton);
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        Button calculatePrice = (Button)findViewById(R.id.calcPrice);
        editText = (EditText)findViewById(R.id.editText);
        qtyText = (EditText)findViewById(R.id.editText2);
        priceText = (EditText)findViewById(R.id.price);
        totalPrice = (TextView)findViewById(R.id.totalPrice);

        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the listview

        //setting the adapter on the listview
        listView.setAdapter(firebaseAdapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItem = listView.getCheckedItemPosition();

                if(selectedItem >= 0)
                {
                    lastDeletedPosition = selectedItem;
                    lastDeletedProduct = firebaseAdapter.getItem(selectedItem);

                    firebaseAdapter.getRef(selectedItem).setValue(null);

                    Snackbar snackBar = Snackbar.make(listView, "Item Deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mRef.push().setValue(lastDeletedProduct);
                                    Snackbar snackbar = Snackbar.make(listView, "Item restored!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            });

                    snackBar.show();
                }
            }
        });

        calculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrice.setText("Total Price: "+String.valueOf(calculateTotalprice())+"kr");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean addProduct() {
        String itemName = editText.getText().toString();
        String qty = qtyText.getText().toString();
        String price = priceText.getText().toString();

        if (qty.isEmpty() || qty.equals("0") || itemName.equals("") || price.isEmpty())
            return false;

        mRef.push().setValue(new Product(itemName, Integer.parseInt(qty), Integer.parseInt(price)));

        editText.setText("");
        qtyText.setText("");
        priceText.setText("");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Toast.makeText(this, "Application icon clicked!",
                        Toast.LENGTH_SHORT).show();
                return true; //return true, means we have handled the event
            case R.id.clear_list:
                    showDialog();
                break;
            case R.id.share:
                share();
                break;
            case R.id.settings:
                setPreferences();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item); //we did not handle the event
    }

    public void showDialog() {
        //showing our dialog.
        DialogClearList dialog = new DialogClearList() {
            @Override
            protected void positiveClick() {

                for (int i = 0; i < firebaseAdapter.getCount(); i++) {
                    firebaseAdapter.getRef(i).setValue(null);
                }
            }
            @Override
            protected void negativeClick() {
            }
        };
        dialog.show(getFragmentManager(), "MyFragment");
    }

    public String convertListToString()
    {
        String result = "";

        for(int i = 0; i<firebaseAdapter.getCount(); i++)
        {
            Product p = (Product)firebaseAdapter.getItem(i);
            result += p.toString() + ", ";
        }
        return result;
    }

    public void share()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, convertListToString());
        startActivity(intent);
    }

    public int calculateTotalprice()
    {
        int result = 0;
        for(int i = 0; i<firebaseAdapter.getCount(); i++)
        {
            Product p = (Product)firebaseAdapter.getItem(i);
            result += (p.getPrice() * p.getQuantity());
        }
        return result;
    }

    //This will be called when other activities in our application
    //are finished.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1) //exited our preference screen
        {
            getPreferences();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPreferences() {
        //Here we create a new activity and we instruct the
        //Android system to start it
        Intent intent = new Intent(this, Settings.class);
        //startActivity(intent); //this we can use if we DONT CARE ABOUT RESULT

        //we can use this, if we need to know when the user exists our preference screens
        startActivityForResult(intent, 1);
    }

    public void getPreferences() {

        //We read the shared preferences from the
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String name = prefs.getString("name", "");

        Toast.makeText(
                this,
                "Name: " + name + "\nEmail: " + email, Toast.LENGTH_SHORT).show();
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

}
