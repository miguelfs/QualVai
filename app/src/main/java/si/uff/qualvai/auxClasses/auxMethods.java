package si.uff.qualvai.auxClasses;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;

import si.uff.qualvai.R;


public class auxMethods {

    public static String[][] initializeDatesArray(Resources res) {
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

    public static ArrayList generatePlaces(){
        ArrayList<Place> placeVector = new ArrayList<Place>();
        placeVector.add(new Place("Fast Chicken", "Grajaú", "Bares", "Petiscos", "Área aberta", -22.920653, -43.260051, R.drawable.fast1, R.drawable.fast2));
        placeVector.add(new Place("Tonoshii Sushi Bar Grajaú", "Grajaú", "Restaurantes", "Japonesa", "Área fechada", -22.923492, -43.255670, R.drawable.tonoshii1, R.drawable.tonoshii2));
        placeVector.add(new Place("Bar do Adão", "Grajaú", "Bares", "Petiscos", "Área aberta", -22.921554, -43.262335, R.drawable.bardoadao1, R.drawable.bardoadao2));
        placeVector.add(new Place("Dom Manoel Restaurante", "Grajaú", "Restaurantes", "Italiana", "Área fechada", -22.922592, -43.264139, R.drawable.dommanoel1, R.drawable.dommanoel2));
        placeVector.add(new Place("Vestibular do Chopp", "Gragoatá", "Restaurantes", "Italiana", "Área aberta", -22.899845, -43.130535, R.drawable.vestibulardochopp1, R.drawable.vestibulardochopp2));
        placeVector.add(new Place("À Mineira Gourmet", "Gragoatá", "Restaurantes", "Brasileira", "Área fechada", -22.899955, -43.130996, R.drawable.amineira1, R.drawable.amineira2 ));
        placeVector.add(new Place("Bar Tio Coto", "Gragoatá", "Bares", "Petiscos", "Área fechada", -22.899817, -43.130724, R.drawable.bartiocoto1, R.drawable.bartiocoto2 ));
        placeVector.add(new Place("Bar do Laury", "Gragoatá", "Bares", "Petiscos", "Área aberta", -22.899497, -43.129998, R.drawable.bardolaury1, R.drawable.bardolaury2 ));
        placeVector.add(new Place("Bar do Chico's", "Maracanã", "Bares", "Petiscos", "Área aberta", -22.915038, -43.226666, R.drawable.bardochicos1, R.drawable.bardochicos2 ));
        placeVector.add(new Place("Bar do Bode Cheiroso", "Maracanã", "Bares", "Petiscos", "Área aberta", -22.914504, -43.225765, R.drawable.bodecheiroso1, R.drawable.bodecheiroso2 ));
        placeVector.add(new Place("Planeta do Chopp", "Maracanã", "Bares", "Petiscos", "Área aberta", -22.913327, -43.235979, R.drawable.planetadochopp1, R.drawable.planetadochopp2 ));
        placeVector.add(new Place("Bellini Bar", "Maracanã", "Bares", "Petiscos", "Área aberta", -22.916558, -43.229209, R.drawable.bellini1, R.drawable.bellini2 ));

        return placeVector;
    }

    public static int getPlaceIndexByName(String name) {
        ArrayList<Place> arrayPlace = generatePlaces();
        for (int index = 0; index < auxMethods.generatePlaces().size(); index++) {
            if (arrayPlace.get(index).getName().equals(name)) { return index;
            }
        }
        return -1;
    }
}
