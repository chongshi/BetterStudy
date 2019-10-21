package comp5216.sydney.edu.au.betterstudy.model;

import java.util.Date;
import java.util.List;

public class Seat {


    int I, J;
    String st, ft, da, library;

    public Seat() {

    }

    public Seat(int i, int j, String d, String s, String f, String lib) {
        I = i;
        J = j;
        da = d;
        st = s;
        ft = f;
        library = lib;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }


    public void setDa(String da) {
        this.da = da;
    }


    public void setFt(String ft) {
        this.ft = ft;
    }

    public void setI(int i) {
        I = i;
    }

    public void setJ(int j) {
        J = j;
    }


    public void setSt(String st) {
        this.st = st;
    }


    public String getDa() {
        return da;
    }

    public int getI() {
        return I;
    }

    public int getJ() {
        return J;
    }

    public String getFt() {
        return ft;
    }

    public String getSt() {
        return st;
    }



    //判断新时间是否在列表时间的2小时内
    public boolean isDateInTheNextTwoHours(Date inputDate, Date compareDate) {
        long gapTime = inputDate.getTime() - compareDate.getTime();
        if (gapTime / 1000 / 60 / 60 < 2) {
            return true;
        } else {
            return false;
        }
    }


}
