package com.example.administrator.essim.models;

import java.util.List;

public class PixivMember {
    public class Response {
        public String name;

        public class Status {
            public String getWorks() {
                return works;
            }

            public void setWorks(String works) {
                this.works = works;
            }

            public String getFavorites() {
                return favorites;
            }

            public void setFavorites(String favorites) {
                this.favorites = favorites;
            }

            public String getFollowing() {
                return following;
            }

            public void setFollowing(String following) {
                this.following = following;
            }

            public String getFriends() {
                return friends;
            }

            public void setFriends(String friends) {
                this.friends = friends;
            }

            public String works;
            public String favorites;
            public String following;
            public String friends;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String id;
        public Status stats;
        public Profile profile;

        public class Profile {
            public String getJob() {
                return job;
            }

            public void setJob(String job) {
                this.job = job;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getHomepage() {
                return homepage;
            }

            public void setHomepage(String homepage) {
                this.homepage = homepage;
            }

            public String job;
            public String introduction;
            public String location;
            public String homepage;
        }
    }

    public List<Response> response;
}
