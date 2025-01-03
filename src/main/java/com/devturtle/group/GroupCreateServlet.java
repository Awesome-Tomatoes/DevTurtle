package com.devturtle.group;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devturtle.user.UserDAO;
import com.devturtle.user.UserVO;

/**
 * Servlet implementation class GroupCreateServlet
 */
@WebServlet("/groupcreate")
public class GroupCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

		// ----------------------페이지 -------------------------
		// 동적으로 포함할 contentPage 경로 설정
	    request.setAttribute("contentPage", "/jsp/group/group_create.jsp");

	    // layout.jsp로 포워딩
	    request.getRequestDispatcher("/index.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("group-name");
	    String category = request.getParameter("group-category");
	    int size = Integer.parseInt(request.getParameter("group-size"));
	    String gprivate = request.getParameter("group-private");
	    int condition = Integer.parseInt(request.getParameter("group-condition"));
	    String rule = request.getParameter("group-rule");
	    String description = request.getParameter("group-description");
	    String location = "서울";
	    GroupDAO gdao = new GroupDAO();
	    int userId = 1; 
	    GroupVO gvo = new GroupVO(name,size, condition,
				description, category,
				gprivate,location);
	    
	    gdao.createGroup(userId,gvo);

	    response.sendRedirect("/groupcreate");
	}

}
