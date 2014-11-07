package com.jingwei.db.domain;

import java.io.Serializable;

/**
 * 
 *
 * @author xianwen.tan
 * @date 2014-11-6
 *
 */
public class City implements Serializable {
    /**  */
    private Integer id;

    /**  */
    private String cityName;

    /**  */
    private String countrycode;

    /**  */
    private String district;

    /**  */
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}