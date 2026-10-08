public class Enemy {
    private final String shortName;
    private final String longName;
    private int health;
    private final Weapon weapon;
    private Room currentRoom;

    public Enemy(String shortName, String longName, int health, Weapon weapon, Room enemyRoom){
        this.shortName = shortName;
        this.longName = longName;
        this.health = health;
        this.weapon = weapon;
        this.currentRoom = enemyRoom;
    }



    public int getWeaponDamage(){
        return this.weapon.use(); //da fjenden har uendelig ammunition, gør det ikke noget use() kaldes
    }

    public String getDescription(){
        return longName;
    }

    //Fjenden angriber playeren
    public String attack(){
        int damage = weapon.use(); //bruger våbnet og får damage-værdien
        return "\nThe " + getName() + " attacks you with " + weapon.getLongName() + " and inflicts " + damage + " damage on you.";
    }


    //Fjenden bliver ramt af spilleren
    public boolean hit(int damage) {
        health -= damage;
        if (health <= 0) {
            currentRoom.addItem(weapon);
            currentRoom.removeEnemy(this);
            return false; //false betyder "ikke i live mere"
        }
        return true; //true betyder "stadig i live"
    }


    //Gettere

    public String getName(){
        return shortName;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
