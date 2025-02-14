package com.devturtle.group;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.devturtle.mission.MissionGroupDAO;
import com.devturtle.mission.MissionJoinGroupVO;
import com.devturtle.rank.RankGroupDAO;
import com.devturtle.rank.RankGroupVO;
import com.devturtle.user.UserVO;
import com.google.gson.Gson;

import com.devturtle.mission.MissionCheckProcessGroup; // 그룹 미션 수행 여부 조회하는 클래스 추가

/**
 * Servlet implementation class GroupDetailServlet
 */
@WebServlet("/groupdetail")
public class GroupDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 // URL 파라미터로 groupId 받기
        String groupIdParam = request.getParameter("groupid");

        Integer userId = null;
        String userIdParam = request.getParameter("userid");
        
        if (userIdParam != null && !userIdParam.isEmpty()) {
        	userId = Integer.parseInt(userIdParam);
        } else {
        	HttpSession session = request.getSession();
            userId = (Integer) session.getAttribute("SESS_USER_ID");
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        }
        
        System.out.println("groupIdParam >>> "+groupIdParam);
        if (groupIdParam != null) {
            int groupId = Integer.parseInt(groupIdParam); // groupId를 정수로 변환

            MissionCheckProcessGroup mcpg = new MissionCheckProcessGroup();
            mcpg.missionGroupCompleteCheck(groupId); // 미션 체크 프로세스 추가 : 2005-01-08
            
            
            // 그룹 전체 정보
            GroupDAO gdao = new GroupDAO();
            
            GroupVO groupDetail = gdao.selectGroupDetail(groupId);  // 사용자 ID와 groupId로 그룹 정보 조회
            request.setAttribute("GROUP_DETAIL", groupDetail);

            // 그룹 유저 수정 
            Boolean bool= gdao.groupUserCheckOfUserId(groupId,userId);
            request.setAttribute("GROUP_USER_CHECK", bool);
            
            
            // 그룹 랭킹 정보
            GroupVO gRankVO = gdao.selectGroupByIDWithRank(groupId);
            int gRank = gRankVO.getRank();
            request.setAttribute("GROUP_RANK", gRank);
            //int allGroupSize = gdao.selectAllGroupSize();
            //request.setAttribute("GROUP_SIZE", allGroupSize);
            
            // 그룹 랭킹 차트 정보
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String today = dateFormat.format(new Date());
            
            RankGroupDAO rgdao = new RankGroupDAO();
            ArrayList<RankGroupVO> gRankList = rgdao.selectRankGroupAllByMonth(groupId, today);
            System.out.println("test >>>>"+  gRankList.toString());
            
            String gsonRankList = new Gson().toJson(gRankList);
            System.out.println("GROUP_RANK_CHART: " + gsonRankList);
            
            request.setAttribute("GROUP_RANK_CHART",  new Gson().toJson(gRankList));
            
            
            // Group User ( 수 + userVO ) 반환
            int groupUserCnt = gdao.getNumberOfGroupUser(groupId); 
            ArrayList<GroupUserVO> groupUserList = gdao.selectAllGroupUser(groupId);

            request.setAttribute("GROUP_USER_CNT", groupUserCnt);
            request.setAttribute("GROUP_USER_LIST", groupUserList);
            
            
            // 그룹 미션 정보
            MissionGroupDAO mgdao = new MissionGroupDAO();
            
            ArrayList<MissionJoinGroupVO> mgvo = mgdao.selectMissionGroup(groupId);
            ArrayList<MissionJoinGroupVO> b_mgvo = mgdao.selectMissionGroupBadge(groupId);
            int groupBadgeCnt = b_mgvo.size();

            request.setAttribute("MISSION_GROUP_CNT", groupBadgeCnt);
            request.setAttribute("MISSION_GROUP_LIST", mgvo);
            request.setAttribute("MISSION_GROUP_BADGE_LIST", b_mgvo);
            
            
            // 동적으로 포함할 contentPage 경로 설정
            request.setAttribute("contentPage", "/jsp/group/group_detail.jsp");

            // layout.jsp로 포워딩
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            // groupId가 없을 경우 에러 처리
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Group ID is required.");
        }
    }
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
	        // 세션 ID 가져오기
	    int userId = (Integer) session.getAttribute("SESS_USER_ID");
	    int groupId =  Integer.parseInt(request.getParameter("groupid"));
	    
	    System.out.println("updaet 전 확인 >>>"+groupId);
	    String name = request.getParameter("group-name");
	    int size = Integer.parseInt(request.getParameter("group-size"));
	    int condition = Integer.parseInt(request.getParameter("group-condition"));
	    String description = request.getParameter("group-description");
	    
	    String location = "서울"; // 수정 x
	    String rule = request.getParameter("group-rule"); // 수정 x
	    String gprivate = request.getParameter("group-private"); // 수정 x
	    String category = request.getParameter("group-category"); // 수정 x
	    
	    GroupVO gvo = new GroupVO(	groupId, name,	size, condition,
									description, category,
									gprivate,	location);
	    
	    System.out.println(" param check >>>>> " + gvo.toString());
		
		  GroupDAO gdao = new GroupDAO(); int result =
		  gdao.updateGroupDetail(userId,gvo);
		  System.out.println("updateGroupDetail 수정결과는?? >> " + result);
		  response.sendRedirect("/groupdetail?groupid="+groupId);
		 	
	    }

}
