package WebtoonConsole.VO;

import java.sql.Date;

public class ReplyVO {
    private int replyNum;
    private String replyContent;
    private Date replyPublishedDate;
    private int replyLikeCount;
    private int replyDislikeCount;
    private int memberNum;
    private String memberNickname;
    private int postNum;

    public ReplyVO(int replyNum, String replyContent, Date replyPublishedDate, int replyLikeCount, int replyDislikeCount, int memberNum, String memberNickname) {
        this.replyNum = replyNum;
        this.replyContent = replyContent;
        this.replyPublishedDate = replyPublishedDate;
        this.replyLikeCount = replyLikeCount;
        this.replyDislikeCount = replyDislikeCount;
        this.memberNum = memberNum;
        this.memberNickname = memberNickname;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public Date getReplyPublishedDate() {
        return replyPublishedDate;
    }

    public int getReplyLikeCount() {
        return replyLikeCount;
    }

    public int getReplyDislikeCount() {
        return replyDislikeCount;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public int getPostNum() {
        return postNum;
    }
}