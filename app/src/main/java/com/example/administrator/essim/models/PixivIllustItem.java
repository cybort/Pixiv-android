package com.example.administrator.essim.models;

import java.util.List;

public class PixivIllustItem {
    public List<Response> response;

    public class Response {
        public String id;
        public String title;
        public MetaData metadata;
        public String page_count;
        public MetaData.Page.ImageUrl image_urls;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPage_count() {
            return page_count;
        }

        public void setPage_count(String page_count) {
            this.page_count = page_count;
        }

        public class MetaData {
            public List<Page> pages;

            public class Page {
                public ImageUrl image_urls;

                public class ImageUrl {
                    public String large;
                    public String px_128x128;
                    public String px_480mw;
                    public String medium;

                    public String getLarge() {
                        return large;
                    }

                    public void setLarge(String large) {
                        this.large = large;
                    }

                    public String getPx_128x128() {
                        return px_128x128;
                    }

                    public void setPx_128x128(String px_128x128) {
                        this.px_128x128 = px_128x128;
                    }

                    public String getPx_480mw() {
                        return px_480mw;
                    }

                    public void setPx_480mw(String px_480mw) {
                        this.px_480mw = px_480mw;
                    }

                    public String getMedium() {
                        return medium;
                    }

                    public void setMedium(String medium) {
                        this.medium = medium;
                    }
                }
            }
        }
    }
}
