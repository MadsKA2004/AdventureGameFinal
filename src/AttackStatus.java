public enum AttackStatus {
    NO_WEAPON,
    NO_AMMO,
    ATTACK_EMPTY_AIR,
    ENEMY_NOT_FOUND;

    public static AttackStatus getStatus(Weapon weapon, Enemy enemy, String name){
        if (weapon == null) return NO_WEAPON;
        if (!weapon.canUse()) return NO_AMMO;
        if (enemy == null && name.isEmpty()) return ATTACK_EMPTY_AIR;
        if (enemy == null) return ENEMY_NOT_FOUND;
        return null;
    }
}
