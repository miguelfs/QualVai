package si.uff.qualvai.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import si.uff.qualvai.R;

public class SelectDateActivity extends AppCompatActivity {
    private static final String FIREBASE_URL = "https://qualvaiappuffsi.firebaseio.com/";

    private AuthData mAuthData = LoginActivity.getPublicAuthData();
    public final static String UID_MESSAGE = "si.uff.qualvai.UID_MESSAGE";

    private Button mLogoutButton;
    private Spinner mSpinner;
    private TextView mAvailableGames;
    private String[][] datesArray;
    private GridLayout mGridLayout;
    private TextView mWelcomeUserTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        setupToolbar();

        Firebase.setAndroidContext(this);

        datesArray = initializeDatesArray(getResources());
        mWelcomeUserTextView = (TextView) findViewById(R.id.welcomeUserTextView);
        mLogoutButton = (Button) findViewById(R.id.logoutButton);
        mSpinner = (Spinner) findViewById(R.id.datePickerSpinner);
        mAvailableGames = (TextView) findViewById(R.id.availableGamesTextView);
        mGridLayout = (GridLayout) findViewById(R.id.availableGamesGridLayout);

        String string = "Bem vindo, " + mAuthData.getProviderData().get("email").toString() + "!";
        mWelcomeUserTextView.setText(string);
        mGridLayout.setRowCount(10);
        mGridLayout.setColumnCount(1);


            mAvailableGames.setVisibility(View.VISIBLE);

// Create an ArrayAdapter using the string array and a default spinner layout

        setDateSpinnerData(mSpinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {


                    mGridLayout.removeAllViews();

                        if (position > 5) {
                            mAvailableGames.setText(R.string.notAvailableGames);
                        } else {
                            mAvailableGames.setText(R.string.availableGames);
                            for (int i = 0; i < datesArray[position].length; i++) {
                                final Button button = new Button(SelectDateActivity.this);
                                button.setPadding(0, 32, 0, 0);
                                button.setBackgroundColor(0);
                                button.setTextColor(getResources().getColor(R.color.colorAccent));
                                button.setText(datesArray[position][i]);
                                final int finalI = i;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SelectDateActivity.this, SelectPlaceActivity.class);
                                        intent.putExtra(SelectPlaceActivity.POSITION_DAY_ARRAY, position);
                                        intent.putExtra(SelectPlaceActivity.ID_SPORT_ARRAY, finalI);
                                        startActivity(intent);
                                    }
                                });
                                mGridLayout.addView(button, i);
                            }
                        }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDateActivity.this, LoginActivity.class);
                intent.putExtra(SelectDateActivity.UID_MESSAGE, true);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setDateSpinnerData(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dates_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private String[][] initializeDatesArray(Resources res) {
        TypedArray ta = res.obtainTypedArray(R.array.dates_references_array);
        int n = ta.length();
        String[][] array = new String[n][];
        for (int i = 0; i < n; ++i) {
            int id = ta.getResourceId(i, 0);
            if (id > 0) {
                array[i] = res.getStringArray(id);
            } else {
                // something wrong with the XML
            }
        }
        ta.recycle(); // Important!
        return array;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}


