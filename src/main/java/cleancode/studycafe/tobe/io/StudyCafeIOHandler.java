package cleancode.studycafe.tobe.io;

import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;

public class StudyCafeIOHandler {
    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();

    public boolean doesUserSelectToUseLocker(StudyCafeLockerPass studyCafeLockerPass){
        outputHandler.askLockerPass(studyCafeLockerPass);
        return inputHandler.getLockerSelection();
    }

    public void showWelcomeMessage(){
        outputHandler.showWelcomeMessage();
    }

    public void showAnnouncement(){
        outputHandler.showAnnouncement();
    }

    public StudyCafePassType askPassType(){
       outputHandler.askPassTypeSelection();
       return inputHandler.getPassTypeSelectingUserAction();
    }

    public StudyCafePass getPassTypeFromUser(List<StudyCafePass> passes) {
        outputHandler.showPassList(passes);
        return inputHandler.getPassTypeFromUser(passes);
    }

    public void showPassOrderSummary(StudyCafePass selectedPass){
        outputHandler.showPassOrderSummary(selectedPass);
    }

    public void showPassOrderSummary(StudyCafePass selectedPass, StudyCafeLockerPass lockerPass){
        outputHandler.showPassOrderSummary(selectedPass, lockerPass);
    }

    public void showExceptionMessage(Exception e){
        outputHandler.showSimpleMessage(e.getMessage());
    }

    public void showSimpleMessage(String s){
        outputHandler.showSimpleMessage(s);
    }
}
