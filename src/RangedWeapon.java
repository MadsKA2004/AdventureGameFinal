public class RangedWeapon extends Weapon {
    private int ammunition;
    private int damage;

    public RangedWeapon(String shortName, String longName, int ammunition, int damage) {
        super(shortName, longName);
        this.ammunition = ammunition;
        this.damage = damage;

    }

    @Override
    public boolean canUse() {
        return ammunition > 0;
    }

    @Override
    public int use() {
        if (!canUse()) {
            return 0; //Ingen ammunition = 0 skade
        }
        ammunition--; //træk et skud fra
        return damage; //returnerer skaden, der gives
    }

    //Getter-metode
    public int getAmmunition(){
        return ammunition;
    }

    @Override
    public String attack() {
        if (!canUse()){
            return "Click! Your " + getShortName() + " is out of ammo.";
        }
        use(); //Affyrer våbnet (ammunition falder med 1)
        return "You fire the " + getShortName() + " into the empty air. " + getAmmunition() + " shots left.";
    }
}

