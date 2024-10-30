package WebtoonConsole.DAO;

import WebtoonConsole.Common.Common;
import WebtoonConsole.Common.GetVal;
import WebtoonConsole.VO.PostVO;
import WebtoonConsole.VO.ReplyVO;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReplyDAO {
    GetVal gv = new GetVal();
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    MemberDAO memberDAO = new MemberDAO();

    public List<ReplyVO> replySelect(int postNum) {
        List<ReplyVO> replyList = new ArrayList<>();
        String query = "SELECT reply_num, reply_content, reply_published_date, reply_like_count, reply_dislike_count, member_num "+
                        "FROM REPLY " +
                        "WHERE post_num = ? ";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, postNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                int replyNum = rs.getInt("reply_num");
                String replyContent = rs.getString("reply_content");
                Date replyPublishedDate = rs.getDate("reply_published_date");
                int replyLikeCount = rs.getInt("reply_like_count");
                int replyDislikeCount = rs.getInt("reply_dislike_count");
                int memberNum = rs.getInt("member_num");
                String memberNickname = getMemberNickname(memberNum);

                ReplyVO replyVO = new ReplyVO(replyNum, replyContent, replyPublishedDate, replyLikeCount, replyDislikeCount, memberNum, memberNickname);
                replyList.add(replyVO);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return replyList;
    }

    private String getReplyContent(int replyNum) {
        String replyContent = null;
        String query = "SELECT reply_content FROM REPLY WHERE reply_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, replyNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                replyContent = rs.getString("member_nickname");
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return replyContent;
    }

    private String getMemberNickname(int memberNum) {
        String memberNickname = null;
        String query = "SELECT member_nickname, member_type_num FROM MEMBER " +
                        "WHERE member_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, memberNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                memberNickname = rs.getString("member_nickname");
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return memberNickname;
    }

    public String getMemberId (int replyNum) {
        String memberId = null;
        String query = "SELECT member_id " +
                        "FROM MEMBER m JOIN REPLY r " +
                        "ON m.member_num = r.member_num " +
                        "WHERE post_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, replyNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                memberId = rs.getString("member_id");
            }
        }catch (Exception e) {
        }finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return memberId;
    }

    public void replyInsert(int postNum, String memberId) {
        String replyContent = gv.getReplyContent();
        Integer memberNum = memberDAO.memberNumSelect(memberId);
        String query = "INSERT INTO REPLY (reply_content, reply_published_date, member_num, post_num) " +
                        "VALUES (?, SYSDATE, ?, ?)";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, replyContent);
            psmt.setInt(2, memberNum);
            psmt.setInt(3, postNum);
            rs = psmt.executeQuery();
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        System.out.println("댓글 작성이 완료되었습니다.");
    }

    public void replyUpdate(int postNum, int replyNum, String memberId) {
        String replyContent = getReplyContent(replyNum);
        String replyUpdateContent = gv.getUpdateReplyContent(replyContent);
        String query = "UPDATE REPLY SET reply_content = ? WHERE reply_num = ?";

        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, replyUpdateContent);
            psmt.setInt(2, replyNum);
            rs = psmt.executeQuery();
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        System.out.println("댓글 수정이 완료되었습니다.");
    }

    public void replyLikeInsert(String memberId, int replyNum, int likeDislike) {
        int memberNum = memberDAO.memberNumSelect(memberId);
        String insertQuery = "INSERT INTO REPLY_EVALUATION VALUES (?, ?, ?) ";
        String updateQuery = "UPDATE REPLY SET reply_like_count =  reply_like_count + 1 WHERE reply_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(insertQuery);
            psmt.setInt(1, memberNum);
            psmt.setInt(2, replyNum);
            psmt.setInt(3, likeDislike);
            rs = psmt.executeQuery();

            conn = Common.getConnection();
            psmt = conn.prepareStatement(updateQuery);
            psmt.setInt(1, replyNum);
            rs = psmt.executeQuery();
        } catch (Exception e) {
            System.out.printf("이미 %s한 댓글입니다.\n", (likeDislike == 0) ? "공감" : "비공감");
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
    }

//    public void replyLikeUpdate(int replyNum) {
//        String query = "UPDATE REPLY SET reply_like_count =  reply_like_count + 1 WHERE reply_num = ?";
//        try {
//            conn = Common.getConnection();
//            psmt = conn.prepareStatement(query);
//            psmt.setInt(1, replyNum);
//            rs = psmt.executeQuery();
//        } catch (Exception e) {
//        } finally {
//            Common.close(rs);
//            Common.close(psmt);
//            Common.close(conn);
//        }
//    }

    public void replyDislikeUpdate(int replyNum) {
        String query = "UPDATE REPLY SET reply_dislike_count =  reply_dislike_count + 1 WHERE reply_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, replyNum);
            rs = psmt.executeQuery();
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
    }


}
