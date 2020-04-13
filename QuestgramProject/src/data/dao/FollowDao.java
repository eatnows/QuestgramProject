package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.dto.FollowDto;
import data.dto.UserDto;
import mysql.db.DbConnect;

public class FollowDao {
	DbConnect db = new DbConnect();
	
	// �ȷο� (�̿ϼ�: �μ�Ʈ�� ������ �ߺ����� ���� �ɾ�� ��)
	public void insertFollow(String add, String target) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO follow VALUES(null, ?, ?, now())";
		
		conn = db.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, add);
			pstmt.setString(2, target);
			
			pstmt.execute();
			
		} catch (SQLException e) {
			System.out.println("insert follow error : " + e.getMessage());
		} finally {
			db.dbClose(pstmt, conn);
		}
		
	}
	
	// �ȷο� ����
	public void deleteFollow(String add, String target) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM follow WHERE add_user_id = ? AND target_user_id = ?";
		
		conn = db.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, add);
			pstmt.setString(2, target);
			
			pstmt.execute();
			
		} catch (SQLException e) {
			System.out.println("delete follow error : " + e.getMessage());
		} finally {
			db.dbClose(pstmt, conn);
		}
	}
	
	// �ȷο� ����Ʈ ��������
	public List<FollowDto> getFollowers(String id) {
		List<FollowDto> list = new ArrayList<FollowDto>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT user.id, user.name, user.nickname, user.profile_img, follow.created_at "
				+ "FROM user "
				+ "JOIN (SELECT add_user_id, created_at FROM follow WHERE target_user_id = ?) follow "
				+ "ON user.id = follow.add_user_id;";
		
		conn = db.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				FollowDto dto = new FollowDto();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setNickname(rs.getString("nickname"));
				dto.setProfile_img(rs.getString("profile_img"));
				// follow, user ���̺� �� �� Į������ created_at���� ���Ƽ�
				// follow�� dto �������� followed_at ���� �ۼ���
				dto.setFollowed_at(rs.getTimestamp("created_at"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("get followers method error : " + e.getMessage());
		} finally {
			db.dbClose(rs, pstmt, conn);
		}
		return list;
	}
	
	// �ȷ��� ����Ʈ ��������
	public List<FollowDto> getFollowings(String id) {
		List<FollowDto> list = new ArrayList<FollowDto>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT user.id, user.name, user.nickname, user.profile_img, follow.created_at "
				+ "FROM user "
				+ "JOIN (SELECT target_user_id, created_at FROM follow WHERE add_user_id = ?) follow "
				+ "ON user.id = follow.target_user_id;";
		
		conn = db.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				FollowDto dto = new FollowDto();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setNickname(rs.getString("nickname"));
				dto.setProfile_img(rs.getString("profile_img"));
				// follow, user ���̺� �� �� Į������ created_at���� ���Ƽ�
				// follow�� dto �������� followed_at ���� �ۼ���
				dto.setFollowed_at(rs.getTimestamp("created_at"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("get followers method error : " + e.getMessage());
		} finally {
			db.dbClose(rs, pstmt, conn);
		}
		return list;
	}
}