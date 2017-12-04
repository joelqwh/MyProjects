package sample.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import sample.dao.jdbc.DBUtility;
import sample.dao.jdbc.StudentDAO;
import sample.dao.jdbc.StudentDaoImpl;
import sample.model.Student;

public class StudentManager {
	
public ArrayList<Student> findAll() throws SQLException
{
	StudentDAO sdao = new StudentDaoImpl();
	Connection conn = new DBUtility().getConnection();
	return (ArrayList<Student>)sdao.loadAll(conn);
	
	
}
}
