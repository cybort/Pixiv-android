package com.example.administrator.essim.response;

public class PixivAccountsResponse {
    private BodyBean body;
    private boolean error;
    private String message;

    public BodyBean getBody() {
        return this.body;
    }

    public void setBody(BodyBean paramBodyBean) {
        this.body = paramBodyBean;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String paramString) {
        this.message = paramString;
    }

    public boolean isError() {
        return this.error;
    }

    public void setError(boolean paramBoolean) {
        this.error = paramBoolean;
    }

    public static class BodyBean {
        private String device_token;
        private String password;
        private String user_account;

        public String getDevice_token() {
            return this.device_token;
        }

        public void setDevice_token(String paramString) {
            this.device_token = paramString;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String paramString) {
            this.password = paramString;
        }

        public String getUser_account() {
            return this.user_account;
        }

        public void setUser_account(String paramString) {
            this.user_account = paramString;
        }
    }
}