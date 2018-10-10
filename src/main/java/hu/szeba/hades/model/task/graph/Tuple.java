package hu.szeba.hades.model.task.graph;

public class Tuple {

    private String element1;
    private String element2;

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

    public void print() {
        System.out.println(element1 + " / " + element2);
    }
}
