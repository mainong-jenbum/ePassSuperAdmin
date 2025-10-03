package com.jenbumapps.core.model.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class HomeModel {
    private int totalActiveReq;
    private int totalNewReq;
    private int totalExpiredReq;

    private List<CityViewModel> cityViewModels;


    public int getTotalActiveReq() {
        return totalActiveReq;
    }

    public void setTotalActiveReq(int totalActiveReq) {
        this.totalActiveReq = totalActiveReq;
    }

    public int getTotalNewReq() {
        return totalNewReq;
    }

    public void setTotalNewReq(int totalNewReq) {
        this.totalNewReq = totalNewReq;
    }

    public int getTotalExpiredReq() {
        return totalExpiredReq;
    }

    public void setTotalExpiredReq(int totalExpiredReq) {
        this.totalExpiredReq = totalExpiredReq;
    }


    public List<CityViewModel> getCityViewModels() {
        if (cityViewModels == null) cityViewModels = new ArrayList<>();
        return cityViewModels;
    }

    public void setCityViewModels(List<CityViewModel> cityViewModels) {
        this.cityViewModels = cityViewModels;
    }
}
