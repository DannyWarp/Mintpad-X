package dannywarp.mintpad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Mintpad_X extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar menu_bar;
	private JMenu menu_project, menu_help, view, preset, theme;
	private JMenuItem open_list, save, workspace, exit;
	private JMenuItem contents, about;
	private JRadioButtonMenuItem edit_mode, view_mode;
	private JRadioButtonMenuItem small, medium,  def, metal, nimbus, gtk;
	private Dimension screen_size;
	private boolean small_e = true, medium_e, help_mode;
	private boolean plist_init, one = true;
	private boolean escape, escape1, activate, sub, block = true;
	private DefaultListModel<String> projects_model;
	private JDialog plist, new_project, sw_wspace;
	private JButton p_open, create, bt_close;
	private JButton p_new, sw_set, sw_close;
	private JButton p_remove;
	private JList<String> content_list, subcontent_list, list;
	private JTextArea contentpad;
	private String category, subcategory, name, file = "";
	private int step;
	private JTextField tf, sw_tf;
	
	Core core = new Core();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mintpad_X frame = new Mintpad_X();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Mintpad_X() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Mintpad-X");
		setLayout(new BorderLayout());
		
		Image icon = Toolkit.getDefaultToolkit().getImage("resources/mintpad_icon.png");
		//Image icon = getToolkit().getImage(ClassLoader.getSystemResource("resources/mintpad_icon.png")); // uncomment this for JAR build
		setIconImage(icon);
		
		core.load_configurations();
		
		 try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		screen_size = new Dimension();
		screen_size = getToolkit().getScreenSize();
		setSize(screen_size.width / 2, screen_size.height / 2);
		setMinimumSize(new Dimension(screen_size.width / 4, screen_size.height / 4));
		
		setLocationRelativeTo(null);
		
		menu_bar = new JMenuBar();
		
		// Menus
		menu_project = new JMenu(" Project ");
		menu_help = new JMenu(" Help ");
		
		// Project menu items
		open_list = new JMenuItem(" Open Projects List ");
		save = new JMenuItem(" Save ");
		//save_as = new JMenuItem(" Save As.. ");
		workspace = new JMenuItem(" Switch Workspace ");
		theme = new JMenu(" Theme ");
		exit = new JMenuItem(" Exit ");	
		view = new JMenu(" View ");
		preset = new JMenu(" Preset ");
		edit_mode = new JRadioButtonMenuItem(" Edit Mode ");
		view_mode = new JRadioButtonMenuItem(" View Mode ");
		small = new JRadioButtonMenuItem(" Small ");
		medium = new JRadioButtonMenuItem(" Medium ");
		
		def = new JRadioButtonMenuItem(" Default ");
		metal = new JRadioButtonMenuItem(" Metal ");
		nimbus = new JRadioButtonMenuItem(" Nimbus ");
		gtk = new JRadioButtonMenuItem(" GTK+ ");
		theme.add(def);
		theme.add(metal);
		theme.add(nimbus);
		theme.add(gtk);
		
		preset.add(small);
		preset.add(medium);
		view.add(edit_mode);
		view.add(view_mode);
		view.add(preset);
		
		var button_group0 = new ButtonGroup();
		button_group0.add(view_mode);
		button_group0.add(edit_mode);
		
		var button_group1 = new ButtonGroup();
		button_group1.add(small);
		button_group1.add(medium);
		
		var button_group2 = new ButtonGroup();
		button_group2.add(def);
		button_group2.add(metal);
		button_group2.add(nimbus);
		button_group2.add(gtk);
		
		def.setSelected(true);
		
		// Help menu items
		contents = new JMenuItem(" Help Contents ");
		about = new JMenuItem(" About Mintpad ");
		
		// Adding items
		menu_project.add(open_list);	
		menu_project.add(save);
		menu_project.add(view);
		menu_project.add(theme);
		menu_project.add(workspace);
		menu_project.add(exit);
		
		menu_help.add(contents);
		menu_help.add(about);
		
		menu_bar.add(menu_project);
		menu_bar.add(menu_help);
		
		setJMenuBar(menu_bar);
		
			var content_model = new DefaultListModel<String>();
			content_list = new JList<String>(content_model);
			var content_pane = new JScrollPane(content_list);
			
			var subcontent_model = new DefaultListModel<String>();
			subcontent_list = new JList<String>(subcontent_model);
			var subcontent_pane = new JScrollPane(subcontent_list);
			
			contentpad = new JTextArea();
			var contentpad_pane = new JScrollPane(contentpad);
			var container = new JPanel();
			container.setLayout(new BorderLayout());
			
			container.add(contentpad_pane, BorderLayout.CENTER);
			
			contentpad.setTabSize(4);
			contentpad.setLineWrap(true);
			
			var field = new JTextField();
			
			var panel = new JPanel();
			
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			
			var bt_add = new JButton(" Add ");
			var bt_remove = new JButton(" Remove ");
			bt_remove.setEnabled(false);
			
			bt_add.setEnabled(false);
			contentpad.setEnabled(false);
				
			panel.add(bt_remove);
			panel.add(Box.createHorizontalStrut(6));
			panel.add(bt_add);
			panel.add(Box.createHorizontalStrut(6));
			panel.add(new JSeparator(SwingConstants.VERTICAL));
			panel.add(Box.createHorizontalStrut(6));
			panel.add(field);
			container.add(panel, BorderLayout.SOUTH);
			
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
			var split0 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, content_pane, subcontent_pane);
			var split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split0, container);
			
			split0.setOneTouchExpandable(true);
			split0.setContinuousLayout(true);
			
			split1.setContinuousLayout(true);
			split1.setOneTouchExpandable(true);
			
			add(split1);
			
			bt_remove.addActionListener((event) -> {

				if (content_list.getSelectedIndex() == -1) {
					return;
				}
				
				bt_remove.setText(" Confirm ");
				
				if (block) {
					block = false;
					return;
				}
				
					if (!sub) {
						category = content_list.getSelectedValue();
						core.remove_content(core.get_folder(name, category));
						content_model.remove(content_list.getSelectedIndex());
						content_list.updateUI();
						bt_remove.setEnabled(false);
					}
					
					bt_remove.setText(" Remove ");
					
					if (subcontent_list.getSelectedIndex() == -1) {
						return;
					}
				
					if (sub) {
						subcategory = subcontent_list.getSelectedValue();
						core.remove_content(core.get_folder(name, category, subcategory));
						subcontent_model.remove(subcontent_list.getSelectedIndex());
						subcontent_list.updateUI();
						sub = false;
						bt_remove.setEnabled(false);
					}
					
				bt_remove.setText(" Remove ");
				
				
			});
			
			bt_add.addActionListener((event) -> {
				if (help_mode) {
					if (step == 0) {
						content_model.addElement(field.getText());
						contentpad.setText(contentpad.getText() + "\nSelect your category");
						step += 1;
					}
					
					else {
						subcontent_model.addElement(field.getText());
						contentpad.setText("Select your subcategory");
					}
					
					return;
						
				}
				block = true;
       		 	bt_remove.setText(" Remove ");
       		 
       		 	if (content_list.getSelectedIndex() == -1) {
       		 	String category = field.getText();
       		 	if (category.isBlank()) {
					 return;
				 }
   			 
       		 	for (int i = 0; i <= content_model.getSize() - 1; i++) {
					 if (category.equals(content_model.get(i))) {
						 System.out.println("error: [" +category+ "] already exists");
						 return;
					 }
				 }
       		 		
       		 	 content_model.addElement(category);
				 core.new_category(name, category);
				 System.out.println("[debug] name: "+name+ " category: " +category);
				 //String content = text_content.getText();      // WTF?
				 core.gen_content(name, category, "");
				 field.setText("");
       		 	}
       		 	
       		 	if (!sub) {
       		 		String subcategory = field.getText();
       		 		
		       		 	if (subcategory.isBlank()) {
							 return;
						 }
						 
						 for (int i = 0; i <= subcontent_model.getSize() - 1; i++) {
							 if (subcategory.equals(subcontent_model.get(i))) {
								 System.out.println("error: [" +subcategory+ "] already exists");
								 return;
							 }
						 }
       		 		
       		 		category = content_list.getSelectedValue();
       		 	 subcontent_model.addElement(subcategory);
				 core.new_subcategory(name, category, subcategory);
				 core.sub_content(name, category, subcategory, "");
				 field.setText("");
       		 	}
			});
			
			view_mode.addActionListener((event) -> {
				container.remove(panel);
				container.updateUI();
				if (isMaximumSizeSet()) {
					System.out.println("lmao");
				}

			});
			
			edit_mode.addActionListener((event) -> {
				container.add(panel, BorderLayout.SOUTH);
				container.updateUI();
			});
			
			small.setSelected(true);
			
			small.addActionListener((event) -> {
				if (small_e == false) {
				setExtendedState(NORMAL);
				screen_size = new Dimension();
				screen_size = getToolkit().getScreenSize();
				setSize(screen_size.width / 2, screen_size.height / 2);
				split0.setDividerLocation(200);
				split1.setDividerLocation(400);
				split0.updateUI();
				split1.updateUI();
				small_e = true;
				medium_e = false;
				}
				
			});
			
			medium.addActionListener((event) -> {
				if (medium_e == false) {
				setExtendedState(NORMAL);
				screen_size = new Dimension();
				screen_size = getToolkit().getScreenSize();
				int width = screen_size.width;
				int height = screen_size.height;
				int value_w = width / 2;
				int value_h = height / 2;
				setSize(width - (value_w / 2) - 150, height - (value_h / 2) - 100);
				split0.setDividerLocation(300);
				split1.setDividerLocation(600);
				split0.updateUI();
				split1.updateUI();
				medium_e = true;
				small_e = false;
				}
			});
			
			
			workspace.addActionListener((event) -> {
				switch_workspace();
			});
			
			
			open_list.addActionListener((event) -> {
				projects_list();
			});
			
			
			def.addActionListener((event) -> {
				try {
					themes(0);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			metal.addActionListener((event) -> {
				try {
					themes(1);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			nimbus.addActionListener((event) -> {
				try {
					themes(2);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			gtk.addActionListener((event) -> {
				try {
					themes(3);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			 p_open = new JButton("Open");
			 p_new = new JButton("New");
			 p_remove = new JButton("Remove");
			 p_remove.setEnabled(false);
			 p_open.setEnabled(false);		
			
			p_open.addActionListener((event) -> {
				 String project = list.getSelectedValue();
				    
			    	if (list.getSelectedIndex() == -1) {
			    		return;
			    	}
			    	
			    	content_model.clear();
			    	subcontent_model.clear();
			    	
			    	core.genloader(project);
			    	
			    	for (int i = 0; i < core.genfolders.length; i++) {
			    		content_model.addElement(core.genfolders[i]);
			    	}
			    	
			    	name = project;
			    	setTitle("Mintpad - " + name);
			    	
			    	field.setEnabled(true);
			        contentpad.setEnabled(true);
			        activate = true;
			        contentpad.setText("");
			        
			        bt_add.setEnabled(true);
			        bt_remove.setEnabled(true);
			        contentpad.setEnabled(true);
			        
			        plist.dispose();
			});
			
			create = new JButton("Create");
			
			p_new.addActionListener((event) -> {
				new_project = new JDialog(this, "New Project", true);
				new_project.setSize(434, 130);
				new_project.setLocationRelativeTo(plist);
				new_project.setLayout(null);
				new_project.setResizable(false);
				
				var label = new JLabel("Project Name:");
				
					label.setBounds(23, 17, 110, 30);
				
					tf = new JTextField();
					tf.setBounds(120, 15, 305, 35);
					create.setBounds(355, 60, 70, 35);
					
					bt_close = new JButton("Cencel");
					bt_close.setBounds(280, 60, 70, 35);
				
				new_project.add(tf);
				new_project.add(create);
				new_project.add(label);
				new_project.add(bt_close);
				new_project.setVisible(true);
			
				
			});		
			
			create.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					String name = tf.getText();	
						if (name.isBlank()) {
							return;
						}
						
						for (int i = 0; i <= projects_model.getSize() -1; i++) {
							if (name.equals(projects_model.get(i))) {
								System.out.println("error: project [" +name+ "] already exists");
								return;
							}
						}
						
							core.new_project(name);
							projects_model.addElement(name);
						
						new_project.dispose();
					
						}
			   		});
			
			
			p_remove.addActionListener((event) -> {
				//System.out.println("remove"); 
				if (list.getSelectedIndex() == -1) {
					return;
				}
				name = list.getSelectedValue();
				core.remove_content(core.get_project(name));
				projects_model.remove(list.getSelectedIndex());
				list.updateUI();
				p_remove.setEnabled(false);
				
			});
			
			sw_set = new JButton("Set");
			
			sw_set.addActionListener((event) -> {
				core.set_workspace(sw_tf.getText());
				content_model.clear();
				content_list.updateUI();
				subcontent_model.clear();
				subcontent_list.updateUI();
				contentpad.setEnabled(false);
				bt_add.setEnabled(false);
				bt_remove.setEnabled(false);
				sw_wspace.dispose();
				
			});
			
			save.addActionListener((event) -> {
				if (sub) {
					 String content = contentpad.getText();
					 String subcategory0 = subcontent_list.getSelectedValue();
					 
					 if (subcategory0 == null) {
						 System.out.println("error: no subcategory selected");
					 }
					 
						core.sub_content(name, category, subcategory0, content);
						System.out.println("["+subcategory0+"]: content saved" );
				 }
				 
				 else {
					 String content = contentpad.getText();
					 String category0 = content_list.getSelectedValue();
					 
					 if (category0 == null) {
						 System.out.println("error: no category selected");
						 return;
					 }
					 
					 core.gen_content(name, category0, content);
					 System.out.println("["+category0+"]: content saved" );
				 
				 	}
			});	
			
			
			contents.addActionListener((event) -> {
				content_model.clear();
				subcontent_model.clear();
				content_model.addElement("Select this");
				content_list.updateUI();
				help_mode = true;
				bt_remove.setEnabled(false);
				bt_add.setEnabled(false);
				contentpad.setEnabled(true);
				menu_project.setEnabled(false);
				
			});
			
			about.addActionListener((event) -> {
				JFrame about = new JFrame("About Mintpad X");
				Image ic = Toolkit.getDefaultToolkit().getImage("resources/mintpad_icon.png");
				//Image ic = getToolkit().getImage(ClassLoader.getSystemResource("resources/mintpad_icon.png")); // uncomment this for JAR build
				about.setIconImage(ic);
					about.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					about.setSize(new Dimension(983, 589));
					about.add(new About());
					about.setResizable(false);
					about.setLocationRelativeTo(this);
					about.setVisible(true);
					
			});
			
			exit.addActionListener((event) -> {
				System.exit(0);
			});
			
			
			content_list.addMouseListener(new MouseAdapter() {
	        	 public void mouseClicked(MouseEvent e) {
	        		 
	        		 if (help_mode) {
	        			 if (content_list.getSelectedIndex() == 0) {
	        				 subcontent_model.addElement("Now select this");
		        			 return;
	        			 }
	        			 
	        			 if (content_list.getSelectedIndex() == 1) {
	        				 subcontent_model.clear();
	        				 contentpad.setText("");
	        				 field.setText("Write something here again");
	        				 return;
	        			 }
	        			
	        		 }
	        		 
	        		 block = true;
	        		 if (!(content_list.getSelectedIndex() == -1)) {
	        			 bt_remove.setText(" Remove ");
	        			 bt_remove.setEnabled(true);
	        		
	        		 }
	        		 
	        		 else {
	        			 bt_remove.setEnabled(false);
	        			 subcontent_model.clear();
	        			 subcontent_list.updateUI();
	        		
	        		 }
	        		 bt_remove.setText(" Remove ");
	        		 if (activate) {
	        			 
	        			 sub = false;
	        		 
	        		 if (content_list.getSelectedIndex() == -1) {
	        			 if(escape1) {
	        				 contentpad.setText("");
	        				 return;
	        			 }
	        			 
	        			 core.gen_content(name, category, contentpad.getText());
	        			 contentpad.setText("");
	        			 return;
	        		 }
	        		 
	        		 escape = true;
	        		 
	        		 if (escape1) {
	        			 
	        			 if (!file.equals("")) {
	        			 core.sub_content(name, category, subcategory, file);
	        			 }
	        			 
	        		 }    		 
	        		 
	        		 // if H-CH = true block buffer update
	        		 if (!escape1) {;
	        			 	
	        			 	String text = contentpad.getText();
	        			 	
	        			 	if (text.equals("")) {
	        			 	}
	        			 	
	        			 	else {
	        			 		if (category == null) {
	        			 			category = content_list.getSelectedValue();
	        			 		}
	        			 		core.gen_content(name, category, text);
	        			 		System.out.println("content saved: "+text+" category: "+category);
	        			 				 		
	        			 	}
	        			 	
	        			 }
	        		    
	        		 category = content_list.getSelectedValue();
	        		 escape1 = false; // end
	        		 
	        		 // load file
	        		 core.file_content = "";
	        		 core.content_loader(name, category, null, 0);
	        		 contentpad.setText(core.file_content);
	        		 contentpad.updateUI();
	        		 
	        		 subcontent_model.clear();
	        		 core.subloader(name, category);
	        		 
	        		 for(int i = 0; i < core.subfolders.length; i++) {
				    	  if (!core.subfolders[i].equals(category)) {
				    		  subcontent_model.addElement(core.subfolders[i]);
				    	  }
				      }
	        		 
	        		 }
	        	 } // activate
	         });
			
			
			 subcontent_list.addMouseListener(new MouseAdapter() {
	        	 public void mouseClicked(MouseEvent arg0) {
	        		 if (help_mode) {
	        			 if (step == 0) {
	        				 contentpad.setText("Now you see this.");
	        			 }
	        			 
	        			 else {
	        				
	        				 contentpad.setText("That's all. \nDeselect: Ctrl + Left Mouse Button \nSave: Ctrl + S"
	        				 		+ "\nPress here for disable help mode"
	        						 + "\n--------------------------------------------------"
	        						 + "\nThanks for using!");
	        			 }
	        			 
	        			 return;
	        		 }
	        		 block = true;
	        		 if (!(subcontent_list.getSelectedIndex() == -1)) {
	        			 bt_remove.setText(" Remove ");
	        			 bt_remove.setEnabled(true);	
	        		
	        		 }
	        		 
	        		 else {
	        			 bt_remove.setEnabled(false);	
	        			 
	        		 }
	        		 
	        		 bt_remove.setText(" Remove ");
	        		 if (activate) {
	        			 
	        			 sub = true;
	        			 
	        			 if (subcontent_list.getSelectedIndex() == -1) {
	        				 if(escape) {
	        					 contentpad.setText("");
	        					 return;
	        				 }
	        				 
	        				core.sub_content(name, category, subcategory, contentpad.getText());
	        				contentpad.setText("");
	        				return;
	        				 
	        			 }
	        		 
	        		 escape1 = true;
	        		 
	        		 if (escape) {
	        			String content =  contentpad.getText();
	        			if (!content.equals("")) {
	        			core.gen_content(name, category, content);
	        			}
	        			
	        		 }
	        		 
	        		 if (!escape) {
	     			 	
	     			     String text = contentpad.getText();
	     			 	
	     			 	if (text.equals("")) {
	     			 		System.out.println("[sub] text is empty");
	     			 	}
	     			 	
	     			 	else {
	     			 		if(subcategory == null) {
	     			 			subcategory = content_list.getSelectedValue();
	     			 		}
	     			 		core.sub_content(name, category, subcategory, text);
	     			 		
	     			 	}
	        		 }
	        		 
	        		 subcategory = subcontent_list.getSelectedValue(); // sub-category update
	        		 System.out.println("load content");
	        		 escape = false;
	        		 
	        		 core.file_content = "";
	        		 core.content_loader(name, category, subcategory, 1);
	  
	        		 contentpad.setText("");
	        		 contentpad.setText(core.file_content);
	        		 contentpad.updateUI();
	        		   		 
	        		 }
	        	 }// activate
	        	 
	        });
			
 
			 contentpad.addMouseListener(new MouseAdapter() {
				 	
				 public void mouseClicked(MouseEvent e) {
					 if (help_mode) {
						 if (!(step == 1)) {
						 	field.setText("Write here something");
						 	
						 }
						 
						 else {
							 contentpad.setText("");
							 field.setText("");
							 content_model.clear();
							 subcontent_model.clear();
							 contentpad.setEnabled(false);
							 bt_add.setEnabled(false);
							 step = 0;
							 one = true;
							 help_mode = false;
							 menu_project.setEnabled(true);
						 }
						 
						 return;
					 }
					 block = true;
					 bt_remove.setText(" Remove ");
				 }
			 
			 });
		 
			 
			 contentpad.addKeyListener(new KeyAdapter() {
	        	 
	        	 boolean ctrl;
	        	 
	        	 public void keyPressed(KeyEvent e) {
	        	 
	        	 if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
	    			 ctrl = true;
	    		 }
	    		 
	    		 if (e.getKeyCode() == KeyEvent.VK_S) {
	    			 if (ctrl) {
	    				 // save content, ctrl+s + selected (sub)category
	    				 if (sub) {
	    					 String content = contentpad.getText();
	    					 String subcategory0 = subcontent_list.getSelectedValue();
	    					 
	    					 if (subcategory0 == null) {
	    						 System.out.println("error: no subcategory selected");
	    					 }
	    					 
								core.sub_content(name, category, subcategory0, content);
								System.out.println("["+subcategory0+"]: content saved" );
	    				 }
	    				 
	    				 else {
	    					 String content = contentpad.getText();
	    					 String category0 = content_list.getSelectedValue();
	    					 
	    					 if (category0 == null) {
	    						 System.out.println("error: no category selected");
	    						 return;
	    					 }
	    					 
							 core.gen_content(name, category0, content);
							 System.out.println("["+category0+"]: content saved" );
	    				 
	    				 
	    				 	}
	    			 	}
	    		 	}
	        	}
	        	 
	        	 public void keyReleased(KeyEvent e) {
	        		 
	        		 if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
	        			 ctrl = false;
	        		 	}
	        	 		}
	        	 
			 	}); 
			 
			 
			 field.addKeyListener(new KeyAdapter() {
				 
				 public void keyPressed(KeyEvent e) {
					 
					 if (help_mode) {
						 if (!field.getText().isBlank()) {
							 bt_add.setEnabled(true);
							 if (one) {
							 contentpad.setText(contentpad.getText() + "\nPress 'Add' button");
							 one = false;
							 }
							 
							 return;
						 }
					 }
					 
					 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						 block = true;
			       		 	bt_remove.setText(" Remove ");
			       		 
			       		 	if (content_list.getSelectedIndex() == -1) {
			       		 	String category = field.getText();
			       		 	if (category.isBlank()) {
								 return;
							 }
			   			 
			   			 for (int i = 0; i <= content_model.getSize() - 1; i++) {
								 if (category.equals(content_model.get(i))) {
									 System.out.println("error: [" +category+ "] already exists");
									 return;
								 }
							 }
			       		 		
			       		 	 content_model.addElement(category);
							 core.new_category(name, category);
							 //String content = text_content.getText();      // WTF?
							 core.gen_content(name, category, "");
							 field.setText("");
			       		 	}
			       		 	
			       		 	if (!sub) {
			       		 		String subcategory = field.getText();
			       		 		
					       		 	if (subcategory.isBlank()) {
										 return;
									 }
									 
									 for (int i = 0; i <= subcontent_model.getSize() - 1; i++) {
										 if (subcategory.equals(subcontent_model.get(i))) {
											 System.out.println("error: [" +subcategory+ "] already exists");
											 return;
										 }
									 }
			       		 		
			       		 		category = content_list.getSelectedValue();
			       		 	 subcontent_model.addElement(subcategory);
							 core.new_subcategory(name, category, subcategory);
							 core.sub_content(name, category, subcategory, "");
							 field.setText("");
			       		 	}
					 }
				 }
			 });
			 
			 field.addMouseListener(new MouseAdapter() {
				 public void mouseClicked(MouseEvent e) {
					 field.setText("");
				 }
			 });
			
	}
	
	public void projects_list() {
		plist = new JDialog(this, "Project Manager", true);
		plist.setSize(680,  370);
		plist.setMinimumSize(new Dimension(400, 240));
		plist.setLocationRelativeTo(this);
		plist.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		plist.setLayout(new BorderLayout());
		
			projects_model = new DefaultListModel<String>();
			list = new JList<String>(projects_model);
			
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					p_remove.setEnabled(true);
					p_open.setEnabled(true);
					
					if (list.getSelectedIndex() == -1) {
						p_remove.setEnabled(false);
						p_open.setEnabled(false);
					}
				}
			});
			
			var pane = new JScrollPane(list);
			
			pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			plist.add(pane, BorderLayout.CENTER);
			
				var panel = new JPanel();
				panel.setLayout(null);
				panel.setPreferredSize(new Dimension(120, 0));
			
					p_open.setBounds(10, 10, 100, 30);
					p_new.setBounds(10, 45, 100, 30);
					p_remove.setBounds(10, 80, 100, 30);
			
				panel.add(p_open);
				panel.add(p_new);
				panel.add(p_remove);
				
				p_remove.setEnabled(false);
				
				SwingUtilities.updateComponentTreeUI(p_open);
				SwingUtilities.updateComponentTreeUI(p_new);
				SwingUtilities.updateComponentTreeUI(p_remove);
			
			plist.add(panel, BorderLayout.EAST);
			
			
			core.load_projects();
			for(int i=0; i<core.projects.length; i++) {
				if (!core.projects[i].equals("config.ini")) {
					projects_model.addElement(core.projects[i]);
				}
		      }
			
			
			plist.setVisible(true);
			plist_init = true;
		
	}	
	
	
	public void themes(int arg) throws ClassNotFoundException, InstantiationException, 
							IllegalAccessException, UnsupportedLookAndFeelException {
		switch (arg) {
			case 0:
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(this);
				if (plist_init) {
					SwingUtilities.updateComponentTreeUI(plist);
				}
				
				break;
			case 1:
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
				SwingUtilities.updateComponentTreeUI(this);
				if (plist_init) {
					SwingUtilities.updateComponentTreeUI(plist);
				}
			
				break;
			case 2:
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				SwingUtilities.updateComponentTreeUI(this);
				if (plist_init) {
					SwingUtilities.updateComponentTreeUI(plist);
				}
				
				break;
			case 3:
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				SwingUtilities.updateComponentTreeUI(this);
				if (plist_init) {
					SwingUtilities.updateComponentTreeUI(plist);
				}
				
				break;
		}
	
	}
	
	
	public void switch_workspace() {
		sw_wspace = new JDialog(this, "Switch Workspace", true);
		sw_wspace.setSize(434, 130);
		sw_wspace.setLocationRelativeTo(this);
		sw_wspace.setLayout(null);
		sw_wspace.setResizable(false);
		
		var label = new JLabel("Workspace");
		
			label.setBounds(10, 17, 110, 30);
			sw_tf = new JTextField();
			sw_tf.setBounds(90, 15, 335, 35);	
			sw_close = new JButton("Cencel");
			sw_close.setBounds(355, 60, 70, 35);
			sw_set.setBounds(280, 60, 70, 35);
			
			sw_tf.setText(core.path);
		
			sw_wspace.add(sw_tf);
			sw_wspace.add(sw_set);
			sw_wspace.add(label);
			sw_wspace.add(sw_close);
			sw_wspace.setVisible(true);
		
	}
	
}
