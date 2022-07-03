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
import javax.swing.JTextField;

import dao.StaffInfoDao;
import dto.StaffInfoDto;

public class StaffInfoPanel extends JPanel implements ActionListener {

	private MovieInfoMainFrame context;
	private StaffInfoDao staffDao;
	
	private JLabel lblStaffInfo;
	private StaffFormPanel staffFormPanel = new StaffFormPanel();

	private ScrollPane staffScrollPane;
	private JTextField fldSearchStaff;

	private JButton btnSearchStaff;
	private JButton btnAllStaffSearch;
	private JButton btnInsertStaff;
	private JButton btnUpdateStaff;
	private JButton btnDeleteStaff;

	private Vector<StaffInfoDto> vcStaff = new Vector<>();
	private JList<StaffInfoDto> staffJlist = new JList<>();

	private int staffinfoNum;
	private int personInfoNum;
	
	public StaffInfoPanel(MovieInfoMainFrame context) {
		this.context = context;
	}
	
	private void initData() {
		// 스태프 부분
		setBounds(428, 407, 342, 332);
		setLayout(null);

		lblStaffInfo = new JLabel("Staff Info");
		lblStaffInfo.setFont(new Font("Arial Black", Font.BOLD, 25));
		lblStaffInfo.setBounds(38, 21, 250, 36);
		add(lblStaffInfo);

		staffScrollPane = new ScrollPane();
		staffScrollPane.setBounds(38, 98, 253, 151);
		add(staffScrollPane);

		fldSearchStaff = new JTextField();
		fldSearchStaff.setText("스태프 이름 검색");
		fldSearchStaff.setColumns(10);
		fldSearchStaff.setBounds(38, 67, 106, 21);
		add(fldSearchStaff);

		btnSearchStaff = new JButton("Search");
		btnSearchStaff.setBackground(Color.WHITE);
		btnSearchStaff.setBounds(191, 67, 100, 21);
		add(btnSearchStaff);

		staffJlist = new JList<>();
		staffJlist.setBounds(38, 98, 253, 151);
		staffScrollPane.add(staffJlist);

		btnAllStaffSearch = new JButton("Search All");
		btnAllStaffSearch.setBackground(Color.WHITE);
		btnAllStaffSearch.setBounds(38, 266, 100, 21);
		add(btnAllStaffSearch);

		btnInsertStaff = new JButton("Insert");
		btnInsertStaff.setBackground(Color.WHITE);
		btnInsertStaff.setBounds(191, 266, 100, 21);
		add(btnInsertStaff);

		btnUpdateStaff = new JButton("Update");
		btnUpdateStaff.setBackground(Color.WHITE);
		btnUpdateStaff.setBounds(191, 293, 100, 21);
		add(btnUpdateStaff);

		btnDeleteStaff = new JButton("Delete");
		btnDeleteStaff.setBackground(Color.WHITE);
		btnDeleteStaff.setBounds(38, 293, 100, 21);
		add(btnDeleteStaff);
	}
	
	private void addEventListener() {
		// 스태프 정보 관련 이벤트
		btnSearchStaff.addActionListener(this);
		btnAllStaffSearch.addActionListener(this);
		btnDeleteStaff.addActionListener(this);
		btnUpdateStaff.addActionListener(this);
		btnInsertStaff.addActionListener(this);
		staffFormPanel.getBtnUpdateStaffInfo().addActionListener(this);
		staffFormPanel.getBtnInsertStaffInfo().addActionListener(this);
		fldSearchStaff.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (fldSearchStaff.getText().equals("스태프 이름 검색")) {
					fldSearchStaff.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (fldSearchStaff.getText().equals("")) {
					fldSearchStaff.setText("스태프 이름 검색");
				}
			}
		});
		staffJlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					StaffInfoDto dto = staffJlist.getSelectedValue();
					new StaffInfoDetailFrame(dto);
					staffJlist.setSelectedValue(null, false);
				}
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		staffDao = new StaffInfoDao();

		// Staff Search 버튼
		if (e.getSource() == btnSearchStaff) {

			if (fldSearchStaff.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "스태프 이름을 입력해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE);

			} else {

				vcStaff.removeAllElements();

				String removeTrimStaffName = removeTrim(fldSearchStaff.getText());
				
				Vector<StaffInfoDto> selectDirectorNameResult = staffDao.selectDirectorName(removeTrimStaffName);

				for (int i = 0; i < selectDirectorNameResult.size(); i++) {
					vcStaff.add(selectDirectorNameResult.get(i));
				}

				staffJlist.setListData(vcStaff);
				staffScrollPane.add(staffJlist);

				staffJlist.setSelectedValue(null, false);
			}
		}

		// Staff AllSearch 버튼
		else if (e.getSource() == btnAllStaffSearch) {

			vcStaff.removeAllElements();

			Vector<StaffInfoDto> selectAllStaffInfoResult = staffDao.selectAllStaffInfo();

			for (int i = 0; i < selectAllStaffInfoResult.size(); i++) {
				vcStaff.add(selectAllStaffInfoResult.get(i));
			}

			staffJlist.setListData(vcStaff);
			staffScrollPane.add(staffJlist);

			staffJlist.setSelectedValue(null, false);

		}

		// Staff Insert 버튼
		else if (e.getSource() == btnInsertStaff) {

			resetStaffInfoTextField();

			staffFormPanel.getBtnInsertStaffInfo().setEnabled(true);
			staffFormPanel.getBtnUpdateStaffInfo().setEnabled(false);

			if(context.getJtab().getTabCount() == 2) {
				context.getJtab().removeTabAt(1);
			}
			
			context.getJtab().addTab("Staff", null, staffFormPanel, null);
			context.getJtab().setSelectedComponent(staffFormPanel);

			staffJlist.setSelectedValue(null, false);

		}

		// Staff Update 버튼
		else if (e.getSource() == btnUpdateStaff) {

			if (staffJlist.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "수정하려는 항목을 선택해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);

			} else if (staffJlist.getSelectedValue() != null) {

				staffFormPanel.getBtnUpdateStaffInfo().setEnabled(true);
				staffFormPanel.getBtnInsertStaffInfo().setEnabled(false);

				StaffInfoDto dto = staffJlist.getSelectedValue();

				staffFormPanel.getFldDirectorName().setText(dto.getDirectorName());
				staffFormPanel.getFldGender().setText(dto.getGender());
				staffFormPanel.getFldBirthYear().setText(dto.getBirthYear() + "");
				staffFormPanel.getFldMarriagePartner().setText(dto.getMarriagePartner());
				staffFormPanel.getFldRepresentativeWork().setText(dto.getRepresentativeWork());

				staffinfoNum = dto.getStaffNum();
				personInfoNum = dto.getPersonNum();

				staffJlist.setSelectedValue(null, false);
				
				if(context.getJtab().getTabCount() == 2) {
					context.getJtab().removeTabAt(1);
				}

				context.getJtab().addTab("Staff", null, staffFormPanel, null);
				context.getJtab().setSelectedComponent(staffFormPanel);

			}

		}

		// Staff Delete 버튼
		else if (e.getSource() == btnDeleteStaff) {

			if (staffJlist.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "삭제하려는 항목을 선택해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);

			} else {

				if (vcStaff.size() != 0) {

					String rempceTrimDirectorName = removeTrim(staffJlist.getSelectedValue().getDirectorName());
					
					boolean doubleCheck = staffDao.selectStaffInfoDoubleCheck(
							rempceTrimDirectorName, staffJlist.getSelectedValue().getBirthYear());

					if (!doubleCheck) {

						int deleteCheck = staffDao.deleteStaffInfo(staffJlist.getSelectedValue().getStaffNum());

						if (deleteCheck == 1) {

							int index = staffJlist.getSelectedIndex();
							vcStaff.remove(index);
							staffJlist.ensureIndexIsVisible(index);
							staffJlist.repaint();

							JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.", "INFORMATION",
									JOptionPane.INFORMATION_MESSAGE);

						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "삭제하려는 정보가 존재하지 않습니다", "ERROR", JOptionPane.ERROR_MESSAGE);

				}
			}
			staffJlist.setSelectedValue(null, false);
		}

		// StaffInfoPanel - 스태프 정보 추가 부분
		else if (e.getSource() == staffFormPanel.getBtnInsertStaffInfo()) {

			if (!staffFormPanel.getFldBirthYear().getText().equals("")
					&& !staffFormPanel.getFldDirectorName().getText().equals("")
					&& !staffFormPanel.getFldGender().getText().equals("")
					&& !staffFormPanel.getFldRepresentativeWork().getText().equals("")) {
				
				String removeTrimDirectorName = removeTrim(staffFormPanel.getFldDirectorName().getText());

				boolean doubleCheck = staffDao.selectStaffInfoDoubleCheck(removeTrimDirectorName,
						Integer.parseInt(staffFormPanel.getFldBirthYear().getText()));

				if (doubleCheck) {

					StaffInfoDto dto = new StaffInfoDto();
					addDtoStaffInfo(dto);
					int insertCheck = staffDao.insertStaffInfo(dto);

					if (insertCheck == 1) {
						JOptionPane.showMessageDialog(null, "등록이 완료되었습니다.", "INFORMATION",
								JOptionPane.INFORMATION_MESSAGE);
						resetStaffInfoTextField();
					}

				} else {
					JOptionPane.showMessageDialog(null, "입력하신 감독정보가 이미 존재합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "빈칸을 전부 입력해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

		// StaffInfoPanel - 스태프 정보 수정 부분
		else if (e.getSource() == staffFormPanel.getBtnUpdateStaffInfo()) {

			if (!staffFormPanel.getFldBirthYear().getText().equals("")
					&& !staffFormPanel.getFldDirectorName().getText().equals("")
					&& !staffFormPanel.getFldGender().getText().equals("")
					&& !staffFormPanel.getFldMarriagePartner().getText().equals("")
					&& !staffFormPanel.getFldRepresentativeWork().getText().equals("")) {

				StaffInfoDto dto = new StaffInfoDto();
				addDtoStaffInfo(dto);
				int updateCheck = staffDao.updateStaffInfo(staffinfoNum, dto);

				if (updateCheck == 1) {
					JOptionPane.showMessageDialog(null, "업데이트가 완료되었습니다.", "INFORMATION",
							JOptionPane.INFORMATION_MESSAGE);
					resetStaffInfoTextField();

				} else {
					JOptionPane.showMessageDialog(null, "업데이트가 정상적으로 처리되지 않았습니다", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "빈칸을 전부 입력해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private String removeTrim(String keyword) {
		return keyword.replace(" ", "");
	}

	// StaffInfo 정보를 StaffInfoDto로 밀어 넣는 메소드 ( insert, update 에서 사용 )
	private void addDtoStaffInfo(StaffInfoDto dto) {
		dto.setStaffNum(staffinfoNum);
		dto.setPersonNum(personInfoNum);
		dto.setDirectorName(staffFormPanel.getFldDirectorName().getText());

		dto.setGender(staffFormPanel.getFldGender().getText());
		if (!staffFormPanel.getFldBirthYear().getText().equals(null)) {
			dto.setBirthYear(Integer.parseInt(staffFormPanel.getFldBirthYear().getText()));
		}
		dto.setRepresentativeWork(staffFormPanel.getFldRepresentativeWork().getText());
		dto.setMarriagePartner(staffFormPanel.getFldMarriagePartner().getText());
	}

	// 입력후 필드 리셋
	private void resetStaffInfoTextField() {
		staffFormPanel.getFldDirectorName().setText(null);
		staffFormPanel.getFldGender().setText(null);
		staffFormPanel.getFldBirthYear().setText(null);
		staffFormPanel.getFldRepresentativeWork().setText(null);
		staffFormPanel.getFldMarriagePartner().setText(null);
	}

}
