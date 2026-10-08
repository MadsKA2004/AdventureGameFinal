import java.util.ArrayList;

public class UI {
    private final Adventure game = new Adventure();
    private boolean running = true;

    public void startGame() {
        //Velkomstbesked og beskrivelse af start-rummet
        IO.println("Welcome to the Adventure Game!");
        IO.println("Commands: go north/south/east/west (or just n/s/e/w), look, help, inventory, health, take <item>, drop <item>, eat <food>, equip <weapon> exit.");
        IO.println("----------------------------------------------------------------------------");
        printRoomInfo();

        while (running) {
            String input = IO.readln("> ").toLowerCase().trim();


            //Tjek for intenting. Hvis bruger bare trykker Enter
            if (input.isEmpty()) {
                IO.println("You must type a command! Type 'help' for list of commands.");
                continue; //Springer resten af koden over og starter forfra i løkken
            }

            if (input.startsWith("go ")) {
                input = input.substring(3).trim();
            }

            switch (input) {
                case "Look" -> printRoomInfo();
                case "inventory", "inv" -> printInventory();
                case "health" -> printPlayerHealth();
                case "help" -> printHelp();
                case "exit" -> {
                    IO.println("Game Over");
                    running = false;
                }


                //Retninger
                case "north", "n" -> handleMove("north");
                case "south", "s" -> handleMove("south");
                case "east", "e" -> handleMove("east");
                case "west", "w" -> handleMove("west");


                case "take", "t" -> IO.println("What do you want to take? (e.g., 'take lamp')");
                case "drop", "d" -> IO.println("What do you want to drop? (e.g., 'drop lamp')");
                case "eat" -> IO.println("What do you want to eat? (e.g., 'eat bread')");
                case "equip" -> IO.println("What weapon do you want to equip? (e.g., 'equip revolver')");
                case "attack" -> handleAttack("");
                default -> {
                    if (input.startsWith("take ")) {
                        handleTake(input.replace("take ", ""));
                    } else if (input.startsWith("drop ")) {
                        handleDrop(input.replace("drop ", ""));
                    } else if (input.startsWith("eat ")) {
                        handleEat(input.replace("eat ", ""));
                    } else if (input.startsWith("equip ")) {
                        handleEquip(input.replace("equip ", ""));
                    } else if (input.startsWith("attack ")) {
                        handleAttack(input.replace("attack ", ""));
                    } else {
                        IO.println("I don't understand that command. Type 'help' for a list of commands.");
                    }
                }
            }
        }
    }


    private void handleMove(String direction) {
        if (!game.movePlayer(direction)) {
            IO.println("Ouch! You hit a wall. You cannot go that way");
            return; //stopper metoden med det samme, hvis vi ikke printer rum-info
        }
        printRoomInfo();
    }

    private void handleTake(String itemName){
        Item takenItem = game.takeItem(itemName);
        if (takenItem != null){
            IO.println("You have taken the " + takenItem.getShortName());
        } else {
            IO.println("There is nothing like " + itemName + " to take around here.");
        }
    }


    private void handleDrop(String itemName){
        Item droppedItem = game.dropItem(itemName);
        if (droppedItem != null){
            IO.println("You have dropped the " + droppedItem.getShortName());
        } else {
            IO.println("You don't have anything like " + itemName + " in your inventory.");
        }
    }

    private void handleEat(String itemName){
        Item item = game.findItemInGame(itemName);
        EatResult result = game.eatItem(itemName);

        switch (result) {
            case NOT_FOUND -> IO.println("There is nothing like " + itemName + " to eat around here.");
            case NOT_FOOD -> IO.println("You cannot eat the " + (item != null ? item.getShortName() : itemName) + "!");

            case EATEN -> {
                // Fordi vi fandt 'item' før den blev slettet fra listen (inventory.remove eller item.remove), kan vi tjekke dens Food-egenskaber nu!
                Food food = (Food) item;

                // Tjek om det er sundt eller giftigt (plus eller minus point)
                if (food.getHealthPoints() > 0){
                    IO.println("You eat the " + food.getShortName() + ". You feel a little better.");
                } else {
                    IO.println("You eat the " + food.getShortName() + ". That was a mistake.");
                }
                printPlayerHealth(); //Vis deres nye health status med det samme
            }
        }
    }

    private void handleEquip(String itemName){
        //Kalder på Adventure, som kalder på Player og returner teksten
        IO.println(game.equipWeapon(itemName));
    }

    private void handleAttack(String targetEnemyName){
        //Kalder på Adventure, som kalder på Player
        IO.println(game.attackWithWeapon(targetEnemyName));

    }

    private void printInventory(){
        //Viser ens inventory
        ArrayList<Item> inventory = game.getPlayerInventory();

        if (inventory.isEmpty()){
            IO.println("You are not carrying anything right now.");
        } else {
            IO.println("You are carrying: ");
            for (Item item : inventory){
                IO.println("- " + item.getLongName());
            }
        }

        //Viser equipped weapon i inventory
        String equippedName = game.getEquippedWeaponName();
        IO.println("----------------");
        IO.println("Equipped Weapon: " + (equippedName != null ? equippedName : "None"));
        IO.println(" ");
    }

    private void printPlayerHealth(){
        int currentHealth = game.getPlayerHealth();
        if (currentHealth >= 100){
            IO.println("Health: " + currentHealth + " - you are in perfect health");
        } else if (currentHealth >= 50){
            IO.println("Health: " + currentHealth + " - you are in good health, but avoid fighting right now");
        } else {
            IO.println("Health: " + currentHealth + " - you are in poore health. Find some food quickly!");
        }
    }


    private void printHelp(){
        IO.println("\n--- (Help Menu) ---");
        IO.println("Move: 'south', 'north', 'west', 'east'");
        IO.println("Look: See the room description and items again");
        IO.println("Take/Drop <item>: 'take lamp, 'drop book'");
        IO.println("Inventory: Type 'inventory' or 'inv' to see your items.");
        IO.println("Health: type 'health' or 'h' to see your health.");
        IO.println("Equip <weapon>: 'equip sword', 'equip knife'");
        IO.println("Attack <Enemy>: type 'attack <troll>' to attack enemy");
        IO.println("Eat <food>: 'eat mushroom' or 'eat bread' increases or decreases your health.");
        IO.println("Exit: Quit the game.");
    }

    private void printRoomInfo() {
        IO.println("You are in " + game.getCurrentRoomName());
        IO.println(game.getCurrentRoomDescription());
        IO.println();

        //Vis items i rummet
        ArrayList<Item> roomItems = game.getCurrentRoomItems();
        if (!roomItems.isEmpty()) {
            IO.print("Here you see: ");
            for (int i = 0; i < roomItems.size(); i++) {
                IO.print(roomItems.get(i).getLongName());
                if (i < roomItems.size() - 1) { //hvis det er mere end 1 item
                    IO.println(i == roomItems.size() -2 ? ", and " : ",");
                    }
                }
            IO.println("");
        }
        //Viser fjender i rummet
        Enemy enemy = game.getCurrentRoomFirstEnemy(); //kalder på metoden i adventure
        if (enemy != null){
            IO.println("Beware! Here lurks: "  + enemy.getDescription());
        }
    }
}


