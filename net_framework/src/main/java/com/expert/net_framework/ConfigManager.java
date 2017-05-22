package com.expert.net_framework;

/**
 * author：wangkezhi
 * date: 2017/5/19 10:30
 * email：454366460@qq.com
 * 全局参数
 */

public class ConfigManager {
    /**
     * baseurl
     */
    protected String BASE_URL;

    /**
     * debug模式
     */
    protected boolean DEBUG;

    /**
     * 超时设置
     */
    protected long Time_Out;


    private ConfigManager(String BASE_URL, boolean DEBUG, long time_Out) {
        this.BASE_URL = BASE_URL;
        this.DEBUG = DEBUG;
        Time_Out = time_Out;
    }

    protected static class Builder {
        /**
         * baseurl
         */
        private String BASE_URL;

        /**
         * debug模式
         */
        private boolean DEBUG = true;

        /**
         * 超时设置
         */
        private long Time_Out = 15;


        public Builder setBASE_URL(String BASE_URL) {
            this.BASE_URL = BASE_URL;
            return this;
        }

        public Builder setDEBUG(boolean DEBUG) {
            this.DEBUG = DEBUG;
            return this;
        }

        public Builder setTime_Out(long time_Out) {
            Time_Out = time_Out;
            return this;
        }

        public ConfigManager build() {
            return new ConfigManager(BASE_URL, DEBUG, Time_Out);
        }
    }

}
