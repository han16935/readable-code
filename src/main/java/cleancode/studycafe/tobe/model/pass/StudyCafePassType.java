package cleancode.studycafe.tobe.model.pass;

import java.util.Set;

public enum StudyCafePassType {

    HOURLY("시간 단위 이용권"),
    WEEKLY("주 단위 이용권"),
    FIXED("1인 고정석");

    private final String description;

    StudyCafePassType(String description) {
        this.description = description;
    }

    private final static Set<StudyCafePassType> lockerUseAllowedType = Set.of(FIXED);

    public static boolean doesSupportLockerUseType(StudyCafePassType studyCafePassType) {
        return lockerUseAllowedType.contains(studyCafePassType);
    }

    public static boolean doesNotSupportLockerUseType(StudyCafePassType studyCafePassType) {
        return !doesSupportLockerUseType(studyCafePassType);
    }
}
