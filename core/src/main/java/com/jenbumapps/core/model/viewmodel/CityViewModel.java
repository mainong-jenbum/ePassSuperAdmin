package com.jenbumapps.core.model.viewmodel;

import com.jenbumapps.core.model.City;

import org.parceler.Parcel;

@Parcel
public class CityViewModel {
    private int activeReq;
    private int pendingReq;
    private int expiredReq;
    private City city;

    public CityViewModel(){}


    public CityViewModel(int activeReq, int pendingReq, int expiredReq, City city) {
        this.activeReq = activeReq;
        this.pendingReq = pendingReq;
        this.expiredReq = expiredReq;
        this.city = city;
    }

    public int getActiveReq() {
        return activeReq;
    }

    public void setActiveReq(int activeReq) {
        this.activeReq = activeReq;
    }

    public int getPendingReq() {
        return pendingReq;
    }

    public void setPendingReq(int pendingReq) {
        this.pendingReq = pendingReq;
    }

    public int getExpiredReq() {
        return expiredReq;
    }

    public void setExpiredReq(int expiredReq) {
        this.expiredReq = expiredReq;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
