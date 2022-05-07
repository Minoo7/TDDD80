package com.vinga129.savolax.ui.profile.post_preview;

public class PostPreview {

    private final String title;
    private final String content;
    private final String imageUrl;

    public PostPreview(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
