package com.example.administrator.essim.response;

import java.util.List;

public class UgoiraMetadataResponse {
    private UgoiraMetadataBean ugoira_metadata;

    public UgoiraMetadataBean getUgoira_metadata() {
        return this.ugoira_metadata;
    }

    public void setUgoira_metadata(UgoiraMetadataBean paramUgoiraMetadataBean) {
        this.ugoira_metadata = paramUgoiraMetadataBean;
    }

    public static class UgoiraMetadataBean {
        private List<FramesBean> frames;
        private ZipUrlsBean zip_urls;

        public List<FramesBean> getFrames() {
            return this.frames;
        }

        public void setFrames(List<FramesBean> paramList) {
            this.frames = paramList;
        }

        public ZipUrlsBean getZip_urls() {
            return this.zip_urls;
        }

        public void setZip_urls(ZipUrlsBean paramZipUrlsBean) {
            this.zip_urls = paramZipUrlsBean;
        }

        public static class FramesBean {
            private int delay;
            private String file;

            public int getDelay() {
                return this.delay;
            }

            public void setDelay(int paramInt) {
                this.delay = paramInt;
            }

            public String getFile() {
                return this.file;
            }

            public void setFile(String paramString) {
                this.file = paramString;
            }
        }

        public static class ZipUrlsBean {
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