package com.mchacko.gridimagesearch.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mchacko on 2/1/15.
 */
public class ImageSearchSettings implements Serializable{
    public Size size = Size.any;
    public Color color = Color.any;
    public Type type = Type.any;
    public String site = "";

    public String getQueryParams() {
        String params = "";
        if (!size.equals(Size.any)) {
            params += "&imgsz=" + size.text;
        }
        if (!color.equals(Color.any)) {
            params += "&imgcolor=" + color.text;
        }
        if (!type.equals(Type.any)) {
            params += "&imgtype=" + type.text;
        }
        if(!site.equals("")) {
            params += "&as_sitesearch=" + site;
        }
        return params;
    }

    public static enum Size {
        any(0,""),
        small(1,"small"),
        medium(2,"medium"),
        large(3,"large"),
        xlarge(4,"xlarge");
        public int value;
        public String text;
        Size(int value, String text) {
            this.value = value;
            this.text = text;
        }

        private static final Map<Integer, Size> intToTypeMap = new HashMap<Integer, Size>();
        static {
            for (Size type : Size.values()) {
                intToTypeMap.put(type.value, type);
            }
        }

        public static Size fromInt(int selectedItemPosition) {
            Size type = intToTypeMap.get(Integer.valueOf(selectedItemPosition));
            return type;
        }
    };

    public static enum Color {
        any(0,""),
        black(1,"black"),
        blue(2,"blue"),
        brown(3,"brown"),
        gray(4,"gray"),
        green(5,"green"),
        orange(6,"orange"),
        ping(7,"ping"),
        purple(8,"purple"),
        red(9,"red"),
        teal(10,"teal"),
        white(11,"white"),
        yellow(12,"yellow");
        public int value;
        public String text;
        Color(int value, String text) {
            this.value = value;
            this.text = text;
        }
        private static final Map<Integer, Color> intToTypeMap = new HashMap<Integer, Color>();
        static {
            for (Color type : Color.values()) {
                intToTypeMap.put(type.value, type);
            }
        }

        public static Color fromInt(int selectedItemPosition) {
            Color type = intToTypeMap.get(Integer.valueOf(selectedItemPosition));
            return type;
        }
    };

    public static enum Type {
        any(0,""),
        face(1,"face"),
        photo(2,"photo"),
        clipart(3,"clipart"),
        lineart(4,"lineart");
        public int value;
        public String text;
        Type(int value, String text) {
            this.value = value;
            this.text = text;
        }
        private static final Map<Integer, Type> intToTypeMap = new HashMap<Integer, Type>();
        static {
            for (Type type : Type.values()) {
                intToTypeMap.put(type.value, type);
            }
        }

        public static Type fromInt(int selectedItemPosition) {
            Type type = intToTypeMap.get(Integer.valueOf(selectedItemPosition));
            return type;
        }
    };


}
