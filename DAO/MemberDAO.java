package WebtoonConsole.DAO;

import WebtoonConsole.Common.Common;
import WebtoonConsole.Common.GetVal;
import WebtoonConsole.VO.MemberVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    GetVal gv = new GetVal();
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;

    public List<MemberVO> memberSelect(String memberID, String memberPW) {
        List<MemberVO> list = new ArrayList<>();
        String query = "SELECT m.*, mt.member_type_name "+
                        "FROM MEMBER m JOIN MEMBER_TYPE mt " +
                            "ON m.member_type_num = mt.member_type_num " +
                        "WHERE m.member_id = ? AND m.member_pw = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, memberID);
            psmt.setString(2, memberPW);
            rs = psmt.executeQuery();
            while (rs.next()) {
                int memberNum = rs.getInt("member_num");
                String memberId = rs.getString("member_id");
                String memberPw = rs.getString("member_pw");
                String memberEmail = rs.getString("member_email");
                Date memberBirth = rs.getDate("member_birth");
                String memberNickname = rs.getString("member_nickname");
                int memberExist = rs.getInt("member_exist");
                int memberTypeNum = rs.getInt("member_type_num");
                MemberVO memberVO = new MemberVO(memberNum, memberId, memberPw, memberEmail, memberBirth, memberNickname, memberExist, memberTypeNum);
                list.add(memberVO);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return list;
    }

    public List<String> memberInfoSelect(String memberID) {
        List<String> favoriteGenre = memberFavoriteGenreSelect(memberID);
        List<String> memberInfo = new ArrayList<>();
        String memberQuery = "SELECT m.*, mt.member_type_name "+
                            "FROM MEMBER m JOIN MEMBER_TYPE mt " +
                            "ON m.member_type_num = mt.member_type_num " +
                            "WHERE m.member_id = ? ";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(memberQuery);
            psmt.setString(1, memberID);
            rs = psmt.executeQuery();
            while (rs.next()) {
                String memberId = rs.getString("member_id");
                String memberPw = rs.getString("member_pw");
                String memberEmail = rs.getString("member_email");
                Date memberBirth = rs.getDate("member_birth");
                String memberNickname = rs.getString("member_nickname");
                int memberTypeNum = rs.getInt("member_type_num");
                memberInfo.add(memberId);
                memberInfo.add(memberNickname);
                memberInfo.add(memberPw);
                memberInfo.add(memberEmail);
                memberInfo.add(String.valueOf(memberBirth));
            }
            for(String e : favoriteGenre) {
                memberInfo.add(e);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return memberInfo;
    }

    public List<String> memberFavoriteGenreSelect(String memberId) {
        List<String> favoriteGenre = new ArrayList<>();
        String genreQuery = "SELECT g.genre_name "+
                "FROM MEMBER m " +
                "JOIN FAVORITE_GENRE fg ON m.member_num = fg.member_num " +
                "JOIN GENRE g ON fg.genre_num = g.genre_num " +
                "WHERE m.member_id = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(genreQuery);
            psmt.setString(1, memberId);
            rs = psmt.executeQuery();
            while(rs.next()) {
                String genreName = rs.getString("genre_name");
                favoriteGenre.add(genreName);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return favoriteGenre;
    }

    public void memberInsert() {
        int memberResult = 0;
        Integer memberNum = maxMemberNumSelect();
        List<String> memberInfo = gv.memberInfo(); //{memberID, memberPW, memberEmail, memberBirth, memberNickname}
        String memberQuery = "INSERT INTO MEMBER VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, SYSDATE, 0, 1)";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(memberQuery);
            psmt.setInt(1, memberNum);
            psmt.setString(2, memberInfo.get(0));  //member_id
            psmt.setString(3, memberInfo.get(1));  //member_pw
            psmt.setString(4, memberInfo.get(2));  //member_email
            psmt.setString(5, memberInfo.get(3));  //member_birth
            psmt.setString(6, memberInfo.get(4));  //member_nickname
            memberResult = psmt.executeUpdate();

            for (int i = 5; i < memberInfo.size(); i++) {
                String favoriteGenreQuery = "INSERT INTO FAVORITE_GENRE VALUES (?, ?)";
                conn = Common.getConnection();
                psmt = conn.prepareStatement(favoriteGenreQuery);
                psmt.setInt(1, memberNum);
                psmt.setInt(2, Integer.parseInt(memberInfo.get(i)));
                psmt.executeUpdate();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            Common.close(psmt);
            Common.close(conn);
        }
        if (memberResult > 0) {
            System.out.println("회원가입이 완료되었습니다.");
        } else {
            System.out.println("회원가입에 실패했습니다.");
        }
    }

    public Integer maxMemberNumSelect() {
        int memberNum = 0;
        String query = "SELECT MAX(member_num) FROM MEMBER";
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next()) {
                memberNum = rs.getInt(1);
            }
        } catch (Exception e) {
        } finally  {
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        }
        return memberNum + 1;
    }

    public boolean memberIdSelect(String id) {
        boolean exists = false;
        String query = "SELECT * FROM MEMBER WHERE member_id = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, id);
            rs = psmt.executeQuery();

            if (rs.next()) exists = true;
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return exists;
    }

    public Integer memberNumSelect(String memberId) {
        int memberNum = 0;
        String query = "SELECT member_num FROM MEMBER WHERE member_id = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, memberId);
            rs = psmt.executeQuery();

            while(rs.next()) {
                memberNum = rs.getInt("member_num");
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return memberNum;
    }

    public void memberPwUpdate (String memberId, String memberPw) {
        int rs = 0;
        String query = "UPDATE MEMBER SET member_pw = ? WHERE member_id = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, memberPw);
            psmt.setString(2, memberId);
            rs = psmt.executeUpdate();

            if (rs > 0) {
                System.out.println("비밀번호 변경 완료");
            } else {
                System.out.println("비밀번호 변경 실패");
            }
        } catch (Exception e) {
        } finally {
            Common.close(psmt);
            Common.close(conn);
        }
    }

    public void memberFavoriteGenreUpdate(String memberId, List<Integer> favoriteGenreNums) {
        int memberNum = memberNumSelect(memberId);
        String deleteFavoriteGenre = "DELETE FROM FAVORITE_GENRE WHERE member_num = ?";
        String insertFavoriteGenre = "INSERT INTO FAVORITE_GENRE VALUES (?, ?)";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(deleteFavoriteGenre);
            psmt.setInt(1, memberNum);
            rs = psmt.executeQuery();
            for (int e : favoriteGenreNums) {
                conn = Common.getConnection();
                psmt = conn.prepareStatement(insertFavoriteGenre);
                psmt.setInt(1, memberNum);
                psmt.setInt(2, e);
                rs = psmt.executeQuery();
            }
            System.out.println("선호장르 수정 완료");
        } catch (Exception e) {
            System.out.println("선호장르 수정 실패");
        } finally {
            Common.close(psmt);
            Common.close(conn);
        }
    }

    public void deleteMember(String memberId, int existNum) {
        String query = "UPDATE MEMBER SET member_exist = ? WHERE member_id = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, existNum);
            psmt.setString(2, memberId);
            rs = psmt.executeQuery();
            if (existNum == 1) {
                System.out.println("회원탈퇴 완료. 회원님의 모든 정보는 3일 뒤에 완전 삭제됩니다.");
                System.out.println("보고싶을거에요.. " + memberId + "님...😢");
            }
        } catch (Exception e) {
            System.out.println("회원탈퇴 실패");
        } finally {
            Common.close(psmt);
            Common.close(conn);
        }

    }
}
