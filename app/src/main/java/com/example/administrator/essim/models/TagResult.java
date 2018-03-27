package com.example.administrator.essim.models;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class TagResult {
    public List<Response> response;
    public class Response
    {
        public List<String> tags;
        public User user;

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String created_time;

        public Stats stats;

        public class Stats
        {
            public String getScored_count() {
                return scored_count;
            }

            public void setScored_count(String scored_count) {
                this.scored_count = scored_count;
            }

            public String scored_count;

            public String getViews_count() {
                return views_count;
            }

            public void setViews_count(String views_count) {
                this.views_count = views_count;
            }

            public String views_count;
        }
        public class User
        {
            public String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String id;
        }
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String title;
        public class ImageUrl
        {
            public String getPx_480mw() {
                return px_480mw;
            }

            public void setPx_480mw(String px_480mw) {
                this.px_480mw = px_480mw;
            }

            public String px_480mw;
        }

        public ImageUrl image_urls;

        public String width;

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String height;
    }
}
