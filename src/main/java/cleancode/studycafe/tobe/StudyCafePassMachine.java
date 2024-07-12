package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.model.pass.seat.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.seat.StudyCafeSeatPasses;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final StudyCafeIOHandler studyCafeIOHandler = new StudyCafeIOHandler();
    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

    public void run() {
        try {
            studyCafeIOHandler.showWelcomeMessage();
            studyCafeIOHandler.showAnnouncement();

            StudyCafePassType studyCafePassType = studyCafeIOHandler.askPassType();
            StudyCafeSeatPasses allPasses = studyCafeFileHandler.readStudyCafePasses();

            List<StudyCafeSeatPass> passCandidates = allPasses.findPassBy(studyCafePassType);
            StudyCafeSeatPass selectedPass = studyCafeIOHandler.getPassTypeFromUser(passCandidates);

            StudyCafeLockerPasses allLockerPasses = studyCafeFileHandler.readLockerPasses();
            Optional<StudyCafeLockerPass> optionalLockerPass = allLockerPasses.getLockerPassBasedOn(selectedPass);

            optionalLockerPass.ifPresentOrElse(
                    lockerPass -> {
                        if (studyCafeIOHandler.doesUserWantToUseLocker(lockerPass)) {
                            studyCafeIOHandler.showPassOrderSummary(selectedPass, lockerPass);
                            return;
                        }
                        studyCafeIOHandler.showPassOrderSummary(selectedPass);
                    },
                    () -> studyCafeIOHandler.showPassOrderSummary(selectedPass)
            );

//            if (StudyCafePassType.doesNotSupportLockerUseType(studyCafePassType)) {
//                studyCafeIOHandler.showPassOrderSummary(selectedPass);
//                return;
//            }

        } catch (AppException e) {
            studyCafeIOHandler.showExceptionMessage(e);
        } catch (Exception e) {
            studyCafeIOHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }
}
