package com.team10.trotot.model;

/**
 * Created by vinhkhang on 18/12/2017.
 */

public enum BUNDLE_STRING {

    FILTER_STATUS("filter_status"),
    FILTER_TIME("filter_time"),
    FILTER_FROM_NOTIFICATION("filter_from_notification"),
    FILTER_SHOW_MAP("filter_show_map"),
    FILTER_LOCATION_NAME("filter_location_name"),
    FILTER_LONGITUDE("filter_longitude"),
    FILTER_LATITUDE("filter_latitude"),
    FILTER_RADIUS("filter_radius"),
    FILTER_PRICE_MIN("filter_price_min"),
    FILTER_PRICE_MAX("filter_price_max"),
    FILTER_AREA_MIN("filter_area_min"),
    FILTER_AREA_MAX("filter_area_max"),
    FILTER_RATING_MIN("filter_rating_min"),
    FILTER_RATING_MAX("filter_rating_max");

    private String name;

    BUNDLE_STRING(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
