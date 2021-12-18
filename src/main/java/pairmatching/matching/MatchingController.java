package pairmatching.matching;

import pairmatching.GeneralInputView;

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
            // 검증로직
            choice = input;

        } catch (IllegalArgumentException e) {
            choice = inputFunctionByUser();
        }
        return choice;
    }

    public String inputCourseByUser() {
        String selectCourse;
        try {
            String inputCourse = GeneralInputView.inputCourseByClient();
            // 검증 로직
            selectCourse = inputCourse;
        } catch (IllegalArgumentException e) {
            selectCourse = inputCourseByUser();
        }
        return selectCourse;
    }


    public void startMatching(String courseName) {
        matchingService.hasDistinctMatching(courseName);
        List<String> allMatched = new ArrayList<>();
        try {
            matchingService.hasAlreadyMatching(courseName);
            allMatched = matchingService.startMatching(courseName);
        } catch (IllegalArgumentException e) {
            String inputReMatching = MatchingInputView.duplicateMatchingResult();
            if (inputReMatching.equals(REMATCH_NO)) {
                return;
            }
            allMatched = matchingService.startMatching(courseName);
        }
        MatchingOutputView.seeMatchingResult(allMatched);
    }

    public void seeMatchingInfo(String courseName) {
        matchingService.returnMatching(courseName);
        MatchingOutputView.seeMatchingResult(
                matchingService.returnMatching(courseName));
    }

    public void resetMatching() {
        // 초기화 로직
        MatchingOutputView.resetFinishedPrint();
    }

}
