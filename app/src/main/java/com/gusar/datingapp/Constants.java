package com.gusar.datingapp;

import com.gusar.datingapp.model.ModelPerson;

import java.util.List;

/**
 * Created by evgeniy on 17.01.16.
 */
public final class Constants {

    private static int page_num = 0;

    public static void setPageNum(int page_number) {
        page_num = page_number;
    }

    public static int getPageNum() {
        return page_num++;
    }

    public static final String[] IMAGES = new String[] {
            "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
            "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
            "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg",
            "https://lh3.googleusercontent.com/--L0Km39l5J8/URquXHGcdNI/AAAAAAAAAbs/3ZrSJNrSomQ/s1024/Antelope%252520Butte.jpg",
            "https://lh6.googleusercontent.com/-8HO-4vIFnlw/URquZnsFgtI/AAAAAAAAAbs/WT8jViTF7vw/s1024/Antelope%252520Hallway.jpg",
            "https://lh4.googleusercontent.com/-WIuWgVcU3Qw/URqubRVcj4I/AAAAAAAAAbs/YvbwgGjwdIQ/s1024/Antelope%252520Walls.jpg",
            "https://lh6.googleusercontent.com/-UBmLbPELvoQ/URqucCdv0kI/AAAAAAAAAbs/IdNhr2VQoQs/s1024/Apre%2525CC%252580s%252520la%252520Pluie.jpg",
            "https://lh3.googleusercontent.com/-s-AFpvgSeew/URquc6dF-JI/AAAAAAAAAbs/Mt3xNGRUd68/s1024/Backlit%252520Cloud.jpg",
            "https://lh5.googleusercontent.com/-bvmif9a9YOQ/URquea3heHI/AAAAAAAAAbs/rcr6wyeQtAo/s1024/Bee%252520and%252520Flower.jpg",
            "https://lh5.googleusercontent.com/-n7mdm7I7FGs/URqueT_BT-I/AAAAAAAAAbs/9MYmXlmpSAo/s1024/Bonzai%252520Rock%252520Sunset.jpg",
            "https://lh6.googleusercontent.com/-4CN4X4t0M1k/URqufPozWzI/AAAAAAAAAbs/8wK41lg1KPs/s1024/Caterpillar.jpg",
            "https://lh3.googleusercontent.com/-rrFnVC8xQEg/URqufdrLBaI/AAAAAAAAAbs/s69WYy_fl1E/s1024/Chess.jpg",
            "https://lh5.googleusercontent.com/-WVpRptWH8Yw/URqugh-QmDI/AAAAAAAAAbs/E-MgBgtlUWU/s1024/Chihuly.jpg",
            "https://lh5.googleusercontent.com/-0BDXkYmckbo/URquhKFW84I/AAAAAAAAAbs/ogQtHCTk2JQ/s1024/Closed%252520Door.jpg",
            "https://lh3.googleusercontent.com/-PyggXXZRykM/URquh-kVvoI/AAAAAAAAAbs/hFtDwhtrHHQ/s1024/Colorado%252520River%252520Sunset.jpg",
            "https://lh3.googleusercontent.com/-ZAs4dNZtALc/URquikvOCWI/AAAAAAAAAbs/DXz4h3dll1Y/s1024/Colors%252520of%252520Autumn.jpg",
            "https://lh4.googleusercontent.com/-GztnWEIiMz8/URqukVCU7bI/AAAAAAAAAbs/jo2Hjv6MZ6M/s1024/Countryside.jpg",
            "https://lh4.googleusercontent.com/-bEg9EZ9QoiM/URquklz3FGI/AAAAAAAAAbs/UUuv8Ac2BaE/s1024/Death%252520Valley%252520-%252520Dunes.jpg",
            "https://lh6.googleusercontent.com/-ijQJ8W68tEE/URqulGkvFEI/AAAAAAAAAbs/zPXvIwi_rFw/s1024/Delicate%252520Arch.jpg",
            "https://lh5.googleusercontent.com/-Oh8mMy2ieng/URqullDwehI/AAAAAAAAAbs/TbdeEfsaIZY/s1024/Despair.jpg",
            "https://lh5.googleusercontent.com/-gl0y4UiAOlk/URqumC_KjBI/AAAAAAAAAbs/PM1eT7dn4oo/s1024/Eagle%252520Fall%252520Sunrise.jpg",
            "https://lh3.googleusercontent.com/-hYYHd2_vXPQ/URqumtJa9eI/AAAAAAAAAbs/wAalXVkbSh0/s1024/Electric%252520Storm.jpg",
//            "http://cs313217.vk.me/v313217800/436c/DO1w-2mKStQ.jpg",
//            "http://cs312726.vk.me/v312726735/fa1/0Dqyd_2ck8Q.jpg",
//            "http://cs312726.vk.me/v312726012/1000/COjFH1cL6LI.jpg",
//            "http://cs312231.vk.me/v312231019/2d6c/9mjAPeNZ9zo.jpg",
//            "http://cs312231.vk.me/v312231019/2d0c/2XL72_m9TkU.jpg",
//            "http://cs310826.vk.me/v310826012/101/wrbVFlaHy90.jpg",
//            "http://cs309931.vk.me/v309931548/78e3/oalgZr5PBRU.jpg",
//            "http://cs309619.vk.me/v309619012/6d78/cVq02lRJsV8.jpg",
//            "http://cs309123.vk.me/v309123012/99ed/wKBHhR3jz0g.jpg",
//            "http://cs309123.vk.me/v309123012/9874/rUcVGlIs5jE.jpg",
//            "http://cs9299.vk.me/v9299447/2a5f/bNoKD9s8P0w.jpg",
//            "http://cs309123.vk.me/v309123012/986c/aA0kmCe5Gb4.jpg",
//            "http://cs309123.vk.me/v309123012/976a/_8uQQO0ONmc.jpg",
//            "http://cs309123.vk.me/v309123012/9750/vShzFKMkn-M.jpg",
//            "http://cs309123.vk.me/v309123012/9122/r7oKX3HSaIg.jpg",
//            "http://cs309123.vk.me/v309123012/8fee/wr9iaKZxKBo.jpg",
//            "http://cs307212.vk.me/v307212320/aee5/u4sNJsXyIKo.jpg",
//            "http://cs9331.vk.me/v9331012/30af/SA1t3W3YKes.jpg",
//            "http://cs307107.vk.me/v307107012/b36c/W9YH6T33M90.jpg",
//            "http://cs305600.vk.me/v305600012/19acb/Tj6KB-ZcbiE.jpg"
    };

    private static List<ModelPerson> PERSONS;

    public static void setPersons(List<ModelPerson> persons) {
        PERSONS = persons;
    }

    public static List<ModelPerson> getPersons() {
        return PERSONS;
    }

    private Constants() {}

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }
}
