package com.example.administrator.essim.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class IllustsBean
        implements Serializable {
    private String caption;
    private String create_date;
    private int height;
    private int id;
    private ImageUrlsBean image_urls;
    private boolean is_bookmarked;
    private boolean is_muted;
    private List<MetaPagesBean> meta_pages;
    private MetaSinglePageBean meta_single_page;
    private int page_count;
    private int restrict;
    private int sanity_level;
    private Object series;
    private List<TagsBean> tags;
    private String title;
    private List<?> tools;
    private int total_bookmarks;
    private int total_view;
    private String type;
    private UserBean user;
    private boolean visible;
    private int width;

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    private String total_comments;

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String paramString) {
        this.caption = paramString;
    }

    public String getCreate_date() {
        return this.create_date;
    }

    public void setCreate_date(String paramString) {
        this.create_date = paramString;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int paramInt) {
        this.height = paramInt;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }

    public ImageUrlsBean getImage_urls() {
        return this.image_urls;
    }

    public void setImage_urls(ImageUrlsBean paramImageUrlsBean) {
        this.image_urls = paramImageUrlsBean;
    }

    public List<MetaPagesBean> getMeta_pages() {
        return this.meta_pages;
    }

    public void setMeta_pages(List<MetaPagesBean> paramList) {
        this.meta_pages = paramList;
    }

    public MetaSinglePageBean getMeta_single_page() {
        return this.meta_single_page;
    }

    public void setMeta_single_page(MetaSinglePageBean paramMetaSinglePageBean) {
        this.meta_single_page = paramMetaSinglePageBean;
    }

    public int getPage_count() {
        return this.page_count;
    }

    public void setPage_count(int paramInt) {
        this.page_count = paramInt;
    }

    public int getRestrict() {
        return this.restrict;
    }

    public void setRestrict(int paramInt) {
        this.restrict = paramInt;
    }

    public int getSanity_level() {
        return this.sanity_level;
    }

    public void setSanity_level(int paramInt) {
        this.sanity_level = paramInt;
    }

    public Object getSeries() {
        return this.series;
    }

    public void setSeries(Object paramObject) {
        this.series = paramObject;
    }

    public List<TagsBean> getTags() {
        return this.tags;
    }

    public void setTags(List<TagsBean> paramList) {
        this.tags = paramList;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }

    public List<?> getTools() {
        return this.tools;
    }

    public void setTools(List<?> paramList) {
        this.tools = paramList;
    }

    public int getTotal_bookmarks() {
        return this.total_bookmarks;
    }

    public void setTotal_bookmarks(int paramInt) {
        this.total_bookmarks = paramInt;
    }

    public int getTotal_view() {
        return this.total_view;
    }

    public void setTotal_view(int paramInt) {
        this.total_view = paramInt;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String paramString) {
        this.type = paramString;
    }

    public UserBean getUser() {
        return this.user;
    }

    public void setUser(UserBean paramUserBean) {
        this.user = paramUserBean;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int paramInt) {
        this.width = paramInt;
    }

    public boolean isIs_bookmarked() {
        return this.is_bookmarked;
    }

    public void setIs_bookmarked(boolean paramBoolean) {
        this.is_bookmarked = paramBoolean;
    }

    public boolean isIs_muted() {
        return this.is_muted;
    }

    public void setIs_muted(boolean paramBoolean) {
        this.is_muted = paramBoolean;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean paramBoolean) {
        this.visible = paramBoolean;
    }

    public static class ImageUrlsBean
            implements Serializable {
        private String large;
        private String medium;
        private String square_medium;

        public String getLarge() {
            return this.large;
        }

        public void setLarge(String paramString) {
            this.large = paramString;
        }

        public String getMedium() {
            return this.medium;
        }

        public void setMedium(String paramString) {
            this.medium = paramString;
        }

        public String getSquare_medium() {
            return this.square_medium;
        }

        public void setSquare_medium(String paramString) {
            this.square_medium = paramString;
        }
    }

    public static class MetaPagesBean
            implements Serializable {

        @SerializedName("image_urls")
        private ImageUrlsBean image_urlsX;

        public ImageUrlsBean getImage_urlsX() {
            return this.image_urlsX;
        }

        public void setImage_urlsX(ImageUrlsBean paramImageUrlsBean) {
            this.image_urlsX = paramImageUrlsBean;
        }

        public static class ImageUrlsBean
                implements Serializable {
            private String large;
            private String medium;
            private String original;
            private String square_medium;

            public String getLarge() {
                return this.large;
            }

            public void setLarge(String paramString) {
                this.large = paramString;
            }

            public String getMedium() {
                return this.medium;
            }

            public void setMedium(String paramString) {
                this.medium = paramString;
            }

            public String getOriginal() {
                return this.original;
            }

            public void setOriginal(String paramString) {
                this.original = paramString;
            }

            public String getSquare_medium() {
                return this.square_medium;
            }

            public void setSquare_medium(String paramString) {
                this.square_medium = paramString;
            }
        }
    }

    public static class MetaSinglePageBean
            implements Serializable {
        private String original_image_url;

        public String getOriginal_image_url() {
            return this.original_image_url;
        }

        public void setOriginal_image_url(String paramString) {
            this.original_image_url = paramString;
        }
    }

    public static class TagsBean
            implements Serializable {
        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String paramString) {
            this.name = paramString;
        }
    }

    public static class UserBean
            implements Serializable {
        private String account;
        private int id;
        private boolean is_followed;
        private String name;
        private ProfileImageUrlsBean profile_image_urls;

        public String getAccount() {
            return this.account;
        }

        public void setAccount(String paramString) {
            this.account = paramString;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int paramInt) {
            this.id = paramInt;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String paramString) {
            this.name = paramString;
        }

        public ProfileImageUrlsBean getProfile_image_urls() {
            return this.profile_image_urls;
        }

        public void setProfile_image_urls(ProfileImageUrlsBean paramProfileImageUrlsBean) {
            this.profile_image_urls = paramProfileImageUrlsBean;
        }

        public boolean isIs_followed() {
            return this.is_followed;
        }

        public void setIs_followed(boolean paramBoolean) {
            this.is_followed = paramBoolean;
        }

        public static class ProfileImageUrlsBean
                implements Serializable {
            private String medium;

            public String getMedium() {
                return this.medium;
            }

            public void setMedium(String paramString) {
                this.medium = paramString;
            }
        }
    }
}