/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.util;

/**
 *
 * @author root
 */
public interface BJMConstants {
    
    public static final String TEMP_IMAGE="tempImage";
    public static String EMAIL_REGEX="^(.+)@(.+)$";
    @Deprecated
    public static String ACCESS="Access";
    public static String PW_REGEX="^(?=.*\\d).{8,14}$";
    public static String PHONE_REGEX="\\d{11}";
    public static String USER="User";
    public static String FORUM_COMMENT_IMAGE_MAP="forumCommentImageMap";
    public static String SURVEY_VOTE_IMAGE_MAP="surveyVoteImageMap";
    public static String BLOG_COMMENT_IMAGE_MAP="blogCommentImageMap";
    public static String ACCESS_RESET_FOR_URL="y12uTympxP";
    public static String SURVEY_CREATOR="surveyCreator";
    public static String FORUM_CREATOR="forumCreator";
    public static String SURVEY="Survey";
    public static String VISITOR="Visitor";
    public static final int BLOG_COMMENT_LENGTH=1500;
}
