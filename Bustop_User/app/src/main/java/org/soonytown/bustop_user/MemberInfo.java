package org.soonytown.bustop_user;


public class MemberInfo {

    private String name;
    private String phoneNumber;
    private String address;
    private String welfareCard;

    public MemberInfo(String name, String phoneNumber, String address, String welfareCard) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.welfareCard = welfareCard;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWelfareCard() {
        return this.welfareCard;
    }
    public void setWelfareCard(String welfareCard) {
        this.welfareCard = welfareCard;
    }

    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
