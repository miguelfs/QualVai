package si.uff.qualvai.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import si.uff.qualvai.auxClasses.Place;
import si.uff.qualvai.R;
import si.uff.qualvai.auxClasses.auxMethods;

public class GoingActivity extends AppCompatActivity {
    private AuthData mAuthData = LoginActivity.getPublicAuthData();
    private String mUserEmail = mAuthData.getProviderData().get("email").toString();
    public static final String MARKER_NAME = "si.uff.qualvai.MARKER_NAME";
    public static final String GAME_NAME = "si.uff.qualvai.GAME_NAME";
    private static final String FIREBASE_URL = "https://qualvaiappuffsi.firebaseio.com/";
    int mImageId1;
    int mImageId2;
    boolean isImageId1 = true;


    private String mMarkerName;
    private TextView mPlaceNameTextView;
    private TextView mGameTextView;
    private TextView mPeopleCountTextView;
    private Button mGoButton;
    private Button mHomeButton;
    private  Button mAnotherPlaceButton;
    private ImageButton mImageButtonLeft, mImageButtonRight;
    private ImageView mPlacePhotoImageView;
    private Firebase mFirebase;
    private Firebase mPlaceRef;
    private int mPeopleCount;
    private View.OnClickListener myClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going);


        mPlaceNameTextView = (TextView) findViewById(R.id.placeNameTextView);
        mGameTextView = (TextView) findViewById(R.id.gameTextView);
        mPlacePhotoImageView = (ImageView) findViewById(R.id.placePhotoImageView);
        mPeopleCountTextView = (TextView) findViewById(R.id.peopleCountTextView);
        mGoButton = (Button) findViewById(R.id.goButton);
         mHomeButton = (Button) findViewById(R.id.homeButton);
        mAnotherPlaceButton = (Button) findViewById(R.id.anotherPlaceButton);
        mImageButtonLeft = (ImageButton) findViewById(R.id.imageButtonLeft);
        mImageButtonRight = (ImageButton) findViewById(R.id.imageButtonRight);
        Intent intent = getIntent();
        mMarkerName = intent.getStringExtra(MARKER_NAME);
        mPlaceNameTextView.setText(mMarkerName);
        mGameTextView.setText(intent.getStringExtra(GAME_NAME));

        mImageId1 = intent.getIntExtra(MapsActivity.PHOTO_ID1, 0);
        mImageId2 = intent.getIntExtra(MapsActivity.PHOTO_ID2, 0);
        mPlacePhotoImageView.setImageResource(mImageId1);

        mFirebase = new Firebase(FIREBASE_URL);
        if (mFirebase.child("places").child(mMarkerName) == null ){
            mFirebase.child("places").setValue(mMarkerName);
        }
        mPlaceRef = mFirebase.child("places").child(intent.getStringExtra(MARKER_NAME));
        mPlaceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPeopleCountTextView.setText(dataSnapshot.getChildrenCount() + " pessoas clicaram Eu Vou!");
                mPeopleCount = (int) (long) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> emails = new HashMap<String, Object>();
                        boolean hasAlreadySigned = false;

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getValue().equals(mUserEmail)) {
                                hasAlreadySigned = true;
                                Toast.makeText(GoingActivity.this, "Você já confirmou presença nesse evento", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!hasAlreadySigned) {
                            //                           emails.put("" + dataSnapshot.getChildrenCount(),
                            //                         mAuthData.getProviderData().get("email").toString());
                            mPlaceRef.child("" + dataSnapshot.getChildrenCount()).setValue(mAuthData.getProviderData().get("email").toString());
                            mPeopleCount = mPeopleCount + 1;
                            mPeopleCountTextView.setText("" + mPeopleCount + " pessoas clicaram Eu Vou!");
                            Toast.makeText(GoingActivity.this, "Você acaba de confirmar sua presença nesse evento!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoingActivity.this, SelectDateActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAnotherPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

      myClickListener = new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               changePhoto();
           }
       };

        mImageButtonLeft.setOnClickListener(myClickListener);
        mImageButtonRight.setOnClickListener(myClickListener);

    }
    void changePhoto(){
        if(isImageId1){
            mPlacePhotoImageView.setImageResource(mImageId2);
            isImageId1 = false;
        }
        else{
            mPlacePhotoImageView.setImageResource(mImageId1);
            isImageId1 = true;
        }
    }
}
