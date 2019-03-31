package hu.szeba.hades.main.util;

import hu.szeba.hades.main.view.elements.MappedElement;

public class SortUtilities {

    public static int stringIntegerComparator(String s1, String s2) {
        return Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2));
    }

    public static int mappedElementIntegerComparator(MappedElement e1, MappedElement e2) {
        return stringIntegerComparator(e1.getId(), e2.getId());
    }

    public static int mappedElementStringComparator(MappedElement e1, MappedElement e2) {
        return e1.getId().compareTo(e2.getId());
    }

}
