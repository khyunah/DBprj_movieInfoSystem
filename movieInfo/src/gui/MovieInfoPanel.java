package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dao.MovieInfoDao;
import dto.MovieInfoDto;

public class MovieInfoPanel extends JPanel implements ActionListener {

	private MovieInfoDao movieDao;
	
	private int movieinfoNum;
	
	// 영화 패널 부분
	private JLabel lblMovieInfo;
	private MovieFormPanel movieFormPanel = new MovieFormPanel();

	private ScrollPane movieScrollPane;
	private JTextArea movieTextArea;
	private JTextField fldSearchMovie;

	private JButton btnMovieSearch;
	private JButton btnAllMovieSearch;
	private JButton btnDeleteMovie;
	private JButton btnUpdateMovie;
	private JButton btnInsertMovie;
	
	private Vector<MovieInfoDto> vcMovie = new Vector<>();
	private JList<MovieInfoDto> movieInfoJList = new JList<>();
	
	private MovieInfoMainFrame context;
	
	public MovieInfoPanel(MovieInfoMainFrame context) {
		this.context = context;
		initData();
		addEventListener();
	}
	
	private void initData() {
		// 영화 부분
		setBounds(23, 23, 747, 332);
		setLayout(null);

		lblMovieInfo = new JLabel("Movie Info");
		lblMovieInfo.setFont(new Font("Arial Black", Font.BOLD, 25));
		lblMovieInfo.setBounds(12, 10, 168, 27);
		add(lblMovieInfo);

		fldSearchMovie = new JTextField();
		fldSearchMovie.setFont(new Font("나눔고딕", Font.BOLD, 13));
		fldSearchMovie.setBounds(12, 47, 287, 25);
		fldSearchMovie.setText("영화 이름 검색");
		add(fldSearchMovie);

		movieScrollPane = new ScrollPane();
		movieScrollPane.setBounds(12, 84, 577, 226);
		add(movieScrollPane);

		movieTextArea = new JTextArea();
		movieTextArea.setBounds(12, 88, 573, 222);
		movieScrollPane.add(movieTextArea);

		movieInfoJList = new JList<>();
		movieInfoJList.setBounds(12, 88, 573, 222);
		movieTextArea.add(movieInfoJList);

		btnMovieSearch = new JButton("Search");
		btnMovieSearch.setBounds(622, 48, 100, 25);
		btnMovieSearch.setBackground(Color.WHITE);
		add(btnMovieSearch);

		btnAllMovieSearch = new JButton("Search All");
		btnAllMovieSearch.setBounds(622, 88, 100, 25);
		btnAllMovieSearch.setBackground(Color.WHITE);
		add(btnAllMovieSearch);

		btnInsertMovie = new JButton("Insert");
		btnInsertMovie.setBounds(622, 134, 100, 25);
		btnInsertMovie.setBackground(Color.WHITE);
		add(btnInsertMovie);

		btnUpdateMovie = new JButton("Update");
		btnUpdateMovie.setBounds(622, 179, 100, 25);
		btnUpdateMovie.setBackground(Color.WHITE);
		add(btnUpdateMovie);

		btnDeleteMovie = new JButton("Delete");
		btnDeleteMovie.setBounds(622, 224, 100, 25);
		btnDeleteMovie.setBackground(Color.WHITE);
		add(btnDeleteMovie);
	}
	
	private void addEventListener() {
		// 영화정보 관련 이벤트
		btnMovieSearch.addActionListener(this);
		btnAllMovieSearch.addActionListener(this);
		btnInsertMovie.addActionListener(this);
		btnUpdateMovie.addActionListener(this);
		btnDeleteMovie.addActionListener(this);
		movieFormPanel.getBtnInsertMovieInfo().addActionListener(this);
		movieFormPanel.getBtnUpdateMovieInfo().addActionListener(this);
		fldSearchMovie.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (fldSearchMovie.getText().equals("영화 이름 검색")) {
					fldSearchMovie.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (fldSearchMovie.getText().equals("")) {
					fldSearchMovie.setText("영화 이름 검색");
				}
			}
		});
		movieInfoJList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					MovieInfoDto dto = movieInfoJList.getSelectedValue();
					new MovieInfoDetailFrame(dto);

					// 선택되어진 밸류 없애는 코드
					movieInfoJList.setSelectedValue(null, false);
				}
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// 기능 DAO
		movieDao = new MovieInfoDao();
		
		// Movie Search 버튼
		if (e.getSource() == btnMovieSearch) {

			if (fldSearchMovie.getText().equals("영화 이름 검색")) {
				JOptionPane.showMessageDialog(null, "영화이름을 입력해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else {

				// 현재 리스트에 있는 데이터 없애기
				vcMovie.removeAllElements();

				// 입력한 영화제목으로 검색하기
				String removeTrimTitle = fldSearchMovie.getText().replace(" ", "");
				Vector<MovieInfoDto> selectMoviTitleResult = movieDao.selectMovieTitle(removeTrimTitle);

				for (int i = 0; i < selectMoviTitleResult.size(); i++) {
					vcMovie.add(selectMoviTitleResult.get(i));
				}

				movieInfoJList.setListData(vcMovie);
				movieScrollPane.add(movieInfoJList);

				movieInfoJList.setSelectedValue(null, false);
			}
		}

		// Movie Search All 버튼
		else if (e.getSource() == btnAllMovieSearch) {

			vcMovie.removeAllElements();

			Vector<MovieInfoDto> selectAllMovieInfoResult = movieDao.selectAllMovieInfo();

			
			
			for (int i = 0; i < selectAllMovieInfoResult.size(); i++) {
				vcMovie.add(selectAllMovieInfoResult.get(i));
			}
			

			movieInfoJList.setListData(vcMovie);
			movieScrollPane.add(movieInfoJList);

			movieInfoJList.setSelectedValue(null, false);
		}

		// Movie Insert 버튼
		else if (e.getSource() == btnInsertMovie) {
			
			resetMovieInfoTextField();

			if(context.getJtab().getTabCount() == 2) {
				context.getJtab().removeTabAt(1);
			}

			context.getJtab().addTab("Moive", null, movieFormPanel, null);

			// 해당 탭으로 이동
			context.getJtab().setSelectedComponent(movieFormPanel);

			movieFormPanel.getBtnUpdateMovieInfo().setEnabled(false);
			movieFormPanel.getBtnInsertMovieInfo().setEnabled(true);

			movieInfoJList.setSelectedValue(null, false);

		}

		// Movie Update 버튼
		else if (e.getSource() == btnUpdateMovie) {

			if (movieInfoJList.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "수정하려는 영화 항목을 선택해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);

			} else if (movieInfoJList.getSelectedValue() != null) {

				movieFormPanel.getBtnUpdateMovieInfo().setEnabled(true);
				movieFormPanel.getBtnInsertMovieInfo().setEnabled(false);

				MovieInfoDto dto = movieInfoJList.getSelectedValue();

				movieFormPanel.getFldMovieTitle().setText(dto.getMovieTitle());
				movieFormPanel.getFldDirectorName().setText(dto.getDirectorName());

				movieFormPanel.getFldTotalIncome().setText(String.valueOf(dto.getTotalIncome()));
				movieFormPanel.getFldAudience().setText(String.valueOf(dto.getAudience()));
				movieFormPanel.getFldRating().setText(String.valueOf(dto.getRating()));

				movieFormPanel.getFldReleaseYear().setText(String.valueOf(dto.getReleaseYear()));
				movieFormPanel.getFldReleaseMonth().setText(String.valueOf(dto.getReleaseMonth()));

				movieFormPanel.getFldMoviePlot().setText(dto.getMoviePlot());
				movieFormPanel.getFldReview1().setText(dto.getReview1());
				movieFormPanel.getFldReview2().setText(dto.getReview2());
				movieFormPanel.getFldReview3().setText(dto.getReview3());

				movieinfoNum = dto.getMovieInfoNum();

				movieInfoJList.setSelectedValue(null, false);
				
				if(context.getJtab().getTabCount() == 2) {
					context.getJtab().removeTabAt(1);
				}

				context.getJtab().addTab("Movie", null, movieFormPanel, null);
				context.getJtab().setSelectedComponent(movieFormPanel);
			}
		}

		// Movie Delete 버튼
		else if (e.getSource() == btnDeleteMovie) {

			if (movieInfoJList.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "삭제하려는 항목을 선택해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);

			} else {

				if (vcMovie.size() != 0) {

					int movieinfoNum = movieInfoJList.getSelectedValue().getMovieInfoNum();

					// DB에서 데이터 삭제
					int deleteCheck = movieDao.deleteMovieInfo(movieinfoNum);

					if (deleteCheck == 1) {

						// 보여지는 화면 JList에서 삭제
						int index = movieInfoJList.getSelectedIndex();
						vcMovie.remove(index);
						movieInfoJList.repaint();

						JOptionPane.showMessageDialog(null, "영화 정보 삭제가 완료되었습니다.", "INFORMATION",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "영화 정보 삭제가 정상적으로 처리 되지 않았습니다.", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			movieInfoJList.setSelectedValue(null, false);
		}

		// MovieInfoPanel - 영화정보 등록하기 버튼
		else if (e.getSource() == movieFormPanel.getBtnInsertMovieInfo()) {
			

			// 패널에 있는 텍스트 필드에 전부 입력을 했는지 체크하는 과정
			
			if (!movieFormPanel.getFldMovieTitle().getText().equals("")
					&& !movieFormPanel.getFldDirectorName().getText().equals("")
					&& !movieFormPanel.getFldTotalIncome().getText().equals("")
					&& !movieFormPanel.getFldAudience().getText().equals("")
					&& !movieFormPanel.getFldRating().getText().equals("")
					&& !movieFormPanel.getFldReleaseYear().getText().equals("")
					&& !movieFormPanel.getFldReleaseMonth().getText().equals("")) {

				String removeTrimMovieTitle = removeTrim(movieFormPanel.getFldMovieTitle().getText());
				String removeTrimDirectorName = removeTrim(movieFormPanel.getFldDirectorName().getText());
				
				boolean doubleCheck = movieDao.selectMovieDoubleCheck(removeTrimMovieTitle,	removeTrimDirectorName);

				// 중복이 아닐때 true
				if (doubleCheck) {

					MovieInfoDto dto = new MovieInfoDto();
					addDtoMovieInfo(dto);
					int insertCheck = movieDao.insertMovieInfo(dto);

					if (insertCheck == 1) {
						JOptionPane.showMessageDialog(null, "영화 정보 등록이 정상적으로 완료되었습니다.", "Information",
								JOptionPane.INFORMATION_MESSAGE);
						resetMovieInfoTextField();
					} else {
						JOptionPane.showMessageDialog(null, "영화 정보 등록이 정상적으로 처리되지 않았습니다.", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}

				} else {
					JOptionPane.showMessageDialog(null, "입력하신 정보의 영화가 이미 존재합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "영화이름, 감독, 매출액, 관객수, 평점, 개봉연도, 개봉월은 빈칸없이 입력해주세요.", "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		// MovieInfoPanel - 영화정보 수정하기 버튼
		else if (e.getSource() == movieFormPanel.getBtnUpdateMovieInfo()) {

			if (!movieFormPanel.getFldMovieTitle().getText().equals("")
					&& !movieFormPanel.getFldDirectorName().getText().equals("")
					&& !movieFormPanel.getFldTotalIncome().getText().equals("")
					&& !movieFormPanel.getFldAudience().getText().equals("")
					&& !movieFormPanel.getFldRating().getText().equals("")
					&& !movieFormPanel.getFldReleaseYear().getText().equals("")
					&& !movieFormPanel.getFldReleaseMonth().getText().equals("")) {

				MovieInfoDto dto = new MovieInfoDto();
				addDtoMovieInfo(dto);
				int updateCheck = movieDao.updateMovieInfo(movieinfoNum, dto);

				if (updateCheck == 1) {
					JOptionPane.showMessageDialog(null, "영화 정보 업데이트가 정상적으로 완료되었습니다.", "INFORMATION",
							JOptionPane.INFORMATION_MESSAGE);
					resetMovieInfoTextField();

				} else {
					JOptionPane.showMessageDialog(null, "영화 정보 업데이트가 정상적으로 처리되지 않았습니다", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "영화이름, 감독, 매출액, 관객수, 평점, 개봉연도, 개봉월은 빈칸없이 입력해주세요", "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private String removeTrim(String keyword) {
		return keyword.replace(" ", "");
	}
	
	// 입력후 필드 리셋
	private void resetMovieInfoTextField() {
		movieFormPanel.getFldMovieTitle().setText(null);
		movieFormPanel.getFldDirectorName().setText(null);

		movieFormPanel.getFldTotalIncome().setText(null);
		movieFormPanel.getFldAudience().setText(null);
		movieFormPanel.getFldRating().setText(null);

		movieFormPanel.getFldReleaseYear().setText(null);
		movieFormPanel.getFldReleaseMonth().setText(null);

		movieFormPanel.getFldMoviePlot().setText(null);
		movieFormPanel.getFldReview1().setText(null);
		movieFormPanel.getFldReview2().setText(null);
		movieFormPanel.getFldReview3().setText(null);
	}
	
	// MovieInfo 정보를 MovieInfoDto로 밀어 넣는 메소드 ( insert, update 에서 사용 )
	private void addDtoMovieInfo(MovieInfoDto dto) {
		dto.setMovieTitle(movieFormPanel.getFldMovieTitle().getText());
		dto.setDirectorName(movieFormPanel.getFldDirectorName().getText());

		dto.setReleaseYear(Integer.parseInt(movieFormPanel.getFldReleaseYear().getText()));
		dto.setReleaseMonth(Integer.parseInt(movieFormPanel.getFldReleaseMonth().getText()));

		dto.setMoviePlot(movieFormPanel.getFldMoviePlot().getText());

		dto.setTotalIncome(Integer.parseInt(movieFormPanel.getFldTotalIncome().getText()));
		dto.setAudience(Integer.parseInt(movieFormPanel.getFldAudience().getText()));
		dto.setRating(Float.parseFloat((movieFormPanel.getFldRating().getText())));

		dto.setReview1(movieFormPanel.getFldReview1().getText());
		dto.setReview2(movieFormPanel.getFldReview2().getText());
		dto.setReview3(movieFormPanel.getFldReview3().getText());
	}

}
