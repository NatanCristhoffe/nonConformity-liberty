package blessed.company.enums;

import lombok.Getter;

@Getter
public enum PlanType {
    BASIC(2),
    PREMIUM(5);

    private final int maxUsers;

    PlanType(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public boolean canAddUser(long currentUsers) {
        return currentUsers < this.maxUsers;
    }

}
