package edu.kh.jdbc1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc1.model.vo.Emp;

public class JDBCEXample3 {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		//부서명을 입력받아 같은 부서에 있는 사원의
		//사원명, 부서명, 급여 조회
		
		// 1단계: JDBC 참조 변수 선언 (java.sql)
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs =null;
		
		try {
			//2단계 : 참조변수에 알맞은 객체 대입
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String type = "jdbc:oracle:thin:@";
			String ip = "localhost";
			String port = ":1521";
			String sid = ":XE";
			String user = "kh";
			String pw = "kh1234";
			
			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
			
			System.out.println("<입력 받은 부서에 있는 사원의 사원명, 부서명, 급여조회>");
			System.out.print("부서명 입력: ");
			
			String input = sc.next();

			
			String sql = "SELECT EMP_NAME, NVL(DEPT_TITLE, '부서없음') AS DEPT_TITLE, SALARY "
					+ "FROM EMPLOYEE "
					+ "LEFT JOIN DEPARTMENT ON (DEPT_CODE = DEPT_ID) "
					+ "WHERE NVL(DEPT_TITLE,'부서없음') = '" + input + "'"; 
			
			//(중요!)
			// Java에서 작성되는 SQL에 
			// 문자열(String) 변수를 추가(이어쓰기)할 경우
			// ' ' (DM 문자열 리터럴이 누락되지 않도록 신결쓸 것!)
		
			// 만약 '' 미작성 시 String 값은 컬럼명으로 인식되어 부적합한 식별자 오류가 발생한다.
			
			
	
			stmt = conn.createStatement();		
			
			//Statement 객체를 이용해서
			//SQL(SELECT)을 DB에 전달하여 실행한 후
			//ResultSet을 반환 받아 rs 변수에 대입
			rs = stmt.executeQuery(sql);
			
			// 조회결과(rs)를 List에 옮겨 담기
			List<Emp> list = new ArrayList<Emp>();
			
			while(rs.next()) { // 다음 행으로 이동해서 해당 행에 데이터가 있으면 true
				
				//현재 행에 존재하는 컬럼값 얻어오기
				String empName =rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				int salary = rs.getInt("SALARY");
				
				//EMP 객체를 생성하여 컬럼 값 담기
			 
				Emp emp = new Emp(empName, deptTitle, salary);
				
				//생성된 Emp객체를 List추가
				list.add(emp);
				
			
			} 
			
			//조회결과 없는 경우
			//if(list.isEmpty()) 
			if(list.isEmpty()){
				System.out.println("조회 결과 없음");
				
			} else {
				//향상된 for문
				for(Emp emp : list) {
					System.out.println(emp);
				}
			}
			
					
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				if(rs != null)rs.close();
				if(stmt != null)stmt.close();
				if(conn != null)conn.close();
				
			} catch(SQLException e) {
				e.printStackTrace();
				
			}
					
			
		}
		
		
		}
	
	

}
