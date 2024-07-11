package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.*;

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
            StudyCafePasses allPasses = studyCafeFileHandler.readStudyCafePasses();
            List<StudyCafePass> passCandidates = allPasses.findPassBy(studyCafePassType);

            StudyCafePass selectedPass = studyCafeIOHandler.getPassTypeFromUser(passCandidates);

            if (StudyCafePassType.doesNotSupportLockerUseType(studyCafePassType)) {
                studyCafeIOHandler.showPassOrderSummary(selectedPass);
                return;
            }

            StudyCafeLockerPasses lockerPasses = studyCafeFileHandler.readLockerPasses();
            Optional<StudyCafeLockerPass> optionalLockerPass = lockerPasses.getLockerPassBasedOn(selectedPass);

            optionalLockerPass.ifPresentOrElse(
                    lockerPass -> {
                        if (studyCafeIOHandler.doesUserSelectToUseLocker(lockerPass)) {
                            studyCafeIOHandler.showPassOrderSummary(selectedPass, lockerPass);
                            return;
                        }

                        studyCafeIOHandler.showPassOrderSummary(selectedPass);
                    },
                    () -> studyCafeIOHandler.showPassOrderSummary(selectedPass)
            );


        } catch (AppException e) {
            studyCafeIOHandler.showExceptionMessage(e);
        } catch (Exception e) {
            studyCafeIOHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }
}
