package org.example.profiling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LPS {
    private String timestamp;
    private String event;
    private UserInfo userInfo;
    private MethodInfo methodInfo;

    @Data
    public static class UserInfo {
        private String userId;
        private String userType;  // READER, WRITER, PREMIUM
        private int readCount;
        private int writeCount;
        private double avgProductPrice;
    }

    @Data
    public static class MethodInfo {
        private String methodName;
        private String operationType;
        private String result;
    }

    public static class Builder {
        private LPS lps;

        public Builder() {
            lps = new LPS();
            lps.userInfo = new UserInfo();
            lps.methodInfo = new MethodInfo();
        }

        public Builder withTimestamp(String timestamp) {
            lps.timestamp = timestamp;
            return this;
        }

        public Builder withEvent(String event) {
            lps.event = event;
            return this;
        }

        public Builder withUserId(String userId) {
            lps.userInfo.userId = userId;
            return this;
        }

        public Builder withUserType(String userType) {
            lps.userInfo.userType = userType;
            return this;
        }

        public Builder withMethodInfo(String methodName, String operationType, String result) {
            lps.methodInfo.methodName = methodName;
            lps.methodInfo.operationType = operationType;
            lps.methodInfo.result = result;
            return this;
        }

        public Builder withOperationCounts(int readCount, int writeCount) {
            lps.userInfo.readCount = readCount;
            lps.userInfo.writeCount = writeCount;
            return this;
        }

        public Builder withAvgProductPrice(double avgPrice) {
            lps.userInfo.avgProductPrice = avgPrice;
            return this;
        }

        public LPS build() {
            return lps;
        }
    }
}