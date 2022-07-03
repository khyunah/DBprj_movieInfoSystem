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

import dao.ActorDao;
import dto.ActorInfoDto;

public class ActorInfoPanel extends JPanel implements ActionListener {

	private ActorDao actorDao;
	private MovieInfoMainFrame context;
	
	private int actcorNum;
	
	// 배우 패널 부분
	private JLabel lblActorInfo;
	private ActorFormPanel actorFormPanel = new ActorFormPanel();

	private ScrollPane actorScrollPane;
	private JTextField fldSearchActor;

	private JButton btnInsertActor;
	private JButton btnUpdateActor;
	private JButton btnDeleteActor;
	private JButton btnSearchActor;
	private JButton btnAllActorSearch;

	private Vector<ActorInfoDto> vcActor = new Vector<>();
	private JList<ActorInfoDto> actorJList = new JList<>();
	
	public ActorInfoPanel(MovieInfoMainFrame context) {
		this.context = context;
		initData();
		addEventListener();
	}
	
	private void initData() {
		// 배우 부분
		setBounds(23, 407, 342, 332);
		setLayout(null);

		fldSearchActor = new JTextField();
		fldSearchActor.setText("배우 이름 검색");
		fldSearchActor.setBounds(38, 67, 106, 21);
		fldSearchActor.setColumns(10);
		add(fldSearchActor);

		actorScrollPane = new ScrollPane();
		actorScrollPane.setBounds(38, 98, 253, 151);
		add(actorScrollPane);

		lblActorInfo = new JLabel("Actor Info");
		lblActorInfo.setFont(new Font("Arial Black", Font.BOLD, 25));
		lblActorInfo.setBounds(38, 21, 147, 36);
		add(lblActorInfo);

		actorJList = new JList<>();
		actorJList.setBounds(38, 98, 253, 151);
		actorScrollPane.add(actorJList);

		btnAllActorSearch = new JButton("Search All");
		btnAllActorSearch.setBackground(Color.WHITE);
		btnAllActorSearch.setBounds(38, 266, 100, 21);
		add(btnAllActorSearch);

		btnInsertActor = new JButton("Insert");
		btnInsertActor.setBackground(Color.WHITE);
		btnInsertActor.setBounds(191, 266, 100, 21);
		add(btnInsertActor);

		btnUpdateActor = new JButton("Update");
		btnUpdateActor.setBackground(Color.WHITE);
		btnUpdateActor.setBounds(191, 293, 100, 21);
		add(btnUpdateActor);

		btnDeleteActor = new JButton("Delete");
		btnDeleteActor.setBackground(Color.WHITE);
		btnDeleteActor.setBounds(38, 293, 100, 21);
		add(btnDeleteActor);

		btnSearchActor = new JButton("Search");
		btnSearchActor.setBackground(Color.WHITE);
		btnSearchActor.setBounds(191, 67, 100, 21);
		add(btnSearchActor);
	}
	
	private void addEventListener() {
		// 배우 정보 관련 이벤트
		btnAllActorSearch.addActionListener(this);
		btnSearchActor.addActionListener(this);
		btnUpdateActor.addActionListener(this);
		btnDeleteActor.addActionListener(this);
		btnInsertActor.addActionListener(this);
		actorFormPanel.getBtnInsertActorInfo().addActionListener(this);
		actorFormPanel.getBtnUpdateActorInfo().addActionListener(this);
		fldSearchActor.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (fldSearchActor.getText().equals("배우 이름 검색")) {
					fldSearchActor.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (fldSearchActor.getText().equals("")) {
					fldSearchActor.setText("배우 이름 검색");
				}
			}
		});
		actorJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					ActorInfoDto dto = actorJList.getSelectedValue();
					new ActorInfoDetailFrame(dto);
					actorJList.setSelectedValue(null, false);
				}
			}
		});
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		actorDao = new ActorDao();
		
		// Actor Search 버튼
				if (e.getSource() == btnSearchActor) {

					if (fldSearchActor.getText().equals("")) {

						JOptionPane.showMessageDialog(null, "배우 이름을 입력해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE);

					} else {

						vcActor.removeAllElements();
						
						String removeTrimActorName = removeTrim(fldSearchActor.getText());

						Vector<ActorInfoDto> selectActorInfor = actorDao.selectActorInfor(removeTrimActorName);

						for (int i = 0; i < selectActorInfor.size(); i++) {
							vcActor.add(selectActorInfor.get(i));
						}

						actorJList.setListData(vcActor);
						actorScrollPane.add(actorJList);

						actorJList.setSelectedValue(null, false);
					}
				}

				// Actor Search All 버튼
				else if (e.getSource() == btnAllActorSearch) {

					vcActor.removeAllElements();

					Vector<ActorInfoDto> selectAllActorInfoResult = actorDao.selectAllActorInfor();

					for (int i = 0; i < selectAllActorInfoResult.size(); i++) {
						vcActor.add(selectAllActorInfoResult.get(i));
					}

					actorJList.setListData(vcActor);
					actorScrollPane.add(actorJList);

					actorJList.setSelectedValue(null, false);
				}

				// Actor Insert 버튼
				else if (e.getSource() == btnInsertActor) {

					resetActorInfoTextField();

					actorFormPanel.getBtnUpdateActorInfo().setEnabled(false);
					actorFormPanel.getBtnInsertActorInfo().setEnabled(true);
					
					if(context.getJtab().getTabCount() == 2) {
						context.getJtab().removeTabAt(1);
					}

					context.getJtab().addTab("Actor", null, actorFormPanel, null);
					context.getJtab().setSelectedComponent(actorFormPanel);

					actorJList.setSelectedValue(null, false);

				}

				// Actor Update 버튼
				else if (e.getSource() == btnUpdateActor) {

					if (actorJList.getSelectedValue() == null) {
						JOptionPane.showMessageDialog(null, "업데이트 항목을 선택해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);

					} else if (actorJList.getSelectedValue() != null) {

						actorFormPanel.getBtnUpdateActorInfo().setEnabled(true);
						actorFormPanel.getBtnInsertActorInfo().setEnabled(false);

						ActorInfoDto dto = actorJList.getSelectedValue();

						actorFormPanel.getFldActorName().setText(dto.getActorName());
						actorFormPanel.getFldActorRepresentativeMovie().setText(dto.getRepresentativeMovie());
						actorFormPanel.getFldActorRepresentativeRole().setText(dto.getRepresentativeRole());
						actorFormPanel.getFldActorBirthYear().setText(dto.getBirthYear());
						actorFormPanel.getFldatorTall().setText(dto.getActorTall());
						actorFormPanel.getFldActorWieght().setText(dto.getActorWeight());
						actorFormPanel.getFldActorMarriagePartner().setText(dto.getMarriagePartner());
						actorFormPanel.getFldActorGender().setText(dto.getGender());

						actcorNum = dto.getActorNum();
						
						if(context.getJtab().getTabCount() == 2) {
							context.getJtab().removeTabAt(1);
						}

						context.getJtab().addTab("Actor", null, actorFormPanel, null);
						context.getJtab().setSelectedComponent(actorFormPanel);

						actorJList.setSelectedValue(null, false);
					}
				}

				// Actor Delete 버튼
				else if (e.getSource() == btnDeleteActor) {

					if (actorJList.getSelectedValue() == null) {
						JOptionPane.showMessageDialog(null, "삭제하려는 항목을 선택해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);

					} else {

						if (vcActor.size() != 0) {
							
							String removeActorName = removeTrim(actorJList.getSelectedValue().getActorName());
							int actorBirthYear = Integer.parseInt(actorJList.getSelectedValue().getBirthYear());

							boolean doubleCheck = actorDao.selectActorDoubleCheck(removeActorName, actorBirthYear);

							if (!doubleCheck) {

								int deleteCheck = actorDao.deleteActorInfo(actorJList.getSelectedValue().getPersonNum());

								if (deleteCheck == 1) {

									int index = actorJList.getSelectedIndex();
									vcActor.remove(index);
									actorJList.ensureIndexIsVisible(index);
									actorJList.repaint();

									JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.", "INFORMATION",
											JOptionPane.INFORMATION_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(null, "삭제하려는 정보가 존재하지 않습니다", "ERROR", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					actorJList.setSelectedValue(null, false);
				}

				// ActorInfoPanel - 배우정보 등록하기 버튼
				else if (e.getSource() == actorFormPanel.getBtnInsertActorInfo()) {

					if (!actorFormPanel.getFldActorName().getText().equals("")
							&& !actorFormPanel.getFldActorGender().getText().equals("")
							&& !actorFormPanel.getFldActorBirthYear().getText().equals("")
							&& !actorFormPanel.getFldatorTall().getText().equals("")
							&& !actorFormPanel.getFldActorWieght().getText().equals("")) {

						String removeTrimActorName = actorFormPanel.getFldActorName().getText();
						int actorBirthYear = Integer.parseInt(actorFormPanel.getFldActorBirthYear().getText());
						
						boolean doubleCheck = actorDao.selectActorDoubleCheck(removeTrimActorName, actorBirthYear);

						if (!doubleCheck) {

							ActorInfoDto dto = new ActorInfoDto();
							addDtoActorInfo(dto);
							int insertCheck = actorDao.insertActorInfo(dto);

							if (insertCheck == 1) {
								JOptionPane.showMessageDialog(null, "배우정보 등록이 완료되었습니다.", "INFORMATION",
										JOptionPane.INFORMATION_MESSAGE);

								resetActorInfoTextField();

							} else {
								JOptionPane.showMessageDialog(null, "배우 정보 등록이 정상적으로 처리되지 않았습니다.", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(null, "입력하신 정보의 배우가 이미 존재합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "배우 이름을 입력해주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}

				// ActorInfoPanel - 배우정보 수정하기 버튼
				else if (e.getSource() == actorFormPanel.getBtnUpdateActorInfo()) {

					ActorInfoDto dto = new ActorInfoDto();
					addDtoActorInfo(dto);
					int updateCheck = actorDao.updateActorInfo(actcorNum, dto);

					if (updateCheck == 1) {
						JOptionPane.showMessageDialog(null, "배우 정보 업데이트가 완료되었습니다.", "INFORMATION",
								JOptionPane.INFORMATION_MESSAGE);
						resetActorInfoTextField();

					} else {
						JOptionPane.showMessageDialog(null, "배우 정보 업데이트가 정상적으로 처리되지 않았습니다", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
	}
	
	private String removeTrim(String keyword) {
		return keyword.replace(" ", "");
	}
	
	// ActorInfo 정보를 ActorInfoDto로 밀어 넣는 메소드 ( insert, update 에서 사용 )
		private void addDtoActorInfo(ActorInfoDto dto) {
			dto.setActorNum(actcorNum);
			dto.setActorName(actorFormPanel.getFldActorName().getText());

			dto.setRepresentativeMovie(actorFormPanel.getFldActorRepresentativeMovie().getText());
			dto.setRepresentativeRole(actorFormPanel.getFldActorRepresentativeRole().getText());

			dto.setBirthYear(actorFormPanel.getFldActorBirthYear().getText());
			dto.setActorTall(actorFormPanel.getFldatorTall().getText());
			dto.setActorWeight(actorFormPanel.getFldActorWieght().getText());
			dto.setGender(actorFormPanel.getFldActorGender().getText());

			dto.setMarriagePartner(actorFormPanel.getFldActorMarriagePartner().getText());
		}

		// 입력후 필드 리셋
		private void resetActorInfoTextField() {
			actorFormPanel.getFldActorName().setText(null);
			actorFormPanel.getFldActorRepresentativeMovie().setText(null);
			actorFormPanel.getFldActorRepresentativeRole().setText(null);
			actorFormPanel.getFldActorBirthYear().setText(null);
			actorFormPanel.getFldatorTall().setText(null);
			actorFormPanel.getFldActorWieght().setText(null);
			actorFormPanel.getFldActorMarriagePartner().setText(null);
			actorFormPanel.getFldActorGender().setText(null);
		}
}
