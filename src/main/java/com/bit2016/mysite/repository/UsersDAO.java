package com.bit2016.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.bit2016.mysite.vo.Users;

@Repository
public class UsersDAO {

	public void insert(Users users) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DAOConnection.connection();

			String sql = "insert into users values(user_SEQ.nextval, ?,?,?,?)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, users.getName());
			stmt.setString(2, users.getEmail());
			stmt.setString(3, users.getPassword());
			stmt.setString(4, users.getGender());
			stmt.executeUpdate();//
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 로그인시
	public Users selectNo(String email, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Users users = null;
		try {
			conn = DAOConnection.connection();
			String sql = "select no,name  from users where email=? and password=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, password);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				users = new Users();
				users.setName(name);
				users.setNo(no);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return users;
	}

	// 업데이트시
	public Users selectNo(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Users users = null;
		try {
			conn = DAOConnection.connection();
			String sql = "select no,name,email,password,gender  from users where no = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				no = rs.getLong(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);
				String gender = rs.getString(5);

				users = new Users();
				users.setName(name);
				users.setNo(no);
				users.setEmail(email);
				users.setGender(gender);
				users.setPassword(password);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return users;
	} // 업데이트시

	public Users selectNo(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Users users = null;
		try {
			conn = DAOConnection.connection();
			String sql = "select no from users where email = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				users = new Users();
				users.setNo(rs.getLong(1));
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return users;
	}

	public void update(Users users) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql;
		try {
			conn = DAOConnection.connection();
			if (users.getPassword() == null || "".equals(users.getPassword())) {
				sql = "update users set name =?, gender = ? where no =?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, users.getName());
				stmt.setString(2, users.getGender());
				stmt.setLong(3, users.getNo());

			} else {
				sql = "update users set name =?, password = ? , gender = ? where no =?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, users.getName());
				stmt.setString(2, users.getPassword());
				stmt.setString(3, users.getGender());
				stmt.setLong(4, users.getNo());

			}

			stmt.executeUpdate();//
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
