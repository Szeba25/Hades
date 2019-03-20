package hu.szeba.hades.main.model.task.graph;

public class Tuple {

    private String element1;
    private String element2;

    public Tuple(String element1, String element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    public Tuple(String tuple) {
        String[] splitted = tuple.split(",");
        switch (splitted.length) {
            case 2:
                element1 = splitted[0].substring(1).trim();
                element2 = splitted[1].substring(0, splitted[1].length()-1).trim();
                break;
            case 1:
                element1 = splitted[0].substring(1, splitted[0].length()-1).trim();
                element2 = "NULL";
                break;
            default:
                break;
        }
    }

    String getElement1() {
        return element1;
    }

    String getElement2() {
        return element2;
    }

    @Override
    public String toString() {
        if (element2.equals("NULL")) {
            return "(" + element1 + ")";
        } else {
            return "(" + element1 + "," + element2 + ")";
        }
    }

}
