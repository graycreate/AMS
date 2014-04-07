package me.ghui.AMS.domain;

import me.ghui.AMS.utils.StringHelper;

/**
 * Created by ghui on 3/31/14.
 */
public class Teacher {
    private String id;
    private String name;
    private String sex;
    private String birthday;
    private String education;
    private String degree;
    private String jobTitle;
    private String joinLITTime;
    private String nation;
    private String IDCardNo;
    private String nativePlace;
    private String position;
    private String workStatus;
    private String tel;
    private String cellPhoneNO;
    private String resume;

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", education='" + education + '\'' +
                ", degree='" + degree + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", joinLITTime='" + joinLITTime + '\'' +
                ", nation='" + nation + '\'' +
                ", IDCardNo='" + IDCardNo + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", position='" + position + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", tel='" + tel + '\'' +
                ", cellPhoneNO='" + cellPhoneNO + '\'' +
                ", resume='" + resume + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public Teacher() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJoinLITTime() {
        return joinLITTime;
    }

    public void setJoinLITTime(String joinLITTime) {
        this.joinLITTime = joinLITTime;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getIDCardNo() {
        return IDCardNo;
    }

    public void setIDCardNo(String IDCardNo) {
        this.IDCardNo = IDCardNo;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCellPhoneNO() {
        return cellPhoneNO;
    }

    public void setCellPhoneNO(String cellPhoneNO) {
        this.cellPhoneNO = cellPhoneNO;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

}
