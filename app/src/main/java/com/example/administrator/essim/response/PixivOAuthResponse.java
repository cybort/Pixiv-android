package com.example.administrator.essim.response;

public class PixivOAuthResponse {
    private ResponseBean response;

    public ResponseBean getResponse() {
        return this.response;
    }

    public void setResponse(ResponseBean paramResponseBean) {
        this.response = paramResponseBean;
    }

    public static class ResponseBean {
        private String access_token;
        private String device_token;
        private int expires_in;
        private String refresh_token;
        private String scope;
        private String token_type;
        private UserBean user;

        public String getAccess_token() {
            return this.access_token;
        }

        public void setAccess_token(String paramString) {
            this.access_token = paramString;
        }

        public String getDevice_token() {
            return this.device_token;
        }

        public void setDevice_token(String paramString) {
            this.device_token = paramString;
        }

        public int getExpires_in() {
            return this.expires_in;
        }

        public void setExpires_in(int paramInt) {
            this.expires_in = paramInt;
        }

        public String getRefresh_token() {
            return this.refresh_token;
        }

        public void setRefresh_token(String paramString) {
            this.refresh_token = paramString;
        }

        public String getScope() {
            return this.scope;
        }

        public void setScope(String paramString) {
            this.scope = paramString;
        }

        public String getToken_type() {
            return this.token_type;
        }

        public void setToken_type(String paramString) {
            this.token_type = paramString;
        }

        public UserBean getUser() {
            return this.user;
        }

        public void setUser(UserBean paramUserBean) {
            this.user = paramUserBean;
        }

        public static class UserBean {
            private String account;
            private int id;
            private boolean is_mail_authorized;
            private boolean is_premium;
            private String mail_address;
            private String name;
            private ProfileImageUrlsBean profile_image_urls;
            private int x_restrict;

            public String getAccount() {
                return this.account;
            }

            public void setAccount(String paramString) {
                this.account = paramString;
            }

            public int getId() {
                return this.id;
            }

            public void setId(int paramString) {
                this.id = paramString;
            }

            public String getMail_address() {
                return this.mail_address;
            }

            public void setMail_address(String paramString) {
                this.mail_address = paramString;
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

            public int getX_restrict() {
                return this.x_restrict;
            }

            public void setX_restrict(int paramInt) {
                this.x_restrict = paramInt;
            }

            public boolean isIs_mail_authorized() {
                return this.is_mail_authorized;
            }

            public void setIs_mail_authorized(boolean paramBoolean) {
                this.is_mail_authorized = paramBoolean;
            }

            public boolean isIs_premium() {
                return this.is_premium;
            }

            public void setIs_premium(boolean paramBoolean) {
                this.is_premium = paramBoolean;
            }

            public static class ProfileImageUrlsBean {
                private String px_16x16;
                private String px_170x170;
                private String px_50x50;

                public String getPx_16x16() {
                    return this.px_16x16;
                }

                public void setPx_16x16(String paramString) {
                    this.px_16x16 = paramString;
                }

                public String getPx_170x170() {
                    return this.px_170x170;
                }

                public void setPx_170x170(String paramString) {
                    this.px_170x170 = paramString;
                }

                public String getPx_50x50() {
                    return this.px_50x50;
                }

                public void setPx_50x50(String paramString) {
                    this.px_50x50 = paramString;
                }
            }
        }
    }
}