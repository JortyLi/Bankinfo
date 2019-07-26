package com.example.bankinfo.common;

/**
 * Created by Administrator on 2017/6/3.
 */
public enum StationEnum {

    STATION_A("秀场1站", "a"), STATION_B("秀场2站", "b"), STATION_C("秀场3站", "c"), STATION_D("秀场4站", "d"), STATION_E("秀场5站", "e"), STATION_F("秀场6站", "f"), STATION_G("丽柜", "g"), STATION_H("金屋", "h"), STATION_Y("维纳斯", "y"), STATION_L("秀场9站", "l");

    private String name;

    private String description;

    StationEnum(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public static String getDescriptionByName(String name){
        for(StationEnum se : StationEnum.values()){
            if(se.getName().equals(name)){
                return se.getDescription();
            }
        }
        return null;
    }

}
