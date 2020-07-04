package com.example.karthik.testuploadtotomcat;

public class EachRecord {
String cname;
String cpred;
    public EachRecord(String cname, String cpred) {
        this.cname = cname;
        this.cpred = cpred;
    }

    public EachRecord() {
    }

    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCpred() {
        return cpred;
    }

    public void setCpred(String cpred) {
        this.cpred = cpred;
    }
}
