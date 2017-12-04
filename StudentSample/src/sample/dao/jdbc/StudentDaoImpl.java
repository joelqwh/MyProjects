package sample.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javassist.NotFoundException;
import sample.model.Student;

public class StudentDaoImpl implements StudentDAO {
	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#createValueObject()
	 */
	@Override
	public Student createValueObject() {
		return new Student();
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#getObject(java.sql.Connection, int)
	 */
	@Override
	public Student getObject(Connection conn, int studentId) throws NotFoundException, SQLException {

		Student valueObject = createValueObject();
		valueObject.setStudentId(studentId);
		load(conn, valueObject);
		return valueObject;
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#load(java.sql.Connection, sample.model.Student)
	 */
	@Override
	public void load(Connection conn, Student valueObject) throws NotFoundException, SQLException {

		String sql = "SELECT * FROM student WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valueObject.getStudentId());

			singleQuery(conn, stmt, valueObject);

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#loadAll(java.sql.Connection)
	 */
	@Override
	public List loadAll(Connection conn) throws SQLException {

		String sql = "SELECT * FROM student ORDER BY id ASC ";
		List searchResults = listQuery(conn, conn.prepareStatement(sql));

		return searchResults;
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#create(java.sql.Connection, sample.model.Student)
	 */
	@Override
	public synchronized void create(Connection conn, Student valueObject) throws SQLException {

		String sql = "";
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			sql = "INSERT INTO student ( name, nick, course) VALUES (?, ?, ?) ";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, valueObject.getName());
			stmt.setString(2, valueObject.getNick());
			stmt.setString(3, valueObject.getCourse());

			int rowcount = databaseUpdate(conn, stmt);
			if (rowcount != 1) {
				// System.out.println("PrimaryKey Error when updating DB!");
				throw new SQLException("PrimaryKey Error when updating DB!");
			}

		} finally {
			if (stmt != null)
				stmt.close();
		}

		/**
		 * The following query will read the automatically generated primary key back to
		 * valueObject. This must be done to make things consistent for upper layer
		 * processing logic.
		 */
		sql = "SELECT last_insert_id()";

		try {
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();

			if (result.next()) {

				valueObject.setStudentId((int) result.getLong(1));

			} else {
				// System.out.println("Unable to find primary-key for created object!");
				throw new SQLException("Unable to find primary-key for created object!");
			}
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}

	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#save(java.sql.Connection, sample.model.Student)
	 */
	@Override
	public void save(Connection conn, Student valueObject) throws NotFoundException, SQLException {

		String sql = "UPDATE student SET name = ?, nick = ?, course = ? WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valueObject.getName());
			stmt.setString(2, valueObject.getNick());
			stmt.setString(3, valueObject.getCourse());

			stmt.setInt(4, valueObject.getStudentId());

			int rowcount = databaseUpdate(conn, stmt);
			if (rowcount == 0) {
				// System.out.println("Object could not be saved! (PrimaryKey not found)");
				throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
			}
			if (rowcount > 1) {
				// System.out.println("PrimaryKey Error when updating DB! (Many objects were
				// affected!)");
				throw new SQLException("PrimaryKey Error when updating DB! (Many objects were affected!)");
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#delete(java.sql.Connection, sample.model.Student)
	 */
	@Override
	public void delete(Connection conn, Student valueObject) throws NotFoundException, SQLException {

		String sql = "DELETE FROM student WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valueObject.getStudentId());

			int rowcount = databaseUpdate(conn, stmt);
			if (rowcount == 0) {
				// System.out.println("Object could not be deleted (PrimaryKey not found)");
				throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
			}
			if (rowcount > 1) {
				// System.out.println("PrimaryKey Error when updating DB! (Many objects were
				// deleted!)");
				throw new SQLException("PrimaryKey Error when updating DB! (Many objects were deleted!)");
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#deleteAll(java.sql.Connection)
	 */
	@Override
	public void deleteAll(Connection conn) throws SQLException {

		String sql = "DELETE FROM student";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			int rowcount = databaseUpdate(conn, stmt);
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#countAll(java.sql.Connection)
	 */
	@Override
	public int countAll(Connection conn) throws SQLException {

		String sql = "SELECT count(*) FROM student";
		PreparedStatement stmt = null;
		ResultSet result = null;
		int allRows = 0;

		try {
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();

			if (result.next())
				allRows = result.getInt(1);
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}
		return allRows;
	}

	/* (non-Javadoc)
	 * @see sample.dao.jdbc.StudentDAO#searchMatching(java.sql.Connection, sample.model.Student)
	 */
	@Override
	public List searchMatching(Connection conn, Student valueObject) throws SQLException {

		List searchResults;

		boolean first = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM student WHERE 1=1 ");

		if (valueObject.getStudentId() != 0) {
			if (first) {
				first = false;
			}
			sql.append("AND id = ").append(valueObject.getStudentId()).append(" ");
		}

		if (valueObject.getName() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND name LIKE '").append(valueObject.getName()).append("%' ");
		}

		if (valueObject.getNick() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND nick LIKE '").append(valueObject.getNick()).append("%' ");
		}

		if (valueObject.getCourse() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND course LIKE '").append(valueObject.getCourse()).append("%' ");
		}

		sql.append("ORDER BY id ASC ");

		// Prevent accidential full table results.
		// Use loadAll if all rows must be returned.
		if (first)
			searchResults = new ArrayList();
		else
			searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

		return searchResults;
	}

	/**
	 * getDaogenVersion will return information about generator which created these
	 * sources.
	 */
	public String getDaogenVersion() {
		return "DaoGen version 2.4.1";
	}

	/**
	 * databaseUpdate-method. This method is a helper method for internal use. It
	 * will execute all database handling that will change the information in
	 * tables. SELECT queries will not be executed here however. The return value
	 * indicates how many rows were affected. This method will also make sure that
	 * if cache is used, it will reset when data changes.
	 *
	 * @param conn
	 *            This method requires working database connection.
	 * @param stmt
	 *            This parameter contains the SQL statement to be excuted.
	 */
	protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

		int result = stmt.executeUpdate();

		return result;
	}

	/**
	 * databaseQuery-method. This method is a helper method for internal use. It
	 * will execute all database queries that will return only one row. The
	 * resultset will be converted to valueObject. If no rows were found,
	 * NotFoundException will be thrown.
	 *
	 * @param conn
	 *            This method requires working database connection.
	 * @param stmt
	 *            This parameter contains the SQL statement to be excuted.
	 * @param valueObject
	 *            Class-instance where resulting data will be stored.
	 */
	protected void singleQuery(Connection conn, PreparedStatement stmt, Student valueObject)
			throws NotFoundException, SQLException {

		ResultSet result = null;

		try {
			result = stmt.executeQuery();

			if (result.next()) {

				valueObject.setStudentId(result.getInt("id"));
				valueObject.setName(result.getString("name"));
				valueObject.setNick(result.getString("nick"));
				valueObject.setCourse(result.getString("course"));

			} else {
				// System.out.println("Student Object Not Found!");
				throw new NotFoundException("Student Object Not Found!");
			}
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}
	}

	/**
	 * databaseQuery-method. This method is a helper method for internal use. It
	 * will execute all database queries that will return multiple rows. The
	 * resultset will be converted to the List of valueObjects. If no rows were
	 * found, an empty List will be returned.
	 *
	 * @param conn
	 *            This method requires working database connection.
	 * @param stmt
	 *            This parameter contains the SQL statement to be excuted.
	 */
	protected List listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

		ArrayList searchResults = new ArrayList();
		ResultSet result = null;

		try {
			result = stmt.executeQuery();

			while (result.next()) {
				Student temp = createValueObject();

				temp.setStudentId(result.getInt("id"));
				temp.setName(result.getString("name"));
				temp.setNick(result.getString("nick"));
				temp.setCourse(result.getString("course"));

				searchResults.add(temp);
			}

		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}

		return (List) searchResults;
	}
}
