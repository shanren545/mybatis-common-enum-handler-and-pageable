package com.jingwei.db.domain;

import java.io.Serializable;

/**
 * 
 *
 * @author xianwen.tan
 * @date 2014-11-10
 *
 */
public class City implements Serializable {
    /**  */
    private Integer id;

    /** 名称 */
    private String cityName;

    /**  */
    private String countrycode;

    /** 街道 */
    private String district;

    /** 枚举类型转换 */
    private MyEnum code;

    /**  */
    private String longtextsss;

    /**  */
    private byte[] largesss;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public MyEnum getCode() {
        return code;
    }

    public void setCode(MyEnum code) {
        this.code = code;
    }

    public String getLongtextsss() {
        return longtextsss;
    }

    public void setLongtextsss(String longtextsss) {
        this.longtextsss = longtextsss;
    }

    public byte[] getLargesss() {
        return largesss;
    }

    public void setLargesss(byte[] largesss) {
        this.largesss = largesss;
    }
}