package com.platformstory.modelhouse.DTO;

public class Estate{
    // 매물 공통 정보 (1 기본 사항)
    private int id;
    private String photo;

    private String addr1;
    private String addr2;
    private int addr_si_id;
    private int addr_gu_id;
    private int addr_dong_id;
    private Double latitude;
    private Double longtitude;

    private String type;                // 매물 타입 1이면 토지 2이면 건물/////////////////////////////////////
    private String deal_type;           // 중개사무소면 1 직거래면 2

    private String price_type;          // 거래 유형 1이면 매매 2이면 전세 3이면 임대
    private String price;
    private String monthly_price;
    private String annual_price;
    private String monthly_or_annual;
    private String manage_price;

    private String public_price;
    private String loan;
    private String extent;
    private String facility;
    private String info;

    // 매물 공통 정보 (2 지목 용도)
    private String category;
    private String usearea;
    private String land_ratio;
    private String area_ratio;

    // 토지에만 해당되는 정보
    private String land_type;///////////////////////////////////////////////////////////////////////////

    // 건물에만 해당되는 정보
    private String private_extent;
    private String support_extent;
    private String height;
    private String movein;

    private String total_floor;
    private String floor;
    private String heater;
    private String fuel;
    private String complete;
    private String parking;

    // 매물 등록한 사용자 관련 정보
    private String name;
    private String mobile;
    private String user_type;       // 1이면 기업회원 2이면 중개사무소 3이면 일반회원///////////////////////////////



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
        this.monthly_price = monthly;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public int getAddr_si_id() {
        return addr_si_id;
    }

    public void setAddr_si_id(int addr_si_id) {
        this.addr_si_id = addr_si_id;
    }

    public int getAddr_gu_id() {
        return addr_gu_id;
    }

    public void setAddr_gu_id(int addr_gu_id) {
        this.addr_gu_id = addr_gu_id;
    }

    public int getAddr_dong_id() {
        return addr_dong_id;
    }

    public void setAddr_dong_id(int addr_dong_id) {
        this.addr_dong_id = addr_dong_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public String getType() {
        return  type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeal_type() {
        return deal_type;
    }

    public void setDeal_type(String deal_type) {
        this.deal_type = deal_type;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMonthly_price() {
        return monthly_price;
    }

    public void setMonthly_price(String monthly_price) {
        this.monthly_price = monthly_price;
    }

    public String getAnnual_price() {
        return annual_price;
    }

    public void setAnnual_price(String annual_price) {
        this.annual_price = annual_price;
    }

    public String getMonthly_or_annual() {
        return monthly_or_annual;
    }

    public void setMonthly_or_annual(String monthly_or_annual) {
        this.monthly_or_annual = monthly_or_annual;
    }

    public String getManage_price() {
        return manage_price;
    }

    public void setManage_price(String manage_price) {
        this.manage_price = manage_price;
    }

    public String getPublic_price() {
        return public_price;
    }

    public void setPublic_price(String public_price) {
        this.public_price = public_price;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsearea() {
        return usearea;
    }

    public void setUsearea(String usearea) {
        this.usearea = usearea;
    }

    public String getLand_ratio() {
        return land_ratio;
    }

    public void setLand_ratio(String land_ratio) {
        this.land_ratio = land_ratio;
    }

    public String getArea_ratio() {
        return area_ratio;
    }

    public void setArea_ratio(String area_ratio) {
        this.area_ratio = area_ratio;
    }

    public String getLand_type() {
        return land_type;
    }

    public void setLand_type(String land_type) {
        this.land_type = land_type;
    }

    public String getPrivate_extent() {
        return private_extent;
    }

    public void setPrivate_extent(String private_extent) {
        this.private_extent = private_extent;
    }

    public String getSupport_extent() {
        return support_extent;
    }

    public void setSupport_extent(String support_extent) {
        this.support_extent = support_extent;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMovein() {
        return movein;
    }

    public void setMovein(String movein) {
        this.movein = movein;
    }

    public String getTotal_floor() {
        return total_floor;
    }

    public void setTotal_floor(String total_floor) {
        this.total_floor = total_floor;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getHeater() {
        return heater;
    }

    public void setHeater(String heater) {
        this.heater = heater;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}