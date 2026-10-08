public class MeleeWeapon extends Weapon {
    private int damage;

    public MeleeWeapon(String shortName, String longName, int damage){
        super(shortName, longName);
        this.damage = damage;

    }


    @Override
    public boolean canUse() {
        return true; //kan altid bruges
    }

    @Override
    public int use() {
        return damage; //returnerer den skade, våbnet gør
    }

    @Override
    public String attack(){
        return "You swing the " + getShortName() +  " at the empty air";
    }
}
