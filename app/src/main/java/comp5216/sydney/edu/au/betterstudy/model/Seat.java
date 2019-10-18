package comp5216.sydney.edu.au.betterstudy.model;

import java.util.Date;
import java.util.List;

public class Seat {

    private String seatNumber;
    //用 Date类型的列表来存座位的状态 例如 几点被预定了就往list 里面加个时间
    //需要写一个函数判断一个时间是否在另一个时间的接下去2小时内

    private List<Date> Status;

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public List<Date> getStatus() {
        return Status;
    }

    public void setStatus(List<Date> status) {
        Status = status;
    }

    //判断新时间是否在列表时间的2小时内
    public boolean isDateInTheNextTwoHours(Date inputDate, Date compareDate){
        long gapTime = inputDate.getTime() - compareDate.getTime();
        if (gapTime/1000/60/60 < 2){
            return true;
        }else {
            return false;
        }
    }


}
