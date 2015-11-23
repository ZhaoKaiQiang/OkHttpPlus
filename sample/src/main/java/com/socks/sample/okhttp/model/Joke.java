package com.socks.sample.okhttp.model;

import java.io.Serializable;

public class Joke implements Serializable {

    public static final String URL_JOKE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan" +
            ".get_duan_comments&page=";

    private String comment_ID;
    private String comment_post_ID;
    private String comment_author;
    private String comment_author_email;
    private String comment_author_url;
    private String comment_author_IP;
    private String comment_date;
    private String comment_date_gmt;
    private String comment_content;
    private String text_content;
    private String comment_agent;
    private String vote_positive;
    private String vote_negative;

    public Joke() {
    }

    public static String getRequestUrl(int page) {
        return URL_JOKE + page;
    }

    @Override
    public String toString() {
        return "Joke{" +
                "comment_ID='" + comment_ID + '\'' +
                ", comment_post_ID='" + comment_post_ID + '\'' +
                ", comment_author='" + comment_author + '\'' +
                ", comment_author_email='" + comment_author_email + '\'' +
                ", comment_author_url='" + comment_author_url + '\'' +
                ", comment_author_IP='" + comment_author_IP + '\'' +
                ", comment_date='" + comment_date + '\'' +
                ", comment_date_gmt='" + comment_date_gmt + '\'' +
                ", comment_content='" + comment_content + '\'' +
                ", text_content='" + text_content + '\'' +
                ", comment_agent='" + comment_agent + '\'' +
                ", vote_positive='" + vote_positive + '\'' +
                ", vote_negative='" + vote_negative + '\'' +
                '}';
    }
}
