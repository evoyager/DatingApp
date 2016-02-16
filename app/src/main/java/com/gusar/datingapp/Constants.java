package com.gusar.datingapp;

import android.graphics.Bitmap;

import com.gusar.datingapp.model.ModelPerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by evgeniy on 17.01.16.
 */
public class Constants {

    private static int page_num = 0;
    private static List<ModelPerson> CURRENT_PERSONS = new ArrayList<ModelPerson>();
    private static Map<Integer, ModelPerson> ALL_PERSONS = new HashMap<Integer, ModelPerson>();
//    private static Map<Integer, Boolean> liked = new HashMap<Integer, Boolean>();
    private static List<Bitmap> bitmaps = new ArrayList<Bitmap>();

    public static void addBitmap(Bitmap new_bitmap) {
        bitmaps.add(new_bitmap);
    }

    public static List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public static void setPageNum(int page_number) {
        page_num = page_number;
    }
    public static int getPageNum() {
        return page_num++;
    }

    public static void changeLikeStatus(Integer id, boolean like) {
//        ALL_PERSONS.get(id).setLike(like);
        ModelPerson p = ALL_PERSONS.get(id);
        p.setLike(like);
        ALL_PERSONS.put(id, p);
//        if (like)
//            liked.put(id, true);
//        else {
//            liked.remove(id);
//        }
    }

    public static boolean isLike(Integer i) {
        return ALL_PERSONS.get(i).isLike();
    }

    public static void setPersons(List<ModelPerson> persons) {
        for (ModelPerson p: persons) {
            if (!ALL_PERSONS.containsKey(p.getId()))
                ALL_PERSONS.put(p.getId(), p);
        }
        CURRENT_PERSONS = persons;
    }

    public static List<ModelPerson> getPersons() {
        return CURRENT_PERSONS;
    }

    private Constants() {
    }
}
