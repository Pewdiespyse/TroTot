package com.team10.trotot.model.databases.local_models;

import com.team10.trotot.model.basic_classes.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pewdiespyse on 13/11/2017.
 */

public class LocalNewsModel {
    public List<News> newsList;

    public LocalNewsModel() {
        newsList = new ArrayList<>();
        newsList.add(new News("NS001", "Trộm cướp", "Dạo gần đây trộm cướp rất hay lui tới khu vực này hãy cẩn thận...",123456));
        newsList.add(new News("NS002", "Người cùng khổ", "Khổ quá mà, cuối tháng phải ăn mì gói, thật là buồn quá đi...", 234234));
        newsList.add(new News("NS003", "Dê gái bị gái chém", "Số phận thẩm thương của thanh niên vì quá ham muốn...",345345));
        newsList.add(new News("NS004", "Đời sinh viên", "Cuộc đời sinh viên có lúc lên voi xuống chó, mà sao xuống chó hoài...",456456));
        newsList.add(new News("NS005", "Cuộc đời của Vũ CC", "Nếu nói về cuộc đời của Vũ thì chỉ cần một chỉ CC...",567567));
    }

    //Add a news to local
    public void addNews(News news) {
        newsList.add(news);
    }

    //Return a specific news with index in list
    public News getNews(int index) {
        return this.newsList.get(index);
    }

    //Return a news with an Id
    public News getNews(String Id) {
        for (News news : this.newsList) {
            if (news.getNewsId() == Id)
                return news;
        }

        return null;
    }

    //Return list of news with specific condition //Not yet finish
    public List<News> getNewsListWithCondition() {
        List<News> newsList = new ArrayList<>();
        boolean condition = true; //Condition here //Not yet finish
        for (int i = 0; i < this.newsList.size(); i++) {
            if (condition) {
                newsList.add(this.newsList.get(i));
            }
        }
        return newsList;
    }

    //Return a list for adapter
    public List<News> getNewsList() {return newsList;}

    //Clear all list of newsList for adapter
    public void clearList() {this.newsList.clear();}

    //Return the total number of news
    public int getSize() {
        return newsList.size();
    }
}
