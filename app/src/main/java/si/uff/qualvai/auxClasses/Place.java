package si.uff.qualvai.auxClasses;


public class Place {

    private String mName;
    private String mBairro;
    private String mTypeStore;
    private String mFoodVariant;
    private String mOutdoorVariant;
    private Double mLat;
    private Double mLon;
    private int mIdImage1;
    private int mIdImage2;

    public Place(String name, String bairro, String typeStore, String foodVariant, String outdoorVariant, Double lat, Double lon, int idImage1, int idImage2) {
        mName = name;
        mBairro = bairro;
        mTypeStore = typeStore;
        mFoodVariant = foodVariant;
        mOutdoorVariant = outdoorVariant;
        mLat = lat;
        mLon = lon;
        mIdImage1 = idImage1;
        mIdImage2 = idImage2;
    }

    public boolean customEquals(String bairro, String typeStore, String foodVariant, String outdoorVariant) {

        if (!mOutdoorVariant.equals(outdoorVariant))
            return false;
        if (!mBairro.equals(bairro))
            return false;
        return !(mTypeStore != null ? !mTypeStore.equals(typeStore) : typeStore != null) && mFoodVariant.equals(foodVariant);
    }



    public boolean customEquals2(String bairro, String typeStore, String foodVariant, String outdoorVariant) {

        return mOutdoorVariant.equals(outdoorVariant) && mBairro.equals(bairro) &&
                mTypeStore.equals(typeStore) && mFoodVariant.equals(foodVariant);
    }

    public int getIdImage2() {
        return mIdImage2;
    }

    public int getIdImage1() {
        return mIdImage1;
    }

    public Double getLat() {
        return mLat;
    }

    public Double getLon() {
        return mLon;
    }

    public String getName() {
        return mName;
    }
}
