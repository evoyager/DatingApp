package com.gusar.datingapp;

import com.gusar.datingapp.model.ModelPerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by evgeniy on 17.01.16.
 */
public final class Constants {

    public static final String[] IMAGES = new String[] {
            "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
            "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"};

    private static int page_num = 0;

    public static void setPageNum(int page_number) {
        page_num = page_number;
    }

    public static int getPageNum() {
        return page_num++;
    }

    private static List<ModelPerson> PERSONS = new ArrayList<ModelPerson>();
    private static Map<Integer, Boolean> liked = new HashMap<Integer, Boolean>();

    public static void changeLikeStatus(Integer id, boolean like) {
        if (like)
            liked.put(id, true);
        else {
            liked.remove(id);
        }
    }

    public static boolean isLiked(Integer id) {
        return liked.containsKey(id);
    }

    public static void setPersons(List<ModelPerson> persons) {
        PERSONS = persons;
    }

    public static List<ModelPerson> getPersons() {
        return PERSONS;
    }

    private Constants() {
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }
}
