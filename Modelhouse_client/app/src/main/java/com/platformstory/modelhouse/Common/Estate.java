package com.platformstory.modelhouse.Common;

public class Estate{
    public int id;
    public String type;
    public String photo;
    public String price_type;
    public String price;
    public String extent;
    public String category;
    public String usearea;
    public String facility;
    public String addr1;
    public String info;
    public Double latitude;
    public Double longtitude;

    public String addr2;
    public String monthly;
    public String monthly_or_annual;
    public String public_price;
    public String land_ratio;
    public String area_ratio;
    public String private_extent;
    public String support_extent;
    public String height;
    public String movein;
    public String loan;
    public String total_floor;
    public String floor;
    public String heater;
    public String fuel;
    public String complete;
    public String parking;

    public int addr_si_id;
    public int addr_gu_id;
    public int addr_dong_id;

    public String manage_price;

    // 매물 검색 결과를 리스트로 뿌려주기 위한 객체 생성자
    public Estate(int id, String type, String photo, String price_type, String price, String extent, String category, String usearea,
           String facility, String addr1, String info, Double latitude, Double longtitude){
        this.id = id;
        this.type = type;
        this.photo = photo;
        this.price_type = price_type;
        this.price = price;
        this.extent = extent;
        this.category = category;
        this.usearea = usearea;
        this.facility = facility;
        this.addr1 = addr1;
        this.info = info;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    // 매물을 등록하기 위한 객체 생성자
    public Estate(String type, String photo, String price_type, String price, String extent, String category, String usearea,
                  String facility, String addr1, String info, Double latitude, Double longtitude, String addr2, String monthly, String monthly_or_annual,
                  String public_price, String land_ratio, String area_ratio, String private_extent, String support_extent, String height, String movein,
                  String loan, String total_floor, String floor, String heater, String fuel, String complete, String parking, int addr_si_id, int addr_gu_id, int addr_dong_id, String manage_price){
        this.type = type;
        this.photo = photo;
        this.price_type = price_type;
        this.price = price;
        this.extent = extent;
        this.category = category;
        this.usearea = usearea;
        this.facility = facility;
        this.addr1 = addr1;
        this.info = info;
        this.latitude = latitude;
        this.longtitude = longtitude;

        this.addr2 = addr2;
        this.monthly = monthly;
        this.monthly_or_annual = monthly_or_annual;
        this.public_price = public_price;
        this.land_ratio = land_ratio;
        this.area_ratio = area_ratio;
        this.private_extent = private_extent;
        this.support_extent = support_extent;
        this.height = height;
        this.movein = movein;
        this.loan = loan;
        this.total_floor = total_floor;
        this.floor = floor;
        this.heater = heater;
        this.fuel = fuel;
        this.complete = complete;
        this.parking = parking;

        this.addr_si_id = addr_si_id;
        this.addr_gu_id = addr_gu_id;
        this.addr_dong_id = addr_dong_id;

        this.manage_price = manage_price;
    }

}