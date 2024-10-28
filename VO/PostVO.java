package WebtoonConsole.VO;

import java.sql.Date;

public class PostVO {
    private int postNum;
    private String postTitle;
    private String postContent;
    private Date postPublishedDate;
    private int memberNum;
    private int boardNum;

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

    public int getMemberNum() {
        return memberNum;
    }

    public int getBoardNum() {
        return boardNum;
    }
}
