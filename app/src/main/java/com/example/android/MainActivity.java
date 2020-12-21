package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    int price = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((CheckBox) findViewById(R.id.whipped_cream_checkbox)).setText("Whipped Cream (" + NumberFormat.getCurrencyInstance().format(1) + " Extra)");
        ((CheckBox) findViewById(R.id.chocolate_checkbox)).setText("Chocolate (" + NumberFormat.getCurrencyInstance().format(2) + " Extra)");
//        ((EditText) findViewById(R.id.name_field)).set();
    }

    public void increaseQuantity(View view) {
        display(++quantity);
    }

    public void decreaseQuantity(View view) {
        display(--quantity);
    }

    public void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_input);
        quantityTextView.setText("" + number);
    }

    public void submitOrder(View view) {
        Context context = getApplicationContext();
        int finalPrice = 0;

        Toast toastMessage = Toast.makeText(context, "", Toast.LENGTH_LONG);
        if (quantity < 0) {
            toastMessage.setText("Quantity cannot be less than 0");
            toastMessage.show();
            return;
        } else if (quantity > 100) {
            toastMessage.setText("Quantity cannot be greater than 100");
            toastMessage.show();
            return;
        }

        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        boolean whippedCream = ((CheckBox) findViewById(R.id.whipped_cream_checkbox)).isChecked();
        boolean chocolate = ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();
        String name = ((EditText) findViewById(R.id.name_field)).getText().toString();


        finalPrice = calculatePrice(quantity, whippedCream, chocolate);
        String orderSummary = createOrderSummary(finalPrice, whippedCream, chocolate, name);


        Uri contactUri = Uri.parse("mailto:");
        editContact(contactUri, "01keshavsinghs.mail@gmail.com", name, orderSummary);
        priceTextView.setText(orderSummary);
    }


    public String createOrderSummary(int _price, boolean whippedCreeamSelected, boolean chocolate, String name) {
        String outputText = getString(R.string.order_summary_name,name);
        outputText += "\nDo you want Whippped Cream? " + (whippedCreeamSelected ? "Yes" : "No");
        outputText += "\nDo you want Chocolate? " + (chocolate ? "Yes" : "No");
        outputText += "\nQuantity: " + quantity;
        outputText += "\nBase Price :"+NumberFormat.getCurrencyInstance().format(this.price)+" per cup";
        outputText += "\nTotal Price: " + NumberFormat.getCurrencyInstance().format(_price);
        outputText = outputText + "\nThank You";
        return outputText;
    }


    public int calculatePrice(int quantity, boolean whippedCream, boolean chocolate) {
        int _price = this.price;

        if (whippedCream) {
            _price += 1;
        }
        if (chocolate) {
            _price += 2;
        }
        return quantity * _price;
    }

    /*
    *
    * Used for Intents
    *
    * */
    public void editContact(Uri contactUri, String email, String name, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(contactUri);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showMap(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=28.6139,77.2090(Treasure)?z=11"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}