package project.coen268.scu.dogplaydate;

import java.io.Serializable;
import java.util.Date;


/**
 *
 playDatesListsTable.put("userName", "Maggie Q");
 playDatesListsTable.put("userID", "12345567");
 playDatesListsTable.put("dogName", "Winnie");
 playDatesListsTable.put("place", "Mission park");
 //https://www.parse.com/docs/android/guide#objects-data-types

 playDatesListsTable.put("startTime", newSetDateStart.getTime()); // para2: Date
 playDatesListsTable.put("endTime", newSetDateEnd.getTime()); // para2: Date
 playDatesListsTable.put("isValid",true );
 */
public class DatesRecordEntity implements Serializable{
    private Date startTime;
    private Date endTime;
    private String userName;
    private String userID;
    private String dogName;
    private String dogID;
    private String place;
    private boolean isValid;
    private int status;


    public DatesRecordEntity() {
        super();
    }

    public DatesRecordEntity(Date d1, Date d2, String u1, String id1, String u2, String id2, String p, boolean v, int s) {
  // public DatesRecordEntity(String u1, String id1, String u2, String id2, String p, boolean v, int s) {
        super();
        startTime = d1;
        endTime = d2;
        userName = u1;
        userID = id1;
        dogName = u2;
        dogID = id2;
        place = p;
        isValid = v;
        status = s;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime( Date d) {
        startTime = d;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date d) {
        endTime = d;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String s) {
        userName = s;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String s) {
        userID = s;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String s) {
        dogName = s;
    }

    public String getDogID() {
        return dogID;
    }

    public void setDogID(String s) {
        dogID = s;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String s) {
        place = s;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(boolean b) {
        isValid = b;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int s) {
        status = s;
    }

    public String toString() {
        return  "0 " + startTime.toString() + "\n" +
                "1 " + endTime.toString() + "\n" +
                "2 " + userName + "\n" +
                "3 " + userID  + "\n" +
                "4 " + dogName + "\n" +
                "5 " + dogID  + "\n" +
                "6 " + place+ "\n" +
                "7 " + isValid + "\n" +
                "8 " + status+ "\n";
    }
 }
