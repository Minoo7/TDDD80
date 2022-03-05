package com.vinga129.a2;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupsContent {
    private final Resources resources;
    private List<GroupItem> items;
    private Map<String, GroupItem> itemMap;

    public GroupsContent(Resources resources) {
        this.resources = resources;
        this.items = new ArrayList<GroupItem>();
        this.itemMap = new HashMap<String, GroupItem>();

        setItems();
    }

    private void setItems() {
        String[] content = resources.getStringArray(R.array.content);
        String[] details = resources.getStringArray(R.array.details);
        for (int i = 0; i < content.length; i++) {
            addItem(createGroupItem(content[i], details[i]));
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


    public static class GroupItem {
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
