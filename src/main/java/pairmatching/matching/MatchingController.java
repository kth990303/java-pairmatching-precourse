package pairmatching.matching;

import pairmatching.GeneralInputView;
import pairmatching.ValidatorMessage;

import java.util.ArrayList;
import java.util.List;

public class MatchingController {
    private static final String REMATCH_YES = "네";
    private static final String REMATCH_NO = "아니오";
    private final MatchingService matchingService;

    public MatchingController() {
        matchingService = new MatchingService();
    }

    public String inputFunctionByUser() {
        String choice;
        try {
            String input = GeneralInputView.inputFunctionByClient();
            if (!input.equals(GeneralInputView.START_MATCH) && !input.equals(GeneralInputView.SEE_MATCH)
                    && !input.equals(GeneralInputView.INIT_MATCH) && !input.equals(GeneralInputView.QUIT)) {
                throw new IllegalArgumentException();
            }
            choice = input;
        } catch (IllegalArgumentException e) {
            ValidatorMessage.printError(ValidatorMessage.ERROR_MESSAGE + ValidatorMessage.INVALID_FUNCTION);
            choice = inputFunctionByUser();
        }
        return choice;
    }

    public String inputCourseByUser() {
        String selectCourse;
        try {
            String inputCourse = GeneralInputView.inputCourseByClient();
            matchingService.checkValidCourseName(inputCourse);
            selectCourse = inputCourse;
        } catch (IllegalArgumentException e) {
            ValidatorMessage.printError(ValidatorMessage.ERROR_MESSAGE + ValidatorMessage.NO_MISSION);
            selectCourse = inputCourseByUser();
        }
        return selectCourse;
    }


    public void startMatching(String courseName) {
        matchingService.hasDistinctMatching(courseName);
        List<String> allMatched;
        try {
            matchingService.hasAlreadyMatching(courseName);
            allMatched = tryMakingMatching(courseName);
        } catch (IllegalArgumentException e) {
            if (!askAgainRematching()) {
                return;
            }
            allMatched = matchingService.startMatching(courseName);
        }
        MatchingOutputView.seeMatchingResult(allMatched);
    }

    public List<String> tryMakingMatching(String courseName) {
        List<String> matchedList = new ArrayList<>();
        try {
            matchedList = matchingService.startMatching(courseName);
        } catch (IllegalArgumentException e) {
            ValidatorMessage.printError(ValidatorMessage.ERROR_MESSAGE + ValidatorMessage.DUPLICATE_MATCHING);
            matchedList.clear();
        }
        return matchedList;
    }

    public boolean askAgainRematching() {
        try {
            String inputReMatching = MatchingInputView.duplicateMatchingResult();
            if (inputReMatching.equals(REMATCH_YES)) {
                return true;
            }
            if (inputReMatching.equals(REMATCH_NO)) {
                return false;
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            ValidatorMessage.printError(ValidatorMessage.ERROR_MESSAGE + ValidatorMessage.INVALID_REMATCH_ANSWER);
            return askAgainRematching();
        }
    }

    public void seeMatchingInfo(String courseName) {
        matchingService.returnMatching(courseName);
        MatchingOutputView.seeMatchingResult(
                matchingService.returnMatching(courseName));
    }

    public void resetMatching() {
        matchingService.initAllMatching();
        MatchingOutputView.resetFinishedPrint();
    }

}
