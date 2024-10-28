package WebtoonConsole.VO;

import java.sql.Date;

public class CommentVO {
    private int commentNum;
    private String commentContent;
    private Date commentPublishedDate;
    private int commentLikeCount;
    private int commentDislikeCount;
    private int memberNum;
    private int postNum;

    public int getCommentNum() {
        return commentNum;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public Date getCommentPublishedDate() {
        return commentPublishedDate;
    }

    public int getCommentLikeCount() {
        return commentLikeCount;
    }

    public int getCommentDislikeCount() {
        return commentDislikeCount;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public int getPostNum() {
        return postNum;
    }
}
