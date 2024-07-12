package cleancode.studycafe.tobe.model.pass;

import cleancode.studycafe.tobe.model.pass.StudyCafePassType;

public interface StudyCafePass {
    StudyCafePassType getPassType();

    int getDuration();

    int getPrice();
}
