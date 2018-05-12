package com.example.administrator.essim.response;

import java.io.Serializable;

public class UserDetailResponse implements Serializable {
    private ProfileBean profile;
    private ProfilePublicityBean profile_publicity;
    private UserBean user;
    private WorkspaceBean workspace;

    public ProfileBean getProfile() {
        return this.profile;
    }

    public void setProfile(ProfileBean paramProfileBean) {
        this.profile = paramProfileBean;
    }

    public ProfilePublicityBean getProfile_publicity() {
        return this.profile_publicity;
    }

    public void setProfile_publicity(ProfilePublicityBean paramProfilePublicityBean) {
        this.profile_publicity = paramProfilePublicityBean;
    }

    public UserBean getUser() {
        return this.user;
    }

    public void setUser(UserBean paramUserBean) {
        this.user = paramUserBean;
    }

    public WorkspaceBean getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(WorkspaceBean paramWorkspaceBean) {
        this.workspace = paramWorkspaceBean;
    }

    public static class ProfileBean
            implements Serializable {
        private int address_id;
        private String background_image_url;
        private String birth;
        private String birth_day;
        private int birth_year;
        private String country_code;
        private String gender;
        private boolean is_premium;
        private boolean is_using_custom_profile_image;
        private String job;
        private int job_id;
        private String pawoo_url;
        private String region;
        private int total_follow_users;
        private int total_follower;
        private int total_illust_bookmarks_public;
        private int total_illust_series;
        private int total_illusts;
        private int total_manga;
        private int total_mypixiv_users;
        private int total_novels;
        private String twitter_account;
        private Object twitter_url;
        private String webpage;

        public int getAddress_id() {
            return this.address_id;
        }

        public void setAddress_id(int paramInt) {
            this.address_id = paramInt;
        }

        public String getBackground_image_url() {
            return this.background_image_url;
        }

        public void setBackground_image_url(String paramString) {
            this.background_image_url = paramString;
        }

        public String getBirth() {
            return this.birth;
        }

        public void setBirth(String paramString) {
            this.birth = paramString;
        }

        public String getBirth_day() {
            return this.birth_day;
        }

        public void setBirth_day(String paramString) {
            this.birth_day = paramString;
        }

        public int getBirth_year() {
            return this.birth_year;
        }

        public void setBirth_year(int paramInt) {
            this.birth_year = paramInt;
        }

        public String getCountry_code() {
            return this.country_code;
        }

        public void setCountry_code(String paramString) {
            this.country_code = paramString;
        }

        public String getGender() {
            return this.gender;
        }

        public void setGender(String paramString) {
            this.gender = paramString;
        }

        public String getJob() {
            return this.job;
        }

        public void setJob(String paramString) {
            this.job = paramString;
        }

        public int getJob_id() {
            return this.job_id;
        }

        public void setJob_id(int paramInt) {
            this.job_id = paramInt;
        }

        public String getPawoo_url() {
            return this.pawoo_url;
        }

        public void setPawoo_url(String paramString) {
            this.pawoo_url = paramString;
        }

        public String getRegion() {
            return this.region;
        }

        public void setRegion(String paramString) {
            this.region = paramString;
        }

        public int getTotal_follow_users() {
            return this.total_follow_users;
        }

        public void setTotal_follow_users(int paramInt) {
            this.total_follow_users = paramInt;
        }

        public int getTotal_follower() {
            return this.total_follower;
        }

        public void setTotal_follower(int paramInt) {
            this.total_follower = paramInt;
        }

        public int getTotal_illust_bookmarks_public() {
            return this.total_illust_bookmarks_public;
        }

        public void setTotal_illust_bookmarks_public(int paramInt) {
            this.total_illust_bookmarks_public = paramInt;
        }

        public int getTotal_illust_series() {
            return this.total_illust_series;
        }

        public void setTotal_illust_series(int paramInt) {
            this.total_illust_series = paramInt;
        }

        public int getTotal_illusts() {
            return this.total_illusts;
        }

        public void setTotal_illusts(int paramInt) {
            this.total_illusts = paramInt;
        }

        public int getTotal_manga() {
            return this.total_manga;
        }

        public void setTotal_manga(int paramInt) {
            this.total_manga = paramInt;
        }

        public int getTotal_mypixiv_users() {
            return this.total_mypixiv_users;
        }

        public void setTotal_mypixiv_users(int paramInt) {
            this.total_mypixiv_users = paramInt;
        }

        public int getTotal_novels() {
            return this.total_novels;
        }

        public void setTotal_novels(int paramInt) {
            this.total_novels = paramInt;
        }

        public String getTwitter_account() {
            return this.twitter_account;
        }

        public void setTwitter_account(String paramString) {
            this.twitter_account = paramString;
        }

        public Object getTwitter_url() {
            return this.twitter_url;
        }

        public void setTwitter_url(Object paramObject) {
            this.twitter_url = paramObject;
        }

        public String getWebpage() {
            return this.webpage;
        }

        public void setWebpage(String paramString) {
            this.webpage = paramString;
        }

        public boolean isIs_premium() {
            return this.is_premium;
        }

        public void setIs_premium(boolean paramBoolean) {
            this.is_premium = paramBoolean;
        }

        public boolean isIs_using_custom_profile_image() {
            return this.is_using_custom_profile_image;
        }

        public void setIs_using_custom_profile_image(boolean paramBoolean) {
            this.is_using_custom_profile_image = paramBoolean;
        }
    }

    public static class ProfilePublicityBean
            implements Serializable {
        private String birth_day;
        private String birth_year;
        private String gender;
        private String job;
        private boolean pawoo;
        private String region;

        public String getBirth_day() {
            return this.birth_day;
        }

        public void setBirth_day(String paramString) {
            this.birth_day = paramString;
        }

        public String getBirth_year() {
            return this.birth_year;
        }

        public void setBirth_year(String paramString) {
            this.birth_year = paramString;
        }

        public String getGender() {
            return this.gender;
        }

        public void setGender(String paramString) {
            this.gender = paramString;
        }

        public String getJob() {
            return this.job;
        }

        public void setJob(String paramString) {
            this.job = paramString;
        }

        public String getRegion() {
            return this.region;
        }

        public void setRegion(String paramString) {
            this.region = paramString;
        }

        public boolean isPawoo() {
            return this.pawoo;
        }

        public void setPawoo(boolean paramBoolean) {
            this.pawoo = paramBoolean;
        }
    }

    public static class UserBean
            implements Serializable {
        private String account;
        private String comment;
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

        public String getComment() {
            return this.comment;
        }

        public void setComment(String paramString) {
            this.comment = paramString;
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

    public static class WorkspaceBean
            implements Serializable {
        private String chair;
        private String comment;
        private String desk;
        private String desktop;
        private String monitor;
        private String mouse;
        private String music;
        private String pc;
        private String printer;
        private String scanner;
        private String tablet;
        private String tool;
        private Object workspace_image_url;

        public String getChair() {
            return this.chair;
        }

        public void setChair(String paramString) {
            this.chair = paramString;
        }

        public String getComment() {
            return this.comment;
        }

        public void setComment(String paramString) {
            this.comment = paramString;
        }

        public String getDesk() {
            return this.desk;
        }

        public void setDesk(String paramString) {
            this.desk = paramString;
        }

        public String getDesktop() {
            return this.desktop;
        }

        public void setDesktop(String paramString) {
            this.desktop = paramString;
        }

        public String getMonitor() {
            return this.monitor;
        }

        public void setMonitor(String paramString) {
            this.monitor = paramString;
        }

        public String getMouse() {
            return this.mouse;
        }

        public void setMouse(String paramString) {
            this.mouse = paramString;
        }

        public String getMusic() {
            return this.music;
        }

        public void setMusic(String paramString) {
            this.music = paramString;
        }

        public String getPc() {
            return this.pc;
        }

        public void setPc(String paramString) {
            this.pc = paramString;
        }

        public String getPrinter() {
            return this.printer;
        }

        public void setPrinter(String paramString) {
            this.printer = paramString;
        }

        public String getScanner() {
            return this.scanner;
        }

        public void setScanner(String paramString) {
            this.scanner = paramString;
        }

        public String getTablet() {
            return this.tablet;
        }

        public void setTablet(String paramString) {
            this.tablet = paramString;
        }

        public String getTool() {
            return this.tool;
        }

        public void setTool(String paramString) {
            this.tool = paramString;
        }

        public Object getWorkspace_image_url() {
            return this.workspace_image_url;
        }

        public void setWorkspace_image_url(Object paramObject) {
            this.workspace_image_url = paramObject;
        }
    }
}