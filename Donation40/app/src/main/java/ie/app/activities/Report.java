package ie.app.activities;

import android.os.Bundle;
import android.widget.ListView;

import ie.app.R;

public class Report extends Base {
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        listView = (ListView) findViewById(R.id.reportList);
        DonationAdapter adapter = new DonationAdapter(this, app.dbManager.getAll());
        listView.setAdapter(adapter);
    }
}