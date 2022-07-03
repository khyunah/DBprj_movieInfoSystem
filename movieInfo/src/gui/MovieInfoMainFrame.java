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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.ActorDao;
import dao.MovieInfoDao;
import dao.StaffInfoDao;
import dto.ActorInfoDto;
import dto.MovieInfoDto;
import dto.StaffInfoDto;
import lombok.Data;

@Data
public class MovieInfoMainFrame extends JFrame{

	private JPanel mainpanel;
	private JPanel searchPanel;
	private JTabbedPane jtab;

	private MovieInfoPanel movieInfoPanel;

	private ActorInfoPanel actorInfoPanel;

	private StaffInfoPanel staffInfoPanel;

	public MovieInfoMainFrame() {
		initData();
	}

	private void initData() {		
		setTitle("Movie Information Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 850, 850);
		setResizable(false);

		mainpanel = new JPanel();
		mainpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainpanel.setSize(getWidth(), getHeight());
		mainpanel.setBackground(Color.WHITE);
		mainpanel.setLayout(null);
		setContentPane(mainpanel);

		jtab = new JTabbedPane(JTabbedPane.TOP);
		jtab.setBounds(15, 5, 800, 800);
		mainpanel.add(jtab);

		searchPanel = new JPanel();
		jtab.addTab("Main", null, searchPanel, null);
		searchPanel.setLayout(null);

		// 영화 부분
		movieInfoPanel = new MovieInfoPanel(this);
		searchPanel.add(movieInfoPanel);

		// 배우 부분
		actorInfoPanel = new ActorInfoPanel(this);
		searchPanel.add(actorInfoPanel);

		// 스태프 부분
		staffInfoPanel = new StaffInfoPanel(this);
		searchPanel.add(staffInfoPanel);

		setVisible(true);
	}

	public static void main(String[] args) {
		new MovieInfoMainFrame();
	}
}
