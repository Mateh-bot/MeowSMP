package org.holiday.meowSMP.enums;

public enum TokenType {
    // Tokens primarios (con niveles)
    LION("Lion Token", true, 0),
    CHEETAH("Cheetah Token", true, 0),
    TIGER("Tiger Token", true, 0),
    HOUSE_CAT("House Cat", true, 60 * 1000),
    SPHINX("Sphinx Token", true, 3 * 60 * 1000),

    // Tokens secundarios (sin niveles)
    CATFISH("Catfish Token", false, 0),
    VILLAGE_CAT("Village Cat", false, 0),
    BLACK_CAT("Black Cat", false, 0),
    JUNGLE_CAT("Jungle Cat", false, 0);

    private final String displayName;
    private final boolean primary;
    private final long abilityCooldown;

    TokenType(String displayName, boolean primary, long abilityCooldown) {
        this.displayName = displayName;
        this.primary = primary;
        this.abilityCooldown = abilityCooldown;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isPrimary() {
        return primary;
    }

    public long getAbilityCooldown() {
        return abilityCooldown;
    }

    // Obtener la clave del token (para CustomModelDataComponent)
    public String getKey(int level) {
        return this.isPrimary() ? this.name().toLowerCase() + "_token_" + level : this.name().toLowerCase() + "_token";
    }
}
