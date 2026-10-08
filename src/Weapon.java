abstract class Weapon extends Item {

    public Weapon(String shortName, String longName){
        super(shortName, longName);
    }

    @Override
    public abstract int use();

    @Override
    public abstract boolean canUse();

    public abstract String attack();
}
