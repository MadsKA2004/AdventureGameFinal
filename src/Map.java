public class Map {
    private Room startRoom;

    public Map(){
        createMap();
    }

    public void createMap(){
        //1. Opret rummene
        Room room1 = new Room("Room 1", "A room with no distinct features, except two doors.");
        Room room2 = new Room("Room 2", "Water drips from the ceiling somewhere in the dark.");
        Room room3 = new Room("Room 3", "A dusty library filled with ancient, forgotten books.");
        Room room4 = new Room("Room 4", "A long, narrow corridor with torches on the walls.");
        Room room5 = new Room("Room 5", "The hidden chamber. Something valuable might be here!");
        Room room6 = new Room("Room 6", "An overgrown greenhouse where strange plants thrive.");
        Room room7 = new Room("Room 7", "A cold cellar smelling of damp earth.");
        Room room8 = new Room("Room 8", "An armory with rusted swords and shields.");
        Room room9 = new Room("Room 9", "A grand balcony overlooking a dark forest.");

        //Tilføj items til specifikke rum
        room1.addItem(new Item("lamp", "a shiny brass lamp"));
        room1.addItem(new Item ("coins", "some gold coins"));
        room3.addItem(new Item ("book", "an ancient, forgotten book"));
        room5.addItem(new Item ("diamond", "a massive glittering diamond"));

        //Food-objekter
        Food bread = new Food("bread", "a loaf of stale bread", 10);
        Food mushroom = new Food("mushroom", "a pale glowing mushroom", -50);
        room1.addItem(bread);
        room2.addItem(mushroom);


        //RangedWeapon-objekter
        RangedWeapon revolver = new RangedWeapon("revolver", "an old revolver", 5, 35);
        RangedWeapon shotgun = new RangedWeapon("shotgun", "a double-barreled shotgun", 2, 50);

        //MeleeWeapon-objekter
        MeleeWeapon sword = new MeleeWeapon("sword", "a rusty sword", 19);
        MeleeWeapon knife = new MeleeWeapon("knife", "a sharp kitchen knife", 12);
        room3.addItem(sword);
        room6.addItem(shotgun);
        room8.addItem(knife);
        room2.addItem(revolver);

        //Enemy-objekter
        Enemy librarian = new Enemy("librarian", "a grumpy librarian", 40, shotgun, room3);
        room3.addEnemy(librarian);

        Enemy troll = new Enemy("troll", "a cave troll", 100, knife, room7);
        room7.addEnemy(troll);

        Enemy plant = new Enemy("plant", "a mutant venus plant", 60, sword, room6);
        room6.addEnemy(plant);

        //2. Forbind den øverste række (1, 2, 3)
        room1.setEast(room2);
        room2.setWest(room1);
        room2.setEast(room3);
        room3.setWest(room2);

        //3. Forbind lodret ned til midterste række (1->4, 3->6)
        room1.setSouth(room4);
        room4.setNorth(room1);
        room3.setSouth(room6);
        room6.setNorth(room3);

        //4. Forbind den midtereste række (Room 5 har kun en indgang)

        //Man går East fra Room 4 for at komme ind i Room 5
        room4.setEast(room5);

        //Man går West fra room 5 for at komme tilbage til Room 4
        room5.setWest(room4);

        //5. Forbinder resten af rækkerne (4->7, 6->9 osv. Indtil alle hænger sammen som kortet viser)
        room4.setSouth(room7);
        room7.setNorth(room4);

        room6.setSouth(room9);
        room9.setNorth(room6);

        room7.setEast(room8);
        room8.setWest(room7);
        room8.setEast(room9);
        room9.setWest(room8);

        //Spilleren starter alid i room 1
        this.startRoom = room1;
    }

    //Get-metode
    public Room getStartRoom(){
        return startRoom;
    }
}
