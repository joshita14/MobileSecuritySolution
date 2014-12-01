

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iiit.CloudStorageAPI;

/**
 * Servlet implementation class GetDateSelected
 */
@WebServlet("/GetDateSelected")
public class GetDateSelected extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static Date date;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDateSelected() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
//		String bucketName=request.getParameter("bucketName");
//		String dateInString=request.getParameter("date2");
//		
//		System.out.println("This is the date   !!!!"+dateInString);
//		
//		request.setAttribute("bucketName", bucketName);
//		request.setAttribute("date", dateInString);
//	    request.getRequestDispatcher("./imagegallery.jsp").forward(request, response);
	}

}
