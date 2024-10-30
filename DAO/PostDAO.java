package WebtoonConsole.DAO;

import WebtoonConsole.Common.Common;
import WebtoonConsole.Common.GetVal;
import WebtoonConsole.VO.PostVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    GetVal gv = new GetVal();
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;

    public List<PostVO> noticeSelect(Integer boardNum) {
        List<PostVO> noticeList = new ArrayList<>();
        String query = "SELECT post_num, post_title, post_content, post_published_date, post_visit, member_num "+
                        "FROM POST " +
                        "WHERE board_num = ? " +
                        "ORDER BY post_published_date ";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, boardNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                int postNum = rs.getInt("post_num");
                String postTitle = rs.getString("post_title");
                String postContent = rs.getString("post_content");
                Date postPublishedDate = rs.getDate("post_published_date");
                int postVisit = rs.getInt("post_visit");
                int memberNum = rs.getInt("member_num");

                PostVO postVO = new PostVO(postNum, postTitle, postContent, postPublishedDate, postVisit, memberNum);
                noticeList.add(postVO);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return noticeList;
    }

    public String[] viewPostContent(int postNum) {
        String postContent = null;
        String postTitle = null;
        String selectQuery = "SELECT post_title, post_content FROM POST WHERE post_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(selectQuery);
            psmt.setInt(1, postNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                postTitle = rs.getString("post_title");
                postContent = rs.getString("post_content");
            }
            String updateQuery = "UPDATE POST SET post_visit = post_visit + 1 WHERE post_num = ?";
            conn = Common.getConnection();
            psmt = conn.prepareStatement(updateQuery);
            psmt.setInt(1, postNum);
            rs = psmt.executeQuery();
        }catch (Exception e) {
        }finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return new String[] {postTitle, postContent};
    }

    public String getPostTitle(int postNum) {
        String postTitle = null;
        String query = "SELECT post_title FROM POST WHERE post_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, postNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                postTitle = rs.getString("post_title");
            }
        }catch (Exception e) {
        }finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return postTitle;
    }

    public String getPostContent(int postNum) {
        String postContent = null;
        String query = "SELECT post_content FROM POST WHERE post_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, postNum);
            rs = psmt.executeQuery();
            while (rs.next()) {
                postContent = rs.getString("post_content");
            }
        }catch (Exception e) {
        }finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return postContent;
    }

    public String getMemberNickname(int memberNum) {
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

    public String getMemberId (int postNum) {
        String memberId = null;
        String query = "SELECT member_id " +
                        "FROM MEMBER m JOIN POST p " +
                        "ON m.member_num = p.member_num " +
                        "WHERE post_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, postNum);
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

    public Integer getMemberNum (String memberId) {
        int memberNum = 0;
        String query = "SELECT member_num FROM MEMBER WHERE member_id = ? ";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, memberId);
            rs = psmt.executeQuery();
            while (rs.next()) {
                memberNum = rs.getInt("member_num");
            }
        }catch (Exception e) {
        }finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return memberNum;
    }

    public void postInsert(String postTitle, String postContent, String memberId, Integer boardNum){
        int rs = 0;
        Integer memberNum = getMemberNum(memberId);
        String query = "INSERT INTO POST (post_title, post_content, post_published_date, member_num, board_num) " +
                        "VALUES (?, ?, SYSDATE, ?, ?)";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, postTitle);
            psmt.setString(2, postContent);
            psmt.setInt(3, memberNum);
            psmt.setInt(4, boardNum);
            rs = psmt.executeUpdate();
            if (rs > 0) {
                System.out.println("게시글 작성이 완료되었습니다.");
            } else {
                System.out.println("게시글 작성에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(psmt);
            Common.close(conn);
        }
    }

    public void postUpdate(int postNum) {
        String postTitle = getPostTitle(postNum);
        String postContent = getPostContent(postNum);
        String[] postUpdate = gv.printPostUpdate(postTitle, postContent);  // 네/아니오, updateTitle, updateContent
        if (postUpdate[0].equals("1")) {
            String query = "UPDATE POST SET post_title = ? , post_content = ? WHERE post_num = ?";
            try {
                conn = Common.getConnection();
                psmt = conn.prepareStatement(query);
                psmt.setString(1, postUpdate[1]);
                psmt.setString(2, postUpdate[2]);
                psmt.setInt(3, postNum);
                rs = psmt.executeQuery();
            } catch (Exception e) {
                System.out.println("수정 내용을 잘못 입력하셨습니다.");
            } finally {
                Common.close(rs);
                Common.close(psmt);
                Common.close(conn);
            }
        }
    }

    public void postDelete(int postNum) {
        int rs = 0;
        String query = "DELETE FROM POST WHERE post_num = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, postNum);
            rs = psmt.executeUpdate();
            if(rs > 0) {
                System.out.println("삭제가 완료되었습니다.");
            }
        } catch (Exception e) {
        } finally {
            Common.close(psmt);
            Common.close(conn);
        }
    }
}
