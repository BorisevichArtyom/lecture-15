package by.itacademy.javaenterprise.entity;

public enum Role {

    ADMIN(0), ATHLETE(1), COACH(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Role getRoleByid(int id) {
        for (Role role : Role.values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("No Role found by this id:" + id);
    }
}