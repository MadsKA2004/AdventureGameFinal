import java.util.ArrayList;

public class Room {
    private String name;
    private String description;
    private Room north;
    private Room east;
    private Room south;
    private Room west;
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();



    public Room(String name, String description){
        this.name = name;
        this.description = description;
        //Javas standardværdi for objekter er automatisk 'null',
        //så alle fire retninger starter med at være tomme.
    }



    //addEnemy, removeEnemy, findEnemy og getFirstEnemy metoder

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy){
        enemies.remove(enemy);
    }

    //Bruges til at finde en specifik fjende, når spilleren skriver f.eks. 'attack troll'
    public Enemy findEnemies(String enemyName){
        for (Enemy enemy : enemies){
            if (enemy.getName().equalsIgnoreCase(enemyName)){
                return enemy;
            }
        }
        return null; //hvis fjenden ikke findes i rummet
    }

    //Bruges hvis spilleren bare skriver 'attack' uden et navn
    public Enemy getFirstEnemy(String enemyName){
        //Hvis spilleren har skrevet et navn -> søg efter fjenden
        if (enemyName != null && !enemyName.isEmpty()) {
            return findEnemies(enemyName);
        }
        //Hvis intet navn er angivet -> tag den første (eller null, hvis listen er tom)
        return enemies.isEmpty() ? null : enemies.getFirst();
    }

    //Additem, removeitem og finditem metoder

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    public String getName(){
        return name;
    }

    //Søgemetode: Løber listen igennem og matecher det korte navn
    public Item findItems(String itemName){
        for (Item item : items){
            if (item.getShortName().equalsIgnoreCase(itemName)){
                return item;
            }
        }
        return null; //Returnerer null hvis item ikke blev fundet
    }

    public String getDescription(){
        return description;
    }

    //North
    public void setNorth(Room room){
        this.north = room;
    }

    public Room getNorth(){
        return north;
    }

    //South
    public void setSouth(Room room){
        this.south = room;
    }

    public Room getSouth(){
        return south;
    }

    //East
    public void setEast(Room room){
        this.east = room;
    }

    public Room getEast(){
        return east;
    }

    //West
    public void setWest(Room room){
        this.west = room;
    }

    public Room getWest(){
        return west;
    }

}
