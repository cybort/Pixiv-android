package com.example.administrator.essim.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CollectionResponse {

    public List<Body> body;

    public class Body {
        public String id;
        public String title;
        public String introduction;
        public List<Illust> illusts;

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

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public class Illust {
            public String illust_id;
            public String illust_user_id;
            public String illust_title;
            public String illust_create_date;
            public String user_name;
            public String user_icon;
            public Url url;

            public String getIllust_id() {
                return illust_id;
            }

            public void setIllust_id(String illust_id) {
                this.illust_id = illust_id;
            }

            public String getIllust_user_id() {
                return illust_user_id;
            }

            public void setIllust_user_id(String illust_user_id) {
                this.illust_user_id = illust_user_id;
            }

            public String getIllust_title() {
                return illust_title;
            }

            public void setIllust_title(String illust_title) {
                this.illust_title = illust_title;
            }

            public String getIllust_create_date() {
                return illust_create_date;
            }

            public void setIllust_create_date(String illust_create_date) {
                this.illust_create_date = illust_create_date;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public class Url {
                @SerializedName("1200x1200")
                private String _$1200x1200;
                @SerializedName("768x1200")
                private String _$768x1200;

                public String get_$1200x1200() {
                    return _$1200x1200;
                }

                public void set_$1200x1200(String _$1200x1200) {
                    this._$1200x1200 = _$1200x1200;
                }

                public String get_$768x1200() {
                    return _$768x1200;
                }

                public void set_$768x1200(String _$768x1200) {
                    this._$768x1200 = _$768x1200;
                }
            }
        }
    }
}
