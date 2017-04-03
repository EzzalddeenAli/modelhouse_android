package com.platformstory.modelhouse.DTO;

/**
 * Created by midasyoon on 2017. 3. 30..
 */

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private int type;               // 1이면 부동산 2이면 기업 3이면 일반회원
    private String mobile;
    private String photo;
    private String prefer_region;
    private String reg_path;
    private int auth;               // 가입 승인 여부?
    private String regnum;
    private String addr;
    private String ceo_phone;
    private String ceo_name;

    // 기업 회원 정보
    private String corp_type;       // 건설사, 시공사 등
    private String corp_name;       // 기업명
    private String dept;

    //부동산 회원 정보
    private String office_name;
    private String officenum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrefer_region() {
        return prefer_region;
    }

    public void setPrefer_region(String prefer_region) {
        this.prefer_region = prefer_region;
    }

    public String getReg_path() {
        return reg_path;
    }

    public void setReg_path(String reg_path) {
        this.reg_path = reg_path;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getRegnum() {
        return regnum;
    }

    public void setRegnum(String regnum) {
        this.regnum = regnum;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCeo_phone() {
        return ceo_phone;
    }

    public void setCeo_phone(String ceo_phone) {
        this.ceo_phone = ceo_phone;
    }

    public String getCeo_name() {
        return ceo_name;
    }

    public void setCeo_name(String ceo_name) {
        this.ceo_name = ceo_name;
    }

    public String getCorp_type() {
        return corp_type;
    }

    public void setCorp_type(String corp_type) {
        this.corp_type = corp_type;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        this.corp_name = corp_name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    public String getOfficenum() {
        return officenum;
    }

    public void setOfficenum(String officenum) {
        this.officenum = officenum;
    }
}
