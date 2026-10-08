import java.util.ArrayList;

public class Player {
    private Room currentroom;
    private ArrayList<Item> inventory = new ArrayList<>();
    private int health = 100;
    private Weapon equippedWeapon;


    public Player(Room startRoom) {
        this.currentroom = startRoom;
        this.equippedWeapon = null;
    }

    //health getter og setter metode

    public int getHealth() {
        return health;
    }

    public EatResult eat(String itemName) {
        Item item = findItemsInInventory(itemName);

        //Hvis det ikke ligger i inventory, så led i det nuværende rum
        if (item == null) {
            return EatResult.NOT_FOUND;
        }
        //Udfald 1: Tingen slet ikke findes som Food-objektet
        if (!(item instanceof Food foodToEat)) {
            return EatResult.NOT_FOUND;
        }
        //Udfald 2 og 3: Tingen findes! nu tjekker vi, om det food
        this.health += foodToEat.getHealthPoints();
        this.inventory.remove(item);
        this.currentroom.removeItem(item);
        return EatResult.EATEN;
    }

    //Equip metoden
    public EquipResult equip(String itemName) {
        Item item = findItemsInInventory(itemName); //finder itemet i inventory

        //Våbnet findes ikke
        if (item == null) {
            return EquipResult.NOT_FOUND;
        }

        //Tjek om det ikke er et våben (og cast samtidig)
        if (!(item instanceof Weapon weapon)) {
            return EquipResult.NO_WEAPON;
        }

        //Læg det gamle våben væk (hvis der er et)
        if (this.equippedWeapon != null){
            this.inventory.add(this.equippedWeapon);
        }

        this.equippedWeapon = weapon;
        this.inventory.remove(item);
        return EquipResult.EQUIPPED;
    }

    public String getEquippedWeaponName(){
        if (this.equippedWeapon != null){
            return this.equippedWeapon.getShortName();
        }
        return null; //returnerer null (ingenting) hvis man ikke har et våben på sig
    }

    public String attack(String targetEnemyName) {
        //Finder fjenden først
        Enemy targetEnemy = this.currentroom.getFirstEnemy(targetEnemyName);

        //Hent enum status
        AttackStatus status = AttackStatus.getStatus(this.equippedWeapon, targetEnemy, targetEnemyName);

        //Hvis der er en fejl/specialtilfælde bliver det håndteret med en Switch
        if (status != null) {
            switch (status) {
                case NO_WEAPON:
                    return "You do not have a weapon equipped!";
                case NO_AMMO:
                case ATTACK_EMPTY_AIR:
                    return this.equippedWeapon.attack(); // attack() tjekker selv canUse() metoden
                case ENEMY_NOT_FOUND:
                    return "There is no enemy named '" + targetEnemyName + "' here.";
            }
        }
        //SELVE KAMPEN (kører kun hvis status er null)
        int damageDone = this.equippedWeapon.use();
        boolean isEnemyAlive = targetEnemy.hit(damageDone);

        String battleReport = "\nYou attack the " + targetEnemy.getName() + " with your " + this.equippedWeapon.getShortName() + ".\n";

        if (!isEnemyAlive) {
            battleReport += "The " + targetEnemy.getName() + " dies, dropping its " + targetEnemy.getWeapon().getShortName() + ".";
            return battleReport;
        }

        battleReport += "You strike the " + targetEnemy.getName() + " and inflicts " + damageDone + " damage on it.";
        battleReport += targetEnemy.attack();

        //Spilleren mister health baseret på fjendens våben
        this.health -= targetEnemy.getWeaponDamage();
        //Tjek om spilleren dør af modangrebet
        if (this.health <= 0) {
            battleReport += "\nGame over! You have been slain by the " + targetEnemy.getName() + ".";
            IO.println(battleReport);
            System.exit(0); //afslutter programmet øjeblikkeligt
        }
        return battleReport;
    }


    //Item-metoder og inventory metoder

    //Søgemetode i spillerens inventory
    public Item findItemsInInventory(String itemName){
        for (Item item : inventory){
            if (item.getShortName().equalsIgnoreCase(itemName) || item.getLongName().toLowerCase().contains(itemName.toLowerCase())) {
                return item;
            }
        }
        return null; //Itemet blev ikke fundet i inventory
    }


    //Denne søgemetode inkluderer currentRoom med itemsøgning
    public Item findItemsAnywhere(String itemName){
        for (Item item : inventory){
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return currentroom.findItems(itemName);
    }

    public Item takeItem(String itemName){
        Item item = currentroom.findItems(itemName);
        if (item != null){
            currentroom.removeItem(item); //fjernet fra rummet / currentroom
            inventory.add(item); //blev tilføjet til spillerens inventory
        }
        return item; //returnerer objektet eller null
    }


    public Item dropItem(String itemName){
        //Hvis spileren dropper sit udrustede våben, bliver det lagt inventory og dermed i findItemsInInventory
        if (isEquippedWeapon(itemName)) {
            this.inventory.add(this.equippedWeapon);
            this.equippedWeapon = null;
        }

        Item item = findItemsInInventory(itemName);
        if (item != null){
            inventory.remove(item);
            currentroom.addItem(item);
        }
        return item; //returnerer objektet hvis det blev fundet ellers null
    }

    public boolean isEquippedWeapon(String itemName){
        if (this.equippedWeapon == null){
            return false;
        }
        boolean matchShort = this.equippedWeapon.getShortName().equalsIgnoreCase(itemName);
        boolean matchLong = this.equippedWeapon.getLongName().toLowerCase().contains(itemName.toLowerCase());
        return matchShort || matchLong;
    }


    public ArrayList<Item> getInventory(){
        return inventory;
    }

    //Room og move
    public Room getCurrentroom(){
        return currentroom;
    }

    public String getCurrentRoomName(){
        return currentroom.getName();
    }

    public String getCurrentRoomDescription(){
        return currentroom.getDescription();
    }

    //Håndterer bevægelse/korsør. Returnerer true, hvis det lykkedes, og false hvis det er en mur (null)
    public boolean move(String direction){
        Room nextRoom = null;

        //Modtager kun de fulde retninger fra UI/Controller
        switch (direction){
            case "north", "n" -> nextRoom = currentroom.getNorth();
            case "south", "s" -> nextRoom = currentroom.getSouth();
            case "east", "e" -> nextRoom = currentroom.getEast();
            case "west", "w" -> nextRoom = currentroom.getWest();
        }
        if (nextRoom != null){
            currentroom = nextRoom; //Husk hvor vi står lige nu
            return true; //flytning lykkedes
        }
        return false; //Der er ikke noget at gå tilbage til endnu /blindgyde
    }
}
