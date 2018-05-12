package com.example.administrator.essim.response;

import java.util.List;

public class SearchUserResponse {
    private String next_url;
    private List<UserPreviewsBean> user_previews;

    public String getNext_url() {
        return this.next_url;
    }

    public void setNext_url(String paramString) {
        this.next_url = paramString;
    }

    public List<UserPreviewsBean> getUser_previews() {
        return this.user_previews;
    }

    public void setUser_previews(List<UserPreviewsBean> paramList) {
        this.user_previews = paramList;
    }

    public static class UserPreviewsBean {
        private List<IllustsBean> illusts;
        private boolean is_muted;
        private List<?> novels;
        private UserBean user;

        public List<IllustsBean> getIllusts() {
            return this.illusts;
        }

        public void setIllusts(List<IllustsBean> paramList) {
            this.illusts = paramList;
        }

        public List<?> getNovels() {
            return this.novels;
        }

        public void setNovels(List<?> paramList) {
            this.novels = paramList;
        }

        public UserBean getUser() {
            return this.user;
        }

        public void setUser(UserBean paramUserBean) {
            this.user = paramUserBean;
        }

        public boolean isIs_muted() {
            return this.is_muted;
        }

        public void setIs_muted(boolean paramBoolean) {
            this.is_muted = paramBoolean;
        }

        public static class UserBean {
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

            public static class ProfileImageUrlsBean {
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
}