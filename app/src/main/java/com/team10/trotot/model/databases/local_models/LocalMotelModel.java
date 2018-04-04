package com.team10.trotot.model.databases.local_models;

import com.team10.trotot.model.basic_classes.Motel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Pewdiespyse on 13/11/2017.
 */

public class LocalMotelModel {
    public List<Motel> motelList;

    public LocalMotelModel() {
        motelList = new ArrayList<>();
        motelList.add(new Motel("MT001", "3uuEChU5waTjSlPzDyX8hH7Atol1", "Phòng trọ HCMUS", 1000000, 100, 1, 123123, 6, 23, "0123456789", "Available", 106.6738673, 10.7575444, "227 Nguyễn Văn Cừ", "Tien nghi day du", new ArrayList<String>(Arrays.asList("http://image.khaigiang.vn/school/files/212/thumbnail/600x400/554554khtnlarge.jpg", "gs://trotot-a1819.appspot.com/motels/photos/MT001/1.jpg", "gs://trotot-a1819.appspot.com/motels/photos/MT001/2.jpg")) ));
        motelList.add(new Motel("MT002", "7WVJL3aXwKRVMsuIbedDvFLl6Xu1", "Phòng trọ HCMUT", 2000000, 200, 2, 231231, 7, 22, "0123456789", "Full", 106.710431, 10.7555207, "268 Lý Thường Kiệt", "Dien nuoc day du", new ArrayList<String>(Arrays.asList("http://www.hcmut.edu.vn/upload/ctctsv1/images/2016/10-2016/Khu%20do%20thi%202.JPG", "gs://trotot-a1819.appspot.com/motels/photos/MT002/1.jpg", "gs://trotot-a1819.appspot.com/motels/photos/MT002/2.jpg")) ));
        motelList.add(new Motel("MT003", "8ALLzAeJNwcWjmATVMQRRtbClQc2", "Phòng trọ UEH", 3000000, 300, 3, 321321, 8, 21, "0123456789", "Available", 106.6888019, 10.7939694, "59C Nguyễn Đình Chiểu", "Tien nghi day du", new ArrayList<String>(Arrays.asList("http://cafefcdn.com/thumb_w/650/Images/Uploaded/Share/8a010923642b7bc979a379e399d7ca78/2013/09/23/images666509image001.jpg", "gs://trotot-a1819.appspot.com/motels/photos/MT003/2.jpg")) ));
        motelList.add(new Motel("MT004", "AHBEktRqzfc8GmreEb6guh9Kqp93", "Phòng trọ YDS", 4000000, 400, 4,  432432, 9, 20, "0123456789", "Full", 106.696168, 10.7757575, "Hồng Bàng, Phường 11, Quận 5", "Dien nuoc day du", new ArrayList<String>(Arrays.asList("https://www.vinhtuong.com/Data/Sites/1/Product/386/7-tran-thach-cao-vinhtuong-daihoc-y-duoc.JPG", "gs://trotot-a1819.appspot.com/motels/photos/MT004/1.jpg", "gs://trotot-a1819.appspot.com/motels/photos/MT004/2.jpg")) ));
        motelList.add(new Motel("MT005", "EcoASId7bZWSbaZYoyKE6rFMU9C2", "Phòng trọ HCMUTE", 5000000, 500, 5, 543543, 10, 19, "0123456789", "Available", 106.691361, 10.724320, "1 Võ Văn Ngân", "Tien nghi day du", new ArrayList<String>(Arrays.asList("http://static.giaoducthoidai.vn/Uploaded/dienns/2014_03_21/14_FUKT.jpg", "gs://trotot-a1819.appspot.com/motels/photos/MT005/1.jpg", "gs://trotot-a1819.appspot.com/motels/photos/MT005/2.jpg")) ));
    }

    //Add a motel to local
    public void addMotel(Motel motel) {
        motelList.add(motel);
    }

    //Return a specific motel with index in list
    public Motel getMotel(int index) {
        return this.motelList.get(index);
    }

    //Return a motel with an Id
    public Motel getMotel(String Id) {
        for (Motel motel : this.motelList) {
            if (motel.getMotelId() == Id)
                return motel;
        }

        return null;
    }

    //Return list of motel with specific condition //Not yet finish
    public List<Motel> getMotelListWithCondition() {
        List<Motel> motelList = new ArrayList<>();
        boolean condition = true; //Condition here //Not yet finish
        for (int i = 0; i < this.motelList.size(); i++) {
            if (condition) {
                motelList.add(this.motelList.get(i));
            }
        }
        return motelList;
    }

    //Return a list for adapter
    public List<Motel> getMotelList() {return motelList;}

    //Clear all list of motelList for adapter
    public void clearList() {this.motelList.clear();}

    //Return the total number of motel
    public int getSize() {
        return motelList.size();
    }
}
