package com.example.administrator.essim.response;

public class PixivAccountsResponse {
    private BodyBean body;
    private boolean error;
    private String message;

    public BodyBean getBody() {
        return this.body;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isError() {
        return this.error;
    }

    public void setBody(BodyBean paramBodyBean) {
        this.body = paramBodyBean;
    }

    public void setError(boolean paramBoolean) {
        this.error = paramBoolean;
    }

    public void setMessage(String paramString) {
        this.message = paramString;
    }

    public static class BodyBean {
        private String device_token;
        private String password;
        private String user_account;

        public String getDevice_token() {
            return this.device_token;
        }

        public String getPassword() {
            return this.password;
        }

        public String getUser_account() {
            return this.user_account;
        }

        public void setDevice_token(String paramString) {
            this.device_token = paramString;
        }

        public void setPassword(String paramString) {
            this.password = paramString;
        }

        public void setUser_account(String paramString) {
            this.user_account = paramString;
        }
    }
}