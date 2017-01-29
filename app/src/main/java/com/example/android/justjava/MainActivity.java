/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;
    //String name = "inoxx the Amateur Dev";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        //gets customer name from the EditText
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        String name = nameEditText.getText().toString();
        // exits early displaying a warning Toast saying that name cannot be felt blank.
        if (name.matches("")) {
            Toast.makeText(this, getString(R.string.blank_name_toast), Toast.LENGTH_SHORT).show();
            return;
        }

        // prints username into the logs
        Log.d("MainActivity", "name: " + name);

        // grabs  the CheckBox by id.
        CheckBox addWhippedCream = (CheckBox) findViewById(R.id.add_whipped_cream);

        // Checks the boolean value of the Checkbox and stores it.
        boolean whippedCream =  addWhippedCream.isChecked();

        // grabs  the CheckBox by id.
        CheckBox addChocolate = (CheckBox) findViewById(R.id.add_chocolate);

        // Checks the boolean value of the Checkbox and stores it.
        boolean chocolate = addChocolate.isChecked();

        // Calls the calculatePrice() method passing whippedCream and chocolate as parameters to it.
        int price = calculatePrice(whippedCream, chocolate);

        // calls the createOrderSummary method which returns a string to be displayed.
        // pass the price, whippedCream and Chocolate input variables to it, get priceMessage in return.
        String priceMessage = createOrderSummary(price, whippedCream, chocolate, name);

        // displays the order summary string
        // displayMessage(priceMessage);

        Context context = getApplicationContext();
        CharSequence text = getString(R.string.creating_email);
        int duration = Toast.LENGTH_SHORT;

        Toast orderSent = Toast.makeText(context, text, duration);
        orderSent.show();

        // this intent sends the name & order summary to  an email app.
        Intent createEmail = new Intent(Intent.ACTION_SENDTO);
        String recipientAddress = "inoxx.developer@gmail.com";
        // mailto is parsed as an email-only identifier
        createEmail.setData(Uri.parse("mailto:" + recipientAddress));
        // set MIME type of message, currently not implemented, required when using the alternative recipient box population method below
        //createEmail.setType("text/plain");
        // alternative way of populating the recipient address, currently not implemented
        //createEmail.putExtra(android.content.Intent.EXTRA_EMAIL, recipientAddress);
        createEmail.putExtra(createEmail.EXTRA_SUBJECT, getString(R.string.email_subject_line, name));
        createEmail.putExtra(createEmail.EXTRA_TEXT, priceMessage);
        // check if there is an app on your system that can be targeted by intent of this type
        if (createEmail.resolveActivity(getPackageManager()) != null) {
            // chooser wrapper for the app selection intent, allows to add text describing the required action: e.g., "Send Email
            startActivity(Intent.createChooser(createEmail, "Send Email"));
        }
        // log a message when order is submitted
        Log.d("MainActivity", "submitOrder: Order Submitted");
    }

    /**
     * This method is called when the `+` button is clicked.
     */
    public void increment(View view) {
        if (quantity < 100) {
            quantity = quantity + 1;
            Log.d("MainActivity", "increment: + 1; current: " + quantity);
        } else {
            // alternative implementation of the Toast w/o additional variables
            Toast.makeText(this, getString(R.string.quantity_max_limit), Toast.LENGTH_SHORT).show();
            // logs error message
            Log.e("MainActivity", "increment: cannot exceed 100 drinks per order.");

        }
        displayQuantity(quantity);

        // prints quantity increase event message and current quantity value to the log
        //Log.d("MainActivity", "increment: +1, current: " + quantity);
    }

    /**
     * This method is called when the `-` button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.quantity_min_limit);
            int duration = Toast.LENGTH_SHORT;

            Toast noticeMin = Toast.makeText(context, text, duration);
            noticeMin.show();

            Log.e("MainActivity", "decrement: quantity cannot be less than 1!");
            // exit method early if condition is met.
            return;
        }

        quantity = quantity - 1;
        Log.d("MainActivity", "decrement:  - 1; current: " + quantity);

        displayQuantity(quantity);

        // prints quantity decrease event message and current quantity value to the log.
        //Log.d("MainActivity", "increment: -1, current: " + quantity);
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private int calculatePrice(boolean whippedCream, boolean chocolate) {
        int pricePerDrink = 5;
        // add $1 for whipped cream to the price of the drink
        if (whippedCream) {
            pricePerDrink += 1;
        }
        // add $2 for chocolate to the price of the drink
        if (chocolate) {
            pricePerDrink += 2;
        }
        return quantity * pricePerDrink;

    }

    /**
     * Currently, this function is implemented for logging only.
     * All the logic related to the order summary is done within the submitOrder function.
     * @param view - in this case, it is a CheckBox
     */
    public void boxIsChecked(View view) {
        CheckBox addWhippedCream = (CheckBox) findViewById(R.id.add_whipped_cream);
        boolean checked = addWhippedCream.isChecked();

        // prints the status of the Whipped Cream CheckBox to the log
        Log.d("MainActivity", "WhippedCream isChecked: " + checked);
    }

    /**
     * Currently, this function is implemented for logging only.
     * All the logic related to the order summary is done within the submitOrder function.
     * @param view - in this case, it is a CheckBox
     */
    public void boxChocoIsChecked(View view) {
        CheckBox addChocolate = (CheckBox) findViewById(R.id.add_chocolate);
        boolean checkedChoco = addChocolate.isChecked();

        // prints the status of the Chocolate CheckBox to the log
        Log.d("MainActivity", "Chocolate isChecked: " + checkedChoco);
    }

    /**
    * This method
     * @param topping - boolean representing the choice of a topping.
     * @return  yesOrNo - returns a string answer based on the input bool value: 'yes' for true and 'no' for false.
    * */
    public String answer(boolean topping) {
        String yesOrNo;

        if (topping) {
            yesOrNo = getString(R.string.yes);
        } else {
            yesOrNo =getString(R.string.no);
        }

        return yesOrNo;
    }

    /**
     * This method displays the given text on the screen, not implemented.
     * @param message - message to be displayed in a TextView.
     */
//    private void displayMessage(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view_text_view);
//        orderSummaryTextView.setText(message);
//    }

    /**
     *This method displays the order summary incl. name, qty, price and appends a "Thank You".
     * @param price - total price of all drinks ordered, returned by the calculatePrice() method.
     * @param whippedCream - whether or not the user wants a whipped cream topping.
     * @param chocolate - whether or not the user wants chocolate topping
     * @return - order summary text.
     */
    private String createOrderSummary(int price, boolean whippedCream, boolean chocolate, String name) {

        // Composes the Order Summary
        String priceMessage = getString(R.string.field_name, name);
        priceMessage += "\n" + getString(R.string.field_whipped_cream, answer(whippedCream));
        priceMessage += "\n" + getString(R.string.field_chocolate, answer(chocolate));
        priceMessage += "\n" + getString(R.string.field_quantity, quantity);
        priceMessage += "\n" + getString(R.string.field_total, price);
            NumberFormat.getCurrencyInstance().format(price);
        priceMessage += "\n" + getString(R.string.thank_you);

        // prints the submit order to the log
        Log.d("MainActivity", "createOrderSummary:\n" + priceMessage);

        return priceMessage;
    }
}

