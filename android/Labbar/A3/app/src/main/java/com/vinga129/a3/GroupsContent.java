package com.vinga129.a3;

import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

import com.vinga129.a3.retro.RetroUserList;
import com.vinga129.a3.retro.RetrofitClient;
import com.vinga129.a3.retro.UserService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupsContent {
    private List<GroupItem> items;
    private Map<String, GroupItem> itemMap;
    private List<RetroUserList.RetroUser> list;

    public GroupsContent(List<RetroUserList.RetroUser> list) {
        this.list = list;
        this.items = new ArrayList<GroupItem>();
        this.itemMap = new HashMap<String, GroupItem>();

        setItems();
        Log.d(TAG, "GroupsContent: ");
    }

    private static final String TAG = "logger";

    private void setItems() {
        for (int i = 0; i < list.size(); i++) {
            addItem(createGroupItem(list.get(i).getName(), "d"));
        }
    }
    public List<GroupItem> getItems() {
        return items;
    }

    public Map<String, GroupItem> getItemMap() {
        return itemMap;
    }

    private void addItem(GroupItem item) {
        items.add(item);
        itemMap.put(item.id, item);
    }

    private static GroupItem createGroupItem(String content, String details) {
        return new GroupItem(content.toLowerCase(), content, details);
    }

    /*private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/


    public static class GroupItem implements Serializable {
        public final String id;
        public final String content;
        public final String details;

        public GroupItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        public String getContent() {
            return content;
        }

        public String getDetails() {
            return details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
