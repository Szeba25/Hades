package hu.szeba.hades.model.task.result;

import java.util.List;

public class ResultMatcher {

    private List<ResultDifference> differences;

    public void match(Result referenceResult, Result desiredResult) {

        int referenceResultCount = referenceResult.getResultLineCount();
        int desiredResultCount = desiredResult.getResultLineCount();

        if (referenceResultCount == desiredResultCount) {
            for (int i = 0; i < referenceResultCount; i++) {
                if (!(referenceResult.getResultLine(i).matches(desiredResult.getResultLine(i)))) {
                    // No match in this line.
                    differences.add(new ResultDifference(i,
                            referenceResult.getResultLine(i),
                            desiredResult.getResultLine(i)));
                }
            }
        } else {
            System.out.println("TODO: Different difference size lengths NYI");
        }

    }

    public ResultDifference getDifference(int index) {
        return differences.get(index);
    }

    public int getDifferencesSize() {
        return differences.size();
    }

}
