public enum EquipResult {
    NOT_FOUND("You don't have '%s' in your inventory."),
    NO_WEAPON("You cannot equip the %s!"),
    EQUIPPED("You have equipped the %s.");

    private final String message;

    EquipResult(String message){
        this.message = message;
    }

    public String format(String itemName){
        return String.format(message, itemName);
    }

}
