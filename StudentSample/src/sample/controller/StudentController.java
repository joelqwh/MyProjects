package sample.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.model.Student;
import sample.services.StudentManager;

/**
 * Servlet implementation class StudentController
 */
@WebServlet("/StudentController/load")		//anything with studetncnontroller wildcard can come here
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StudentManager manager = new StudentManager();
		String returnpath = "/Error.jsp";
		//String cmd = request.getPathInfo();
		
//		switch (cmd)
//		{
//		case "/load":
			ArrayList<Student> slist = null;
			try {
				slist = manager.findAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("slist", slist);
			returnpath = "/DisplayStudents.jsp";
//		}
		

		RequestDispatcher rd = request.getRequestDispatcher(returnpath);
		rd.forward(request, response);
		// will have R, U, D methods in here
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}
	
	

}
