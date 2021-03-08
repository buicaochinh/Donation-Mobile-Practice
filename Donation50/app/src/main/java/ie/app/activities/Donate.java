package ie.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import ie.app.R;
import ie.app.api.DonationApi;
import ie.app.models.Donation;

public class Donate extends Base {
    private Button donateButton;
    private RadioGroup paymentMethod;
    private ProgressBar progressBar;
    private NumberPicker amountPicker;
    private EditText amountText;
    private TextView amountTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        donateButton = (Button) findViewById(R.id.donateButton);
        paymentMethod = (RadioGroup) findViewById(R.id.paymentMethod);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        amountPicker = (NumberPicker) findViewById(R.id.amountPicker);
        amountText = (EditText) findViewById(R.id.paymentAmount);
        amountTotal = (TextView) findViewById(R.id.totalSoFar);
        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);
        progressBar.setMax(10000);
        amountTotal.setText("$0");
    }
    public void donateButtonPressed (View view)
    {
        String method = paymentMethod.getCheckedRadioButtonId() ==
                R.id.PayPal ? "PayPal" : "Direct";
        int donatedAmount = amountPicker.getValue();
        if (donatedAmount == 0)
        {
            String text = amountText.getText().toString();
            if (!text.equals(""))
                donatedAmount = Integer.parseInt(text);
        }
        if (donatedAmount > 0)
        {
            app.newDonation(new Donation(donatedAmount, method, 0));
            progressBar.setProgress(app.totalDonated);
            String totalDonatedStr = "$" + app.totalDonated;
            amountTotal.setText(totalDonatedStr);
        }
    }
    @Override
    public void reset(MenuItem item)
    {
        new ResetTask(this).execute("/donations");
        app.totalDonated = 0;
        amountTotal.setText("$" + app.totalDonated);
    }
    @Override
    public void onResume() {
        super.onResume();
        new GetAllTask(this).execute("/donations");
    }

    private class GetAllTask extends AsyncTask<String, Void, List<Donation>> {
        protected ProgressDialog dialog;
        protected Context context;
        public GetAllTask(Context context)
        {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving Donations List");
            this.dialog.show();
        }
        @Override
        protected List<Donation> doInBackground(String... params) {
            try {
                Log.v("donate", "Donation App Getting All Donations");
                return (List<Donation>) DonationApi.getAll((String) params[0]);
            }
            catch (Exception e) {
                Log.v("donate", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Donation> result) {
            super.onPostExecute(result);
            //use result to calculate the totalDonated amount here
            progressBar.setProgress(app.totalDonated);
            amountTotal.setText("$" + app.totalDonated);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private class InsertTask extends AsyncTask<Object, Void, String> {
        protected ProgressDialog dialog;
        protected Context context;
        public InsertTask(Context context)
        {
            this.context = context; }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Saving Donation....");
            this.dialog.show();
        }
        @Override
        protected String doInBackground(Object... params) {
            String res = null;
            try {
                Log.v("donate", "Donation App Inserting");
            }
            catch(Exception e)
            {
                Log.v("donate","ERROR : " + e);
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private class ResetTask extends AsyncTask<Object, Void, String> {
        protected ProgressDialog dialog;
        protected Context context;
        public ResetTask(Context context)
        {
            this.context = context;
        } @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Deleting Donations....");
            this.dialog.show();
        }
        @Override
        protected String doInBackground(Object... params) {
            String res = null;
            try {
                res = DonationApi.deleteAll((String)params[0]);
            }
            catch(Exception e)
            {
                Log.v("donate"," RESET ERROR : " + e);
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            app.totalDonated = 0;
            progressBar.setProgress(app.totalDonated);
            amountTotal.setText("$" + app.totalDonated);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }
}


