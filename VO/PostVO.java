package WebtoonConsole.VO;

import java.sql.Date;

public class PostVO {
    private int postNum;
    private String postTitle;
    private String postContent;
    private Date postPublishedDate;
    private int postVisit;
    private int memberNum;
    private String memberNickname;
    private int boardNum;

    public PostVO(int postNum, String postTitle, String postContent, Date postPublishedDate, int postVisit, int memberNum, String memberNickname) {
        this.postNum = postNum;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postPublishedDate = postPublishedDate;
        this.postVisit = postVisit;
        this.memberNum = memberNum;
        this.memberNickname = memberNickname;
    }

    public int getPostNum() {
        return postNum;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public Date getPostPublishedDate() {
        return postPublishedDate;
    }

    public int getPostVisit() {
        return postVisit;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public int getBoardNum() {
        return boardNum;
    }
}
