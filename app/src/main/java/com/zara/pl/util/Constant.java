package com.zara.pl.util;


public class Constant {

    public class API {
        public static final String HOST = "https://galstyanzara.github.io";
        public static final String PRODUCT_LIST = HOST + "/pl/products.json";
        public static final String PRODUCT_ITEM = HOST + "/pl/products/"; // id
        public static final String PRODUCT_ITEM_POSTFIX = "/details.json";
        public static final String PRODUCT_ITEM_STATIC_IMAGE = "https://s3-eu-west-1.amazonaws.com/developer-application-test/images/3.jpg";
    }



    public class Extra {
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final String URL = "URL";
        public static final String POST_ENTITY = "POST_ENTITY";
        public static final String REQUEST_TYPE = "REQUEST_TYPE";
        public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";
        public static final String PRODUCT = "PRODUCT";
    }

    public class Symbol {
        public static final String ASTERISK = "*";
        public static final String NEW_LINE = "\n";
        public static final String SPACE = " ";
        public static final String NULL = "";
        public static final String COLON = ":";
        public static final String COMMA = ",";
        public static final String SLASH = "/";
        public static final String DOT = ".";
        public static final String UNDERLINE = "_";
        public static final String DASH = "-";
        public static final String AT = "@";
        public static final String AMPERSAND = "&";
    }

    public class Util {
        public static final String UTF_8 = "UTF-8";
    }
    public class RequestType {
        public static final int PRODUCT_LIST = 1;
        public static final int PRODUCT_ITEM = 2;
    }

    public class RequestMethod {
        public static final String POST = "POST";
        public static final String GET = "GET";
        public static final String PUT = "PUT";
    }

}