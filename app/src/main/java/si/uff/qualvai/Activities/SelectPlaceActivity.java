package si.uff.qualvai.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import si.uff.qualvai.auxClasses.Place;
import si.uff.qualvai.R;
import si.uff.qualvai.auxClasses.auxMethods;

public class SelectPlaceActivity extends AppCompatActivity {
    public final static String POSITION_DAY_ARRAY = "si.uff.qualvai.POSITION_DAY_ARRAY";
    public final static String ID_SPORT_ARRAY = "si.uff.qualvai.ID_SPORT_ARRAY";
    private static final String FIREBASE_URL = "https://qualvaiappuffsi.firebaseio.com/";
    private TextView mPlaceTextView;
    private Spinner mPlacesSpinner;
    private Spinner mFoodVariantSpinner;
    private Spinner mOutdoorSpinner;
    private Button mSearchButton;
    private Spinner mBairroSpinner;
    private ArrayList<Place> mArrayPlaces = auxMethods.generatePlaces();
    int mImageId1;
    int mImageId2;

    String stringMatrix[][];
    int mId;
    int mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);
        stringMatrix = auxMethods.initializeDatesArray(getResources());

        mSearchButton = (Button) findViewById(R.id.searchButton);
        mPlacesSpinner = (Spinner) findViewById(R.id.placeDropdownSpinner);
        mFoodVariantSpinner = (Spinner) findViewById(R.id.foodVariantDropdownSpinner);
        mOutdoorSpinner = (Spinner) findViewById(R.id.outdoorDropdownSpinner);
        mBairroSpinner = (Spinner) findViewById(R.id.locationSpinner);
        loadActivitySpinner(mPlacesSpinner, mFoodVariantSpinner, mOutdoorSpinner, mBairroSpinner, R.array.places_array, R.array.food_variant_array, R.array.outdoor_array, R.array.bairro_array);



        Intent intent = getIntent();
        mId = intent.getIntExtra(POSITION_DAY_ARRAY, 0);
        mName = intent.getIntExtra(ID_SPORT_ARRAY, 0);

        stringMatrix = auxMethods.initializeDatesArray(getResources());
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean triggerToast = true;
                int foodVId = mFoodVariantSpinner.getSelectedItemPosition();
                int OutdoorVId = mOutdoorSpinner.getSelectedItemPosition();
                int BairroVId = mBairroSpinner.getSelectedItemPosition();
                int PlaceVId = mPlacesSpinner.getSelectedItemPosition();
                String[] foodVArray = getResources().getStringArray(R.array.food_variant_array);
                String[] outdoorVArray = getResources().getStringArray(R.array.outdoor_array);
                String[] bairroVArray = getResources().getStringArray(R.array.bairro_array);
                String[] placeVArray = getResources().getStringArray(R.array.places_array);

                for(int index = 0; index < auxMethods.generatePlaces().size(); index++){
                    if (mArrayPlaces.get(index).customEquals(bairroVArray[BairroVId], placeVArray[PlaceVId], foodVArray[foodVId], outdoorVArray[OutdoorVId])) {
                        triggerToast = false;
                        mImageId1 = mArrayPlaces.get(index).getIdImage1();
                        mImageId2 = mArrayPlaces.get(index).getIdImage2();
                        Intent mapsIntent = new Intent(SelectPlaceActivity.this, MapsActivity.class);
                        mapsIntent.putExtra(MapsActivity.PLACE_VARIANT_ID, PlaceVId);
                        mapsIntent.putExtra(MapsActivity.FOOD_VARIANT_ID, foodVId);
                        mapsIntent.putExtra(MapsActivity.OUTDOOR_INDOOR_ID, OutdoorVId);
                        mapsIntent.putExtra(MapsActivity.BAIRRO_VARIANT_ID, BairroVId);
                        mapsIntent.putExtra(MapsActivity.GAME_NAME, stringMatrix[mId][mName]);
                        mapsIntent.putExtra(MapsActivity.PHOTO_ID1,mImageId1);
                        mapsIntent.putExtra(MapsActivity.PHOTO_ID2, mImageId2);
                        startActivity(mapsIntent);
                    }
                    } if (triggerToast){
                                            Toast.makeText(SelectPlaceActivity.this, "Não há estabelecimentos cadastrados com as características acima.", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private String[][] initializeDatesArray(Resources res) {
        TypedArray ta = res.obtainTypedArray(R.array.dates_references_array);
        int n = ta.length();
        String[][] array = new String[n][];
        for (int i = 0; i < n; ++i) {
            int id = ta.getResourceId(i, 0);
            if (id > 0) {
                array[i] = res.getStringArray(id);
            }
        }
        ta.recycle(); // Important!
        return array;
    }

    private void setDateSpinnerData(Spinner spinner, int arrayId ) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void loadActivitySpinner(Spinner spinner1, Spinner spinner2, Spinner spinner3, Spinner spinner4,  int arrayId1, int arrayId2,  int arrayId3, int arrayId4 ){
        setDateSpinnerData(spinner1, arrayId1);
        setDateSpinnerData(spinner2, arrayId2);
        setDateSpinnerData(spinner3, arrayId3);
        setDateSpinnerData(spinner4, arrayId4);
    }

}
