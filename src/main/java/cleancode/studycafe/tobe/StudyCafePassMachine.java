package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;

public class StudyCafePassMachine {

    private final StudyCafeIOHandler studyCafeIOHandler = new StudyCafeIOHandler();
    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

    public void run() {
        try {
            studyCafeIOHandler.showWelcomeMessage();
            studyCafeIOHandler.showAnnouncement();

            StudyCafePassType studyCafePassType = studyCafeIOHandler.askPassType();

            List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePasses();

            List<StudyCafePass> userPasses = studyCafePasses.stream()
                    .filter(studyCafePass -> studyCafePass.hasSamePassType(studyCafePassType))
                    .toList();

            StudyCafePass selectedPass = studyCafeIOHandler.getPassTypeFromUser(userPasses);

            if (StudyCafePassType.doesNotSupportLockerUseType(studyCafePassType))
                studyCafeIOHandler.showPassOrderSummary(selectedPass);

            if (StudyCafePassType.doesSupportLockerUseType(studyCafePassType)) {
                List<StudyCafeLockerPass> lockerPasses = studyCafeFileHandler.readLockerPasses();
                StudyCafeLockerPass lockerPass = getCafeLockerPass(selectedPass, lockerPasses);

                boolean lockerSelection = false;

                if (HasValidLockerPass(lockerPass)) {
                    // 사물함 이용? - 12주권 - 30000원 (1 2)
                    lockerSelection = studyCafeIOHandler.doesUserSelectToUseLocker(lockerPass);
                }

                if (lockerSelection) {
                    studyCafeIOHandler.showPassOrderSummary(selectedPass, lockerPass);
                } else {
                    studyCafeIOHandler.showPassOrderSummary(selectedPass);
                }
            }
        } catch (AppException e) {
            studyCafeIOHandler.showExceptionMessage(e);
        } catch (Exception e) {
            studyCafeIOHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }


    private boolean HasValidLockerPass(StudyCafeLockerPass lockerPass) {
        return lockerPass != null;
    }

    private StudyCafeLockerPass getCafeLockerPass(StudyCafePass selectedPass, List<StudyCafeLockerPass> lockerPasses) {
        return lockerPasses.stream()
                .filter(option ->
                        option.hasSamePassTypeAndDuration(selectedPass)
                )
                .findFirst()
                .orElse(null);
    }
}
