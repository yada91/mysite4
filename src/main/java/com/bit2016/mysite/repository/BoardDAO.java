package com.bit2016.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2016.mysite.vo.Board;
import com.bit2016.mysite.vo.Page;

@Repository
public class BoardDAO {

	@Autowired
	private DataSource datasource;

	public void insert(Board board) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = datasource.getConnection();

			String sql = "INSERT INTO BOARD VALUES " + "(board_SEQ.NEXTVAL, ?,?, SYSDATE, 0,?,"
					+ "NVL ( (SELECT MAX (group_no) FROM board), 0) + 1,1,0)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, board.getTitle());
			stmt.setString(2, board.getContent());
			stmt.setLong(3, board.getUser_no());

			stmt.executeUpdate();
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

	public ArrayList<Board> select(Page page) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Board> list = null;

		try {
			conn = datasource.getConnection();
			String sql = "SELECT * FROM (SELECT ROWNUM AS rn, a1.* FROM ( SELECT b.no, b.title, b.hits, TO_CHAR (b.reg_date, 'yyyy-mm-dd hh:mi:ss') AS reg_date, b.DEPTH, u.NAME,b.USER_NO "
					+ "FROM board b, users u WHERE b.USER_NO = u.NO ORDER BY group_no DESC, order_no ASC) a1) a2 WHERE (?- 1) * ? + 1 <= a2.rn and a2.rn <= ? * ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, page.getCurrentPage());
			pstmt.setInt(2, page.getPageSize());
			pstmt.setInt(3, page.getCurrentPage());
			pstmt.setInt(4, page.getPageSize());

			rs = pstmt.executeQuery();
			list = new ArrayList<Board>();
			while (rs.next()) {
				Long rn = rs.getLong(1);
				Long no = rs.getLong(2);
				String title = rs.getString(3);
				Long hits = rs.getLong(4);
				String reg_date = rs.getString(5);
				Long depth = rs.getLong(6);
				String user_name = rs.getString(7);
				Long user_no = rs.getLong(8);

				Board board = new Board();
				board.setRn(rn);
				board.setNo(no);
				board.setTitle(title);
				board.setHits(hits);
				board.setReg_date(reg_date);
				board.setDepth(depth);
				board.setUser_name(user_name);
				board.setUser_no(user_no);

				list.add(board);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
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

	public ArrayList<Board> search(Page page, String kwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Board> list = null;

		try {
			conn = datasource.getConnection();
			String sql = "SELECT * FROM (SELECT ROWNUM AS rn, a1.* FROM (  SELECT b.no, b.title, b.hits, TO_CHAR (b.reg_date, 'yyyy-mm-dd hh:mi:ss') AS reg_date, b.DEPTH, u.NAME, b.USER_NO FROM board b, users u WHERE b.USER_NO = u.NO AND (b.TITLE like ? OR b.CONTENT LIKE ?) ORDER BY group_no DESC, order_no ASC) a1) a2 WHERE (? - 1) * ? + 1 <= a2.rn AND a2.rn <= ? * ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kwd);
			pstmt.setString(2, kwd);
			pstmt.setInt(3, page.getCurrentPage());
			pstmt.setInt(4, page.getPageSize());
			pstmt.setInt(5, page.getCurrentPage());
			pstmt.setInt(6, page.getPageSize());

			rs = pstmt.executeQuery();
			list = new ArrayList<Board>();
			while (rs.next()) {
				Long rn = rs.getLong(1);
				Long no = rs.getLong(2);
				String title = rs.getString(3);
				Long hits = rs.getLong(4);
				String reg_date = rs.getString(5);
				Long depth = rs.getLong(6);
				String user_name = rs.getString(7);
				Long user_no = rs.getLong(8);

				Board board = new Board();
				board.setRn(rn);
				board.setNo(no);
				board.setTitle(title);
				board.setHits(hits);
				board.setReg_date(reg_date);
				board.setDepth(depth);
				board.setUser_name(user_name);
				board.setUser_no(user_no);

				list.add(board);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
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

	public int count() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int countNum = 0;
		try {
			conn = datasource.getConnection();
			String sql = "select count(*) from board";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				countNum = rs.getInt(1);
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
		return countNum;
	}

	public int searchCount(String kwd) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int countNum = 0;
		try {
			conn = datasource.getConnection();
			String sql = "select count(*) from board where content like ? OR title like ?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, kwd);
			stmt.setString(2, kwd);
			rs = stmt.executeQuery();
			if (rs.next()) {
				countNum = rs.getInt(1);
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
		return countNum;
	}

	public Board view(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board board = null;
		try {
			conn = datasource.getConnection();
			String sql = "select title,content,user_no from board where no =? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				board = new Board();
				board.setNo(no);
				board.setTitle(rs.getString(1));
				board.setContent(rs.getString(2));
				board.setUser_no(rs.getLong(3));
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return board;
	}

	public Board replyView(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board board = null;
		try {
			conn = datasource.getConnection();
			String sql = "select group_no,order_no,depth from board where no =? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				board = new Board();
				board.setNo(no);
				board.setGroup_no(rs.getLong(1));
				board.setOrder_no(rs.getLong(2));
				board.setDepth(rs.getLong(3));
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return board;
	}

	public void update(Board board) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = datasource.getConnection();

			String sql = "UPDATE board set title = ?, content= ? where no = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, board.getTitle());
			stmt.setString(2, board.getContent());
			stmt.setLong(3, board.getNo());
			stmt.executeUpdate();
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

	public void updateHits(Long no) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = datasource.getConnection();

			String sql = "UPDATE board set hits = hits + 1  where no = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, no);
			stmt.executeUpdate();
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

	public void updateReply(Board board) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = datasource.getConnection();

			String sql = "UPDATE board SET order_no = order_no + 1 WHERE group_no = ?  AND order_no > ?";

			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, board.getGroup_no());
			stmt.setLong(2, board.getOrder_no());
			stmt.executeUpdate();
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

	public void insertReply(Board board) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = datasource.getConnection();

			String sql = "INSERT INTO BOARD VALUES (board_SEQ.NEXTVAL, ?,?, SYSDATE, 0,?,?,?,?)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, board.getTitle());
			stmt.setString(2, board.getContent());
			stmt.setLong(3, board.getUser_no());
			stmt.setLong(4, board.getGroup_no());
			stmt.setLong(5, board.getOrder_no());
			stmt.setLong(6, board.getDepth());
			stmt.executeUpdate();
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

	public void delete(Long no) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = datasource.getConnection();

			String sql = "DELETE FROM BOARD WHERE no = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, no);
			stmt.executeUpdate();
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
