import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;


public class TwitchLiveWatcherGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public TwitchLiveWatcherGUI(JTable table) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 367, 396);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	
		this.setVisible(true);
	}
}
