package com.example.administrator.essim.response;

import java.io.Serializable;
import java.util.List;

public class IllustCommentsResponse implements Serializable
{
    private List<CommentsBean> comments;
    private String next_url;
    private int total_comments;

    public List<CommentsBean> getComments()
    {
        return this.comments;
    }

    public String getNext_url()
    {
        return this.next_url;
    }

    public int getTotal_comments()
    {
        return this.total_comments;
    }

    public void setComments(List<CommentsBean> paramList)
    {
        this.comments = paramList;
    }

    public void setNext_url(String paramString)
    {
        this.next_url = paramString;
    }

    public void setTotal_comments(int paramInt)
    {
        this.total_comments = paramInt;
    }

    public static class CommentsBean
    {
        private String comment;
        private String date;
        private int id;
        private ParentCommentBean parent_comment;
        private UserBean user;

        public String getComment()
        {
            return this.comment;
        }

        public String getDate()
        {
            return this.date;
        }

        public int getId()
        {
            return this.id;
        }

        public ParentCommentBean getParent_comment()
        {
            return this.parent_comment;
        }

        public UserBean getUser()
        {
            return this.user;
        }

        public void setComment(String paramString)
        {
            this.comment = paramString;
        }

        public void setDate(String paramString)
        {
            this.date = paramString;
        }

        public void setId(int paramInt)
        {
            this.id = paramInt;
        }

        public void setParent_comment(ParentCommentBean paramParentCommentBean)
        {
            this.parent_comment = paramParentCommentBean;
        }

        public void setUser(UserBean paramUserBean)
        {
            this.user = paramUserBean;
        }

        public static class ParentCommentBean
        {
            private String comment;
            private String date;
            private int id;
            private UserBeanX user;

            public String getComment()
            {
                return this.comment;
            }

            public String getDate()
            {
                return this.date;
            }

            public int getId()
            {
                return this.id;
            }

            public UserBeanX getUser()
            {
                return this.user;
            }

            public void setComment(String paramString)
            {
                this.comment = paramString;
            }

            public void setDate(String paramString)
            {
                this.date = paramString;
            }

            public void setId(int paramInt)
            {
                this.id = paramInt;
            }

            public void setUser(UserBeanX paramUserBeanX)
            {
                this.user = paramUserBeanX;
            }

            public static class UserBeanX
            {
                private String account;
                private int id;
                private String name;
                private ProfileImageUrlsBeanX profile_image_urls;

                public String getAccount()
                {
                    return this.account;
                }

                public int getId()
                {
                    return this.id;
                }

                public String getName()
                {
                    return this.name;
                }

                public ProfileImageUrlsBeanX getProfile_image_urls()
                {
                    return this.profile_image_urls;
                }

                public void setAccount(String paramString)
                {
                    this.account = paramString;
                }

                public void setId(int paramInt)
                {
                    this.id = paramInt;
                }

                public void setName(String paramString)
                {
                    this.name = paramString;
                }

                public void setProfile_image_urls(ProfileImageUrlsBeanX paramProfileImageUrlsBeanX)
                {
                    this.profile_image_urls = paramProfileImageUrlsBeanX;
                }

                public static class ProfileImageUrlsBeanX
                {
                    private String medium;

                    public String getMedium()
                    {
                        return this.medium;
                    }

                    public void setMedium(String paramString)
                    {
                        this.medium = paramString;
                    }
                }
            }
        }

        public static class UserBean
        {
            private String account;
            private int id;
            private String name;
            private ProfileImageUrlsBean profile_image_urls;

            public String getAccount()
            {
                return this.account;
            }

            public int getId()
            {
                return this.id;
            }

            public String getName()
            {
                return this.name;
            }

            public ProfileImageUrlsBean getProfile_image_urls()
            {
                return this.profile_image_urls;
            }

            public void setAccount(String paramString)
            {
                this.account = paramString;
            }

            public void setId(int paramInt)
            {
                this.id = paramInt;
            }

            public void setName(String paramString)
            {
                this.name = paramString;
            }

            public void setProfile_image_urls(ProfileImageUrlsBean paramProfileImageUrlsBean)
            {
                this.profile_image_urls = paramProfileImageUrlsBean;
            }

            public static class ProfileImageUrlsBean
            {
                private String medium;

                public String getMedium()
                {
                    return this.medium;
                }

                public void setMedium(String paramString)
                {
                    this.medium = paramString;
                }
            }
        }
    }
}