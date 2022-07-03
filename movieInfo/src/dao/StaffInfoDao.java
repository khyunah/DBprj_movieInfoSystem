package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import db.DBClient;
import dto.StaffInfoDto;
import interfaces.IStaffService;

public class StaffInfoDao implements IStaffService {

	private DBClient dbClient;
	private Connection connection;
	private PreparedStatement preparedStatement;

	public StaffInfoDao() {
		dbClient = DBClient.getInstance("movieinfo");
		connection = dbClient.getConnection();
	}

	/**
	 * select - staff이름으로 정보조회
	 */
	@Override
	public Vector<StaffInfoDto> selectDirectorName(String searchWord) {
		Vector<StaffInfoDto> selectDirectorNameDtos = new Vector<>();
		String selectDirectorNameQuery = "SELECT * FROM view_staffInfoAll WHERE REPLACE(personName, ' ', '') LIKE '%' ? '%' ";
		try {
			preparedStatement = connection.prepareStatement(selectDirectorNameQuery);
			preparedStatement.setString(1, searchWord);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				StaffInfoDto dto = new StaffInfoDto();
				dto.setDirectorName(resultSet.getString("personName"));
				dto.setBirthYear(resultSet.getInt("birthYear"));
				dto.setGender(resultSet.getString("gender"));
				dto.setMarriagePartner(resultSet.getString("marriegePartner"));
				dto.setPersonNum(resultSet.getInt("personNum"));
				dto.setRepresentativeWork(resultSet.getString("대표작품"));
				dto.setStaffNum(resultSet.getInt("staffNum"));

				selectDirectorNameDtos.add(dto);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return selectDirectorNameDtos;
	}

	@Override
	public Vector<StaffInfoDto> selectAllStaffInfo() {
		Vector<StaffInfoDto> staffInfoDtos = new Vector<>();
		String selectAllStaffInfoQuery = "SELECT * FROM view_staffinfoall ";
		try {
			preparedStatement = connection.prepareStatement(selectAllStaffInfoQuery);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				StaffInfoDto dto = new StaffInfoDto();
				dto.setDirectorName(resultSet.getString("personName"));
				dto.setBirthYear(resultSet.getInt("birthYear"));
				dto.setGender(resultSet.getString("gender"));
				dto.setMarriagePartner(resultSet.getString("marriegePartner"));
				dto.setPersonNum(resultSet.getInt("personNum"));
				dto.setRepresentativeWork(resultSet.getString("대표작품"));
				dto.setStaffNum(resultSet.getInt("staffNum"));
				staffInfoDtos.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return staffInfoDtos;

	}

	@Override
	public int insertStaffInfo(StaffInfoDto dto) {
		
		String insertQuery = "INSERT INTO personInfo(personName, gender, birthYear, height, weight, marriegePartner) VALUES(?, ?, ?, ?, ?, ?)";
		int result = -1;
		
		try {
			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setString(1,dto.getDirectorName());
			preparedStatement.setString(2, dto.getGender());
			preparedStatement.setInt(3, dto.getBirthYear());
			preparedStatement.setString(4, null);
			preparedStatement.setString(5, null);
			preparedStatement.setString(6, dto.getMarriagePartner());
			result = preparedStatement.executeUpdate();
			
			
			// SELECT
			// movieinfoNum을 조회하기 위함.
			String selectQuery = "SELECT * FROM personInfo WHERE REPLACE(personName, ' ', '') LIKE '%' ? '%' AND birthYear = ? ";
			preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, dto.getDirectorName());
			preparedStatement.setInt(2, dto.getBirthYear());
			ResultSet resultSet = preparedStatement.executeQuery();
			int personNum = 0;
			while (resultSet.next()) {
				personNum = resultSet.getInt("personNum");
			}
	
			insertQuery = "INSERT INTO staffInfo(personNum, 대표작품) VALUES(?, ?) ";
			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setInt(1,personNum);
			preparedStatement.setString(2,dto.getRepresentativeWork());
			result = preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int updateStaffInfo(int staffInfoNum, StaffInfoDto dto) {

		String updateQuery = "UPDATE personinfo SET personName = ? , gender = ?, birthYear=?, marriegePartner=? WHERE personNum = ? ";
		int result=-1;
		try {
			preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setString(1, dto.getDirectorName());
			preparedStatement.setString(2,dto.getGender());
			preparedStatement.setInt(3, dto.getBirthYear());
			preparedStatement.setString(4, dto.getMarriagePartner());
			preparedStatement.setInt(5, dto.getPersonNum());
			try {
				result = preparedStatement.executeUpdate();
			} catch(SQLException s) {
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		updateQuery = "UPDATE staffinfo SET 대표작품 = ? where staffNum = ? ";
		try {
			preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setString(1, dto.getRepresentativeWork());
			preparedStatement.setInt(2, staffInfoNum);
			result = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
		

	/**
	 * SELECT staff 중복검사 INSERT, DELETE 기능 수행하기전 중복을 검사한다.
	 */
	@Override
	public boolean selectStaffInfoDoubleCheck(String directorName, int birthYear) {

		// 중복 체크변수
		boolean doubleCheck = false;

		try {
			// 중복검사
			String selectCheckQuery = "SELECT * FROM view_staffinfoall WHERE REPLACE(personName, ' ', '') LIKE '%' ? '%' AND birthYear = ?";
			preparedStatement = connection.prepareStatement(selectCheckQuery);
			preparedStatement.setString(1, directorName);
			preparedStatement.setInt(2, birthYear);

			ResultSet checkRs = preparedStatement.executeQuery();
			int staffInfoNumCheck = -1;
			
			while (checkRs.next()) {
			 staffInfoNumCheck = checkRs.getInt("staffNum");
			}
			
			// 중복이 아니라면 INSERT
			if (staffInfoNumCheck == -1) {
				doubleCheck = true; 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return doubleCheck;
	}
	
	@Override
	public int deleteStaffInfo(int staffInfoNum) {

		int result = -1;
		try {
			// DELETE
			String deleteQuery = "DELETE FROM staffinfo WHERE staffNum = ? ";
			System.out.println(staffInfoNum);
			preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setInt(1, staffInfoNum);
			result = preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}