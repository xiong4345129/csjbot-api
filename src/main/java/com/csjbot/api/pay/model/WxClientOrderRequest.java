package com.csjbot.api.pay.model;

import com.csjbot.api.pay.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class WxClientOrderRequest {
    private String id;
    private Data data;

    public String getId() {
        return id;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private String orderPseudoNo;
        private LocalDateTime orderTime;
        private List<OrderItem> orderList;
        private String orderDesc;

        private String robotUid;
        private String robotModel;
        private String venderCode;
        private String venderUser;

        public String getOrderPseudoNo() {
            return orderPseudoNo;
        }

        public LocalDateTime getOrderTime() {
            return orderTime;
        }

        public List<OrderItem> getOrderList() {
            return orderList;
        }

        public String getOrderDesc() { return orderDesc; }

        public String getRobotUid() {
            return robotUid;
        }

        public String getRobotModel() {
            return robotModel;
        }

        public String getVenderCode() {
            return venderCode;
        }

        public String getVenderUser() {
            return venderUser;
        }
    }


    // public static void main(String[] args) {
    //     String testJson =
    //         "{\n" +
    //             "  \"id\": \"1490854436923-ROBOabcdefg-jl3HDd\",\n" +
    //             "  \"data\": {\n" +
    //             "    \"orderPseudoNo\": \"201703291745-12\",\n" +
    //             "    \"orderTime\": \"2017-02-24T10:51:41\",\n" +
    //             "    \"orderList\": [\n" +
    //             "      {\n" +
    //             "        \"objectId\": \"ZHY00001\",\n" +
    //             "        \"amount\": 3,\n" +
    //             "        \"state\": \"REGULAR\"\n" +
    //             "      },\n" +
    //             "      {\n" +
    //             "        \"objectId\": \"ZHY00007\",\n" +
    //             "        \"amount\": 1,\n" +
    //             "        \"state\": \"REGULAR\"\n" +
    //             "      }\n" +
    //             "    ],\n" +
    //             "    \"robotUid\": \"ROBOabcdefg\",\n" +
    //             "    \"robotModel\": \"yingbin\",\n" +
    //             "    \"venderCode\": \"ZHY\",\n" +
    //             "    \"venderUser\": \"12345678901\"\n" +
    //             "  }\n" +
    //             "}";
    //     MediaTypeParser mapper = new JacksonMapper();
    //     WxClientOrderRequest req = mapper.deserialize(testJson, WxClientOrderRequest.class, "JSON");
    //     System.out.println(req==null);
    // }
}
