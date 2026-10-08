public class Item {
    private final String shortName; //Omdøbt fra name
    private final String longName; //Omdøbt fra description


    public Item(String shortName, String longName){
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getShortName(){
        return shortName;
    }

    public String getLongName(){
        return longName;
    }

    public boolean canUse(){
        return false; //Almindelige items kan ikke bruges til noget automatisk
    }

    public int use(){
        return 0; //Almindelige items gør 0 skade /giver ingen health
    }
}
