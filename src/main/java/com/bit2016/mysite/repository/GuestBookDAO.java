package com.bit2016.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.bit2016.mysite.vo.GuestBook;

@Repository
public class GuestBookDAO {

	public void insert(GuestBook guestBook) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DAOConnection.connection();

			String sql = "insert into guestBook values(GUESTBOOK_SEQ.nextval, ?,?,?,sysdate)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, guestBook.getName());
			stmt.setString(2, guestBook.getPassword());
			stmt.setString(3, guestBook.getContent());
			stmt.executeUpdate();//
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<GuestBook> selectAll() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<GuestBook> list = null;
		try {
			conn = DAOConnection.connection();
			String sql = "select rank() over (order by regdate asc),id, name, password, content,to_char(regdate,'yyyy-mm-dd hh:mi:ss') from guestbook order by regdate desc";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			list = new ArrayList<GuestBook>();
			while (rs.next()) {
				GuestBook gb = new GuestBook();
				gb.setRank(rs.getLong(1));
				gb.setId(rs.getLong(2));
				gb.setName(rs.getString(3));
				gb.setPassword(rs.getString(4));
				gb.setContent(rs.getString(5));
				gb.setRegdate(rs.getString(6));
				list.add(gb);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public void delete(Long id) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DAOConnection.connection();

			String sql = "delete guestbook where id = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, id);

			stmt.executeUpdate();//
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
