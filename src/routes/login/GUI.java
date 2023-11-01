import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.math.BigDecimal;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
* @author: Sophia Dronova
*/
public class GUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	final static String WELCOME = "Welcome";
	final static String CASHIERS = "Cashiers";
	final static String INVENTORY = "Inventory";
	final static String MENU = "Menu";
	final static String TREND = "Trend";
	final static String CLOSE = "Close";
	
	final static String USAGE = "Usage";
	final static String SALES = "Sales";
	final static String EXCESS = "Excess";
	final static String RESTOCK = "Restock";
	final static String TOGETHER = "Together";


	enum Mode {
		Regular,
		Edit,
		Add
	};
	
	JFrame frame;
	JPanel cards;

	JLabel statusLabel;

	// Inventory gui elements
	JComboBox<String> managerComboBox;
	DefaultTableModel inventoryModel;
	JTable inventoryTable;
	JTextField totalRestockField;
	boolean inventoryEditMode;
	JButton inventoryEditButton;
	JButton inventoryRestoreButton;
	JButton inventoryUpdateButton;
	JButton inventoryAddIngredientButton;
	JButton inventoryRestockOrderButton;
	
	// Cashiers gui elements
	DefaultTableModel menuModel;
	JTable menuTable;
	JComboBox<Integer> amountComboBox;
	JComboBox<String> employeeComboBox;
	DefaultTableModel orderModel;
	JTable orderTable;
	JTextField totalTextField;
	
	// Menu gui elements
	JTable menuTable2;
	DefaultTableModel menuModel2;
	boolean menuEditMode;
	JButton menuEditButton;
	JButton menuRestoreButton;
	JButton menuUpdateButton;
	JButton menuAddItemButton;
	JTable menuTable3;
	DefaultTableModel menuModel3;
	JComboBox<String> ingredientComboBox;
	JTextField menuItemName;
	JTextField menuItemPrice;
	Box addMenuItemBox;
	
	// Trend gui elements
	JComboBox<String> reportComboBox;
	JTextField fromDateTextField;
	JTextField toDateTextField;
	JButton generateButton;
	JTextArea reportArea;
	
	// Data
	List<String[]> managers;
	List<String[]> employees;
	
	final int MENUITEM_ID = 0;
	final int MENUITEM_NAME = 1;
	final int MENUITEM_PRICE = 2;
	List<Object[]> menu_items;
	
	List<Object[]> order_items;
	
	final int INGREDIENT_ID = 0;
	final int INGREDIENT_NAME = 1;
	final int INGREDIENT_CURRENT = 2;
	final int INGREDIENT_NEEDED = 3;
	final int INGREDIENT_COST = 4;
	List<Object[]> ingredients;
	
	Map<Integer, Map<Integer, Integer>> menu_item_ingredients;
	
	List<Integer> new_item_ingredients;
	
	BigDecimal total_restock_price;
	
	Connection connection = null;

	public GUI () {
		// the gui is the frame
		frame = this;
		// create cards of panels
		cards = create_cards_panel();
		Box buttons = create_buttons_box();
		JPanel status = create_status_panel();
		
		add(buttons, BorderLayout.WEST);
		add(cards, BorderLayout.CENTER);
		add(status, BorderLayout.SOUTH);
	}

	/**
 	 * @return status a JPanel with a status label
 	 */
	// can report completed action or errors as status
	private JPanel create_status_panel() {
		statusLabel = new JLabel("Status");
		JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT));
		status.setBorder(new BevelBorder(BevelBorder.LOWERED));
		status.setBackground(Color.GRAY);
		status.add(statusLabel);
		return status;
	}
	
	/**
 	 * @param  message  a string which the label will be set to display
 	 */
	private void setStatus(String message) {
		statusLabel.setText(message);
	}

	/**
 	 * @return  panel  a JPanel which contains all text elements of the gui
 	 */
	private JPanel create_cards_panel() {
		// initialize all cards/panels
		JPanel welcomeCard = create_welcome_panel();
		JPanel inventoryCard = create_inventory_panel();
		JPanel trendCard = create_trend_panel();
		JPanel cashiersCard = create_cashiers_panel();
		JPanel menuCard = create_menu_panel();

		// panel with cardlayout
		JPanel panel = new JPanel(new CardLayout());  
		panel.add(welcomeCard, WELCOME);
		panel.add(cashiersCard, CASHIERS);
		panel.add(inventoryCard, INVENTORY);
		panel.add(menuCard, MENU);
		panel.add(trendCard, TREND);

		// to make card active
		// initially, it is the welcome card
		CardLayout cl = (CardLayout)(panel.getLayout());
		cl.show(panel, WELCOME);
		
		return panel;
	}


	/**
 	 * @return buttons, a layout of the buttons in the left side of the gui
 	 */
	private Box create_buttons_box() {
		// create buttons to switch between cards
		// they are toggle buttons
		// only one button active at a time and synchronized with corresponding card
		Box buttons = Box.createVerticalBox();
		JToggleButton cashiersButton = new JToggleButton(CASHIERS);
		JToggleButton inventoryButton = new JToggleButton(INVENTORY);
		JToggleButton trendButton = new JToggleButton(TREND);
		JToggleButton menuButton = new JToggleButton(MENU);
		ButtonGroup buttonGroup = new ButtonGroup(); // buttons in group are synchronized
		buttonGroup.add(cashiersButton);
		buttonGroup.add(inventoryButton);
		buttonGroup.add(menuButton);
		buttonGroup.add(trendButton);
		
		// listener that switches to card associated with the toggle button that is pressed
		ActionListener cardButtonListener = new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = (String)e.getActionCommand(); // gets name of button
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, command); // show corresponding card
			}
		};
		
		// set action listener to each button
		inventoryButton.addActionListener(cardButtonListener);
		trendButton.addActionListener(cardButtonListener);
		cashiersButton.addActionListener(cardButtonListener);
		menuButton.addActionListener(cardButtonListener);
		
		JButton closeButton = new JButton(CLOSE);
		ActionListener closeButtonListener = new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		};
		closeButton.addActionListener(closeButtonListener);
		
		// make all buttons the same width
		Dimension dimension = new Dimension(200, 200);
		inventoryButton.setAlignmentX(CENTER_ALIGNMENT);
		inventoryButton.setMaximumSize(dimension);
		trendButton.setAlignmentX(CENTER_ALIGNMENT);
		trendButton.setMaximumSize(dimension);
		cashiersButton.setAlignmentX(CENTER_ALIGNMENT);
		cashiersButton.setMaximumSize(dimension);
		menuButton.setAlignmentX(CENTER_ALIGNMENT);
		menuButton.setMaximumSize(dimension);
		closeButton.setAlignmentX(CENTER_ALIGNMENT);
		closeButton.setMaximumSize(dimension);
		
		// add buttons to actual button panel
		buttons.add(cashiersButton);
		buttons.add(inventoryButton);
		buttons.add(menuButton);
		buttons.add(trendButton);
		buttons.add(Box.createVerticalGlue());
		buttons.add(closeButton);
		return buttons;
	}

	/**
 	 * @return panel a JPanel which contains the first things the user
  	 * sees after either entering the gui or clicking one of the buttons
  	 * on the side of the gui
 	 */
	// create panels
	private JPanel create_welcome_panel () {
		JLabel label = new JLabel("Welcome");
		Font labelFont = label.getFont();
		label.setFont(new Font(labelFont.getName(), Font.PLAIN, 100));
		label.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.LIGHT_GRAY);
		panel.add(label, BorderLayout.CENTER);
		return panel;
	}
	
	private void set_inventory_mode(Mode mode) {
		switch (mode) {
			case Regular:
				inventoryEditMode = false;
				inventoryEditButton.setEnabled(true);
				inventoryRestoreButton.setEnabled(false);
				inventoryUpdateButton.setEnabled(false);
				inventoryAddIngredientButton.setEnabled(true);
				inventoryRestockOrderButton.setEnabled(true);
				break;
			case Edit:
				inventoryEditMode = true;
				inventoryEditButton.setEnabled(false);
				inventoryRestoreButton.setEnabled(true);
				inventoryUpdateButton.setEnabled(true);
				inventoryAddIngredientButton.setEnabled(false);
				inventoryRestockOrderButton.setEnabled(false);
				break;
			default:
				break;
		}
	}

	/**
 	 * @return panel a JPanel containing all relevant information regarding restocking items in
	 * the ingredients table
 	 */	
	private JPanel create_inventory_panel () {
		managerComboBox = new JComboBox<String>();
		
		Box box1 = Box.createHorizontalBox();
		box1.setBorder(new EmptyBorder(0, 0, 10, 0));
		box1.setAlignmentX(LEFT_ALIGNMENT);
		box1.add(new JLabel("Ingredient's Inventory"));
		box1.add(Box.createRigidArea(new Dimension(15,5)));
		box1.add(new JLabel("Manager:"));
		box1.add(managerComboBox);		
		box1.setMaximumSize(new Dimension(380, 100));
		
		inventoryModel = new DefaultTableModel() {
		    private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
				if (column == 0 || column == 1 || column == 2) 
					return inventoryEditMode;
		       return false;
		    }
		};
		inventoryModel.addColumn("Name"); 
		inventoryModel.addColumn("Price ($)"); 
		inventoryModel.addColumn("Current Qty");
		inventoryModel.addColumn("Needed Qty");
		inventoryModel.addColumn("Restock Qty");
		inventoryModel.addColumn("Restock Price ($)");
		
		inventoryTable = new JTable(inventoryModel); 
		inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(inventoryTable);
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
			
		inventoryEditButton = new JButton("Edit");
		inventoryEditButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				set_inventory_mode(Mode.Edit);
			}
		});
		
		inventoryRestoreButton = new JButton("Restore");
		inventoryRestoreButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableCellEditor editor = inventoryTable.getCellEditor();
				if (editor != null)
					editor.stopCellEditing();
				populate_ingredients();
				set_inventory_mode(Mode.Regular);
			}
		});
		
		inventoryUpdateButton = new JButton("Update");
		inventoryUpdateButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableCellEditor editor = inventoryTable.getCellEditor();
				if (editor != null)
					editor.stopCellEditing();
				List<Object[]> list = select_updated_ingredients();
				if (list.size() == 0) {
					setStatus("No Ingredients Updated");
					return;
				}
				update_ingredients(list);
				populate_ingredients();
				populate_ingredients_combobox();
				populate_menu_items();
				populate_menu_items2();
				set_inventory_mode(Mode.Regular);
				setStatus("Ingredients Updated");
			}
		});
		
		inventoryAddIngredientButton = new JButton("Add Ingredient");
		inventoryAddIngredientButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				int ingredient_id = create_ingredient_id();
				String name = "Ingredient";
				int current_qty = 0;
				int needed_qty = 100;
				Date date = null;
				BigDecimal cost = new BigDecimal(1.00);
				boolean perishable = false;
				insert_ingredient(ingredient_id, name, current_qty, needed_qty, date, cost, perishable);
				Object[] objects = {ingredient_id, name, current_qty, needed_qty, cost};
				ingredients.add(objects);
				populate_ingredients();
				populate_ingredients_combobox();
				populate_menu_items();
				populate_menu_items2();
				set_inventory_mode(Mode.Regular);
			}
		});
		
		
		totalRestockField = new JTextField();
		totalRestockField.setMaximumSize(new Dimension(100, 25));
		totalRestockField.setPreferredSize(new Dimension(100, 25));
		totalRestockField.setText("$0.0");
		totalRestockField.setEditable(false);
		
		inventoryRestockOrderButton = new JButton("Place Restock Order");
		inventoryRestockOrderButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (total_restock_price.equals(new BigDecimal(0))) {
					setStatus("No Restock Order Needed");
					return;
				}
				String message = "Are you sure you want to place the restock order?";
				String title = "Place restock order";
				int result = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
		
				if (result == JOptionPane.YES_OPTION) {
					int index = managerComboBox.getSelectedIndex();
					int manager_id = Integer.valueOf(managers.get(index)[0]);
					place_restock_order(manager_id, total_restock_price);
				}
			}
		});

		Box box2 = Box.createHorizontalBox();
		box2.setBorder(new EmptyBorder(10, 0, 0, 0));
		box2.setAlignmentX(LEFT_ALIGNMENT);
		box2.add(inventoryEditButton);
		box2.add(Box.createRigidArea(new Dimension(5,5)));
		box2.add(inventoryRestoreButton);
		box2.add(Box.createRigidArea(new Dimension(5,5)));
		box2.add(inventoryUpdateButton);
		box2.add(Box.createRigidArea(new Dimension(5,5)));
		box2.add(inventoryAddIngredientButton);
		box2.add(Box.createHorizontalGlue());
		box2.add(new JLabel("Total Restock ($):"));
		box2.add(totalRestockField);
		box2.add(Box.createRigidArea(new Dimension(5,5)));
		box2.add(inventoryRestockOrderButton);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(box1);
		panel.add(scrollPane);
		panel.add(box2);
		
		set_inventory_mode(Mode.Regular);
		
		return panel;
	}

	/**
 	 * @return a list of objects for each item which the user updated in the ingredients table
	 * @throws exception if invalid needed quantity or price change value
 	 */
	  private List<Object[]> select_updated_ingredients() {
		List<Object[]> objects = new ArrayList<Object[]>();
		for (int row = 0; row < inventoryTable.getRowCount(); row++) {
			
			String name = (String)inventoryTable.getValueAt(row, 0);
			int needed = 0;
			BigDecimal cost;
			
			try {
				Object ocost = inventoryModel.getValueAt(row, 1);
				if (ocost instanceof String)
					cost = new BigDecimal((String)ocost);
				else
					cost = (BigDecimal)ocost;
			} catch (Exception e) {
				cost = (BigDecimal)ingredients.get(row)[INGREDIENT_COST];
				setStatus(String.format("Invalid Price at Row %s", row));
			}
			
			try {
				Object oneeded = inventoryTable.getValueAt(row, 2);
				if (oneeded instanceof String)
					needed = Integer.valueOf((String)oneeded);
				else
					needed = (Integer)oneeded;
			} catch (Exception e) {
				needed = (Integer)ingredients.get(row)[INGREDIENT_CURRENT];
				setStatus(String.format("Invalid Current Qty at Row %s", row));
			}
			
			if (((String)ingredients.get(row)[INGREDIENT_NAME]).equals(name) && 
				((Integer)ingredients.get(row)[INGREDIENT_CURRENT]).equals(needed) &&
				((BigDecimal)ingredients.get(row)[INGREDIENT_COST]).equals(cost)) {
				continue;
			}
			ingredients.get(row)[INGREDIENT_NAME] = name;
			ingredients.get(row)[INGREDIENT_CURRENT] = needed;
			ingredients.get(row)[INGREDIENT_COST] = cost;
			objects.add(ingredients.get(row));
				
		}
		return objects;
	}

	/**
 	 * @return a JPanel that contains the trend report for sales report, excess report, and restock report
	 * @throws exception if invalid dates are used from input
 	 */
	private JPanel create_trend_panel () {
		reportComboBox = new JComboBox<String>();
		reportComboBox.addItem(USAGE);
		reportComboBox.addItem(SALES);
		reportComboBox.addItem(EXCESS);
		reportComboBox.addItem(RESTOCK);
		reportComboBox.addItem(TOGETHER);		
		reportComboBox.setMaximumSize(new Dimension(100, 25));
		reportComboBox.addItemListener(new ItemListener() {
			@Override
		    public void itemStateChanged(ItemEvent event) {
		        reportArea.setText("");
		        String report_name = event.getItem().toString();
		        switch(report_name) {
		        	case EXCESS:
			        	toDateTextField.setText(LocalDate.now().toString());
			        	toDateTextField.setEditable(false);
			        	fromDateTextField.setEditable(true);
			        	break;
		        	case RESTOCK:
			        	fromDateTextField.setText("");
			        	fromDateTextField.setEditable(false);	
			        	toDateTextField.setText("");
			        	toDateTextField.setEditable(false);		        		
		        		break;
		        	default:
			        	fromDateTextField.setEditable(true);	
			        	toDateTextField.setEditable(true);
		        		break;
		        }
		        
		    }
		});
		
		fromDateTextField = new JTextField();
		fromDateTextField.setMaximumSize(new Dimension(70, 25));
		
		toDateTextField = new JTextField();
		toDateTextField.setMaximumSize(new Dimension(70, 25));
		
		generateButton = new JButton("Generate");
		generateButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				Date fromDate = null;
				Date toDate = null;
				if ((String)reportComboBox.getSelectedItem() != RESTOCK) {
					try {
						String dateString = fromDateTextField.getText();
						fromDate = Date.valueOf(dateString);					
					}
					catch (Exception ex) {
						setStatus("Invalid From Date Format");
						return;
					}
					
					try {
						String dateString = toDateTextField.getText();
						toDate = Date.valueOf(dateString);					
					}
					catch (Exception ex) {
						setStatus("Invalid To Date Format");
						return;
					}
					
					if (fromDate.after(toDate)) {
						setStatus("To Date is Before From Date");
						return;
					}
				}
				
				String report = "";
				
				switch ((String)reportComboBox.getSelectedItem()) {
					case USAGE:
						report = generateUsageChart(fromDate, toDate);
						break;
					case SALES:
						report = generateSalesReport(fromDate, toDate);
						break;
					case EXCESS:
						report = generateExcessReport(fromDate, toDate);
						break;
					case RESTOCK:
						report = generateRestockReport();
						break;
					case TOGETHER:
						report = generateTogetherReport(fromDate, toDate);
						break;
				}
				reportArea.setText(report);
				setStatus("Report Generated");
			}
		});
		
		JLabel reportLabel = new JLabel("Report:");
		JLabel fromDateLabel = new JLabel("From Date:");
		JLabel toDateLabel = new JLabel("To Date:");
		
		Box box = Box.createHorizontalBox();
		box.setBorder(new EmptyBorder(0, 0, 10, 0));
		box.setAlignmentX(LEFT_ALIGNMENT);
		box.add(reportLabel);
		box.add(Box.createRigidArea(new Dimension(10, 10)));		
		box.add(reportComboBox);
		box.add(Box.createRigidArea(new Dimension(10, 10)));		
		box.add(fromDateLabel);
		box.add(Box.createRigidArea(new Dimension(10, 10)));
		box.add(fromDateTextField);
		box.add(Box.createRigidArea(new Dimension(10, 10)));		
		box.add(toDateLabel);
		box.add(Box.createRigidArea(new Dimension(10, 10)));		
		box.add(toDateTextField);
		box.add(Box.createRigidArea(new Dimension(10, 10)));		
		box.add(generateButton);
		
		reportArea = new JTextArea();
		reportArea.setMaximumSize(new Dimension(1000, 1000));
		reportArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
		
		JScrollPane scrollPane = new JScrollPane(reportArea);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		panel.add(box, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		return panel;
	}

	/**
 	 * @param fromDate the day where the report starts 
	 * @param toDate the day where the report ends
	 * @return a string to text which is formatted as a usage report
 	 */
	private String generateUsageChart(Date fromDate, Date toDate) {
		List<Object[]> usage = get_usage(fromDate, toDate);
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Usage Chart From %s To %s\n", fromDate, toDate));
		builder.append("\n");
		builder.append(String.format("%50s   | %7s\n", "Name", "Amount"));
		builder.append("-------------------------------------------------------------------------------------------------------\n");
		for (Object[] objects : usage) {
			builder.append(String.format("%50s   | %7s\n", objects[0], objects[1]));
		}
		return builder.toString();
	}

	/**
 	 * @param fromDate the day where the report starts 
	 * @param toDate the day where the report ends
	 * @return a string to text which is formatted as a together report
 	 */
	private String generateTogetherReport(Date fromDate, Date toDate) {
		List<Object[]> together = get_together(fromDate, toDate);
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("What Sells Together Report From %s To %s\n", fromDate, toDate));
		builder.append("\n");
		builder.append(String.format("%40s   | %40s   | %10s\n", "Name", "Name", "Frequency"));
		builder.append("-------------------------------------------------------------------------------------------------------\n");
		for (Object[] objects : together) {
			builder.append(String.format("%40s   | %40s   | %7s\n", objects[0], objects[1], objects[2]));
		}
		
		return builder.toString();
	}

	/**
	 * @return a string to text which is formatted as an excess report for which items have enough quantity
 	 */
	private String generateRestockReport() {
		List<Object[]> restock = get_restock();
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Restock Report\n"));
		builder.append("\n");
		builder.append(String.format("%50s   | %12s | %10s\n", "Name", "Current Qty", "Minimum Qty"));
		builder.append("--------------------------------------------------------------------------------------\n");
		for (Object[] objects : restock) {
			builder.append(String.format("%50s   | %12s | %10s\n", objects[0], objects[1], objects[2]));
		}
		return builder.toString();
	}

	/**
 	 * @param fromDate the day where the report starts 
	 * @param toDate the day where the report ends
	 * @return a string to text which is formatted as a report
 	 */
	private String generateExcessReport(Date fromDate, Date toDate) {
		List<Object[]> excess = get_excess(fromDate, toDate);
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Excess Report From %s To %s\n", fromDate, toDate));
		builder.append("\n");
		builder.append(String.format("%50s   | %7s | %7s\n", "Name", "Amount", "Ten Percent"));
		builder.append("--------------------------------------------------------------------------------------\n");
		for (Object[] objects : excess) {
			builder.append(String.format("%50s   | %7s | %7s\n", objects[0], objects[1], objects[2]));
		}
		
		return builder.toString();
	}

	/**
 	 * @param fromDate the day where the report starts 
	 * @param toDate the day where the report ends
	 * @return a string to text which is formatted as a report
 	 */
	private String generateSalesReport(Date fromDate, Date toDate) {
		List<Object[]> sales = get_sales(fromDate, toDate);
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Sales Report From %s To %s\n", fromDate, toDate));
		builder.append("\n");
		builder.append(String.format("%40s   | %7s\n", "Name", "Amount"));
		builder.append("--------------------------------------------------------------------------------------\n");
		for (Object[] objects : sales) {
			builder.append(String.format("%40s   | %7s\n", objects[0], objects[1]));
		}
		
		return builder.toString();
	}

	/**
	 * @return panel a JPanel which encapsulates the menu which the cashier interacts with
 	 */
	private JPanel create_menu_panel() {
		Box menuBox2 = create_menu_box2();
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS)); 
		panel.setBackground(Color.LIGHT_GRAY);
		panel.add(menuBox2);
		return panel;
	}

// 	private Box create_menu_list_box () {
// 		JTable table = new JTable();
// 		table.setModel(new PostgresTableModel(conn)); // Connect to database

// 		JButton commitButton = new JButton("Commit Edits");
// 		commitButton.addActionListener(e -> {
//   		PostgresTableModel model = (PostgresTableModel) table.getModel();
//   		model.commitChanges(); 
//   		model.refreshData(); // Reload table data from database
// }
// 	}
	
	/**
 	 * @param mode an option which determines which buttons can be clicked and which cells can be edited
 	 */
	private void set_menu_mode(Mode mode) {
		switch (mode) {
			case Regular:
				menuEditMode = false;
				menuEditButton.setEnabled(true);
				menuRestoreButton.setEnabled(false);
				menuUpdateButton.setEnabled(false);
				menuAddItemButton.setEnabled(true);
				addMenuItemBox.setVisible(false);
				break;
			case Edit:
				menuEditMode = true;
				menuEditButton.setEnabled(false);
				menuRestoreButton.setEnabled(true);
				menuUpdateButton.setEnabled(true);
				menuAddItemButton.setEnabled(false);
				addMenuItemBox.setVisible(false);
				break;
			case Add:
				menuEditMode = false;
				menuEditButton.setEnabled(false);
				menuRestoreButton.setEnabled(false);
				menuUpdateButton.setEnabled(false);
				menuAddItemButton.setEnabled(false);
				addMenuItemBox.setVisible(true);
				break;
		}
	}

	/**
	 * @return box a Box which contains all GUI elements of the menu such as buttons, text, panels and tables
 	 */
	private Box create_menu_box2 () {
		JLabel label1 = new JLabel("Menu");
		label1.setAlignmentX(LEFT_ALIGNMENT);
		
		menuModel2 = new DefaultTableModel() {
		    private static final long serialVersionUID = 1L;
		    
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return menuEditMode;
		    }
		};
		
		menuModel2.addColumn("Item"); 
		menuModel2.addColumn("Price ($)"); 
		menuTable2 = new JTable(menuModel2);
		menuTable2.getColumnModel().getColumn(1).setMaxWidth(60);
		menuTable2.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		JScrollPane scrollPanel2 = new JScrollPane(menuTable2);
		scrollPanel2.setAlignmentX(LEFT_ALIGNMENT);
		scrollPanel2.setMaximumSize(new Dimension(350, 1000));
		
		menuEditButton = new JButton("Edit");
		menuEditButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				set_menu_mode(Mode.Edit);
			}
		});
		
		menuRestoreButton = new JButton("Restore");
		menuRestoreButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableCellEditor editor = menuTable2.getCellEditor();
				if (editor != null)
					editor.stopCellEditing();
				populate_menu_items2();
				set_menu_mode(Mode.Regular);
			}
		});
		
		menuUpdateButton = new JButton("Update");
		menuUpdateButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableCellEditor editor = menuTable2.getCellEditor();
				if (editor != null)
					editor.stopCellEditing();
				List<Object[]> list = select_updated_menu_items();
				if (list.size() == 0) {
					setStatus("No Items Updated");
					return;
				}
				update_menu_items(list);
				populate_menu_items();
				populate_menu_items2();
				set_menu_mode(Mode.Regular);
				setStatus("Items Updated");
			}

		});
		
		menuAddItemButton = new JButton("Add Item");
		menuAddItemButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				set_menu_mode(Mode.Add);
			}
		});
		
		Box box1 = Box.createHorizontalBox();
		box1.setAlignmentX(LEFT_ALIGNMENT);
		box1.add(menuEditButton);
		box1.add(Box.createRigidArea(new Dimension(5,5)));
		box1.add(menuRestoreButton);
		box1.add(Box.createRigidArea(new Dimension(5,5)));
		box1.add(menuUpdateButton);
		box1.add(Box.createRigidArea(new Dimension(5,5)));
		box1.add(menuAddItemButton);
		
		Box boxv1 = Box.createVerticalBox();
		boxv1.add(label1);
		boxv1.add(Box.createRigidArea(new Dimension(5,5)));
		boxv1.add(scrollPanel2);
		boxv1.add(Box.createRigidArea(new Dimension(5,5)));
		boxv1.add(box1);	
		
		JLabel label2 = new JLabel("Adding New Item");
		label2.setAlignmentX(LEFT_ALIGNMENT);
		
		menuItemName = new JTextField();
		
		Box box2 = Box.createHorizontalBox();
		box2.setAlignmentX(LEFT_ALIGNMENT);
		box2.add(new JLabel("Name:"));
		box2.add(menuItemName);
		box2.setMaximumSize(new Dimension(450, 40));
		
		menuItemPrice = new JTextField();
		
		Box box3 = Box.createHorizontalBox();
		box3.setAlignmentX(LEFT_ALIGNMENT);
		box3.add(new JLabel("Price:"));
		box3.add(menuItemPrice);
		box3.setMaximumSize(new Dimension(450, 40));
		
		ingredientComboBox = new JComboBox<String>();
		ingredientComboBox.setMaximumSize(new Dimension(450, 40));
		
		Box box4 = Box.createHorizontalBox();
		box4.setAlignmentX(LEFT_ALIGNMENT);
		box4.add(new JLabel("Ingredient:"));
		box4.add(ingredientComboBox);
		
		JButton addIngredientButton = new JButton("Add Ingredient");
		addIngredientButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = ingredientComboBox.getSelectedIndex();
				int ingredient_id = (Integer)ingredients.get(index)[0];
				if (new_item_ingredients.contains(ingredient_id)) {
					setStatus("Ingredient is already added");
					return;
				}
				String name = (String)ingredientComboBox.getSelectedItem();
				Object[] objects = {name};
				menuModel3.addRow(objects);
				new_item_ingredients.add(ingredient_id);
			}
		});
		
		Box box5 = Box.createHorizontalBox();
		box5.setAlignmentX(LEFT_ALIGNMENT);
		box5.add(new JLabel("Ingredients"));
		box5.add(Box.createRigidArea(new Dimension(227,5)));
		box5.add(addIngredientButton);
		
		menuModel3 = new DefaultTableModel() {
		    private static final long serialVersionUID = 1L;
		    
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		menuModel3.addColumn("Name"); 
		menuTable3 = new JTable(menuModel3);
		
		JScrollPane scrollPanel3 = new JScrollPane(menuTable3);
		scrollPanel3.setAlignmentX(LEFT_ALIGNMENT);
		scrollPanel3.setMaximumSize(new Dimension(450, 1000));
		
		JButton addButton = new JButton("Add"); 
		addButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = menuItemName.getText();
				String sprice = menuItemPrice.getText();
				BigDecimal price;
				try {
					price = new BigDecimal(sprice);
				} catch (Exception ex) {
					setStatus("Price is invalid");
					return;
				}
				int menuitem_id = create_menu_item_id();
				insert_menu_item(menuitem_id, name, price);
				insert_item_ingredients(menuitem_id, new_item_ingredients);
				insert_menu_item_everywhere(menuitem_id, name, price, new_item_ingredients);
				
				menuItemName.setText("");
				menuItemPrice.setText("");
				menuModel3.setRowCount(0);
				new_item_ingredients.clear();
				set_menu_mode(Mode.Regular);
			}
		});
		
		JButton clearButton = new JButton("Clear"); 
		clearButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuItemName.setText("");
				menuItemPrice.setText("");
				menuModel3.setRowCount(0);
				new_item_ingredients.clear();
			}
		});
		
		JButton cancelButton = new JButton("Cancel"); 
		cancelButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuItemName.setText("");
				menuItemPrice.setText("");
				menuModel3.setRowCount(0);
				new_item_ingredients.clear();
				set_menu_mode(Mode.Regular);
			}
		});
		
		Box box6 = Box.createHorizontalBox();
		box6.setAlignmentX(LEFT_ALIGNMENT);
		box6.add(addButton);
		box6.add(Box.createRigidArea(new Dimension(5,5)));
		box6.add(clearButton);
		box6.add(Box.createRigidArea(new Dimension(5,5)));
		box6.add(cancelButton);
		
		addMenuItemBox = Box.createVerticalBox();
		addMenuItemBox.add(label2);
		addMenuItemBox.add(Box.createRigidArea(new Dimension(5,5)));
		addMenuItemBox.add(box2);
		addMenuItemBox.add(Box.createRigidArea(new Dimension(5,5)));
		addMenuItemBox.add(box3);
		addMenuItemBox.add(Box.createRigidArea(new Dimension(5,5)));
		addMenuItemBox.add(box4);
		addMenuItemBox.add(Box.createRigidArea(new Dimension(5,5)));
		addMenuItemBox.add(box5);
		addMenuItemBox.add(Box.createRigidArea(new Dimension(5,5)));
		addMenuItemBox.add(scrollPanel3);
		addMenuItemBox.add(Box.createRigidArea(new Dimension(5,5)));
		addMenuItemBox.add(box6);
		addMenuItemBox.setVisible(false);
		
		Box box = Box.createHorizontalBox();
		box.setBorder(new EmptyBorder(10, 10, 10, 10));
		box.add(boxv1);
		box.add(Box.createRigidArea(new Dimension(10,10)));
		box.add(addMenuItemBox);	
		
		set_menu_mode(Mode.Regular);
		
		return box;
	}

	/**
	 * @param menuitem_id the unique id of the menu item the user is adding
	 * @param name the name of the menu item the user is adding
	 * @param price the price of the menu item the user is adding
	 * @param new_item_ingredients a list of ids of the ingredients which are used in the menu item 
 	 */
	private void insert_menu_item_everywhere(int menuitem_id, String name, BigDecimal price, List<Integer> new_item_ingredients) {
		// insert to menu_items
		Object[] objects = {menuitem_id, name, price};
		menu_items.add(objects);
		Object[] rowObjects = {name, price};
		// add to tableModel
		menuModel.addRow(rowObjects);
		// add to tableModel2
		menuModel2.addRow(rowObjects);
		
		// insert to menu_item_ingredients
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int ingredient : new_item_ingredients) {
			map.put(ingredient, 1);
		}
		menu_item_ingredients.put(menuitem_id, map);
	}

	/**
 	 * @return a list of objects for each item which the user updated in the ingredients table
	 * @throws exception if invalid price change value
 	 */
	private List<Object[]> select_updated_menu_items() {
		List<Object[]> objects = new ArrayList<Object[]>();
		for (int row = 0; row < menuModel2.getRowCount(); row++) {
			String name = (String)menuModel2.getValueAt(row, 0);
			BigDecimal price;
			
			try {
				price = new BigDecimal((String)menuModel2.getValueAt(row, 1));
			} catch (Exception e) {
				price = (BigDecimal)menu_items.get(row)[MENUITEM_PRICE];
				setStatus(String.format("Invalid Price at Row %s", row));
			}
			
			if (((String)menu_items.get(row)[MENUITEM_NAME]).equals(name) && 
					((BigDecimal)menu_items.get(row)[MENUITEM_PRICE]).equals(price)) {
				continue;
			}
			menu_items.get(row)[MENUITEM_NAME] = name;
			menu_items.get(row)[MENUITEM_PRICE] = price;
			objects.add(menu_items.get(row));
				
		}
		return objects;
	}
	
  	/**
 	 * @return box a BoxLayout panel that is the layout of displayable
  	 * information from the table
 	 */
	private Box create_menu_box () {
		JLabel label = new JLabel("Menu");
		label.setAlignmentX(LEFT_ALIGNMENT);
		
		menuModel = new DefaultTableModel() {
		    private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		menuModel.addColumn("Item"); 
		menuModel.addColumn("Price ($)"); 
		menuTable = new JTable(menuModel); 
		menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		menuTable.getColumnModel().getColumn(1).setMaxWidth(60);
		menuTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		JScrollPane scrollPane = new JScrollPane(menuTable);
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		
		Box box = Box.createVerticalBox();
		box.setBorder(new EmptyBorder(10, 10, 10, 10));
		box.add(label);
		box.add(Box.createRigidArea(new Dimension(5,5)));
		box.add(scrollPane);
		
		return box;
	}

  	/**
 	 * @return box to contain an action button for either cashier or managers
 	 */	
	private Box create_command_box () {
		Integer[] amounts = {1, 2, 3, 4, 5, 6, 12, 18, 24};
		amountComboBox = new JComboBox<Integer>(amounts);
		
		Box amountBox = Box.createHorizontalBox();
		amountBox.add(new JLabel("Amount:"));
		amountBox.add(amountComboBox);
		amountBox.setMaximumSize(new Dimension(120, 120));
		amountBox.setAlignmentX(LEFT_ALIGNMENT);
		
		JButton addMenuItemButton = new JButton("Add Item");
		addMenuItemButton.setMaximumSize(new Dimension(120, 120));
		addMenuItemButton.setAlignmentX(LEFT_ALIGNMENT);
		addMenuItemButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = menuTable.getSelectedRow();
				int amount = (Integer)amountComboBox.getSelectedItem();
				add_menu_item_to_order(index, amount);
			}
		});
		
		Box box = Box.createVerticalBox();
		box.setBorder(new EmptyBorder(10, 0, 10, 0));
		box.add(Box.createVerticalGlue());
		box.add(amountBox);
		box.add(Box.createRigidArea(new Dimension(5,5)));
		box.add(addMenuItemButton);
		box.add(Box.createVerticalGlue());
			
		return box;
	}

	/**
 	 * @return box a display for order information on the customer (cashier) side
 	 */
	private Box create_order_box () {
		employeeComboBox = new JComboBox<String>();
		
		Box box1 = Box.createHorizontalBox();
		box1.setAlignmentX(LEFT_ALIGNMENT);
		box1.add(new JLabel("Order"));
		box1.add(Box.createRigidArea(new Dimension(15,5)));
		box1.add(new JLabel("Employee:"));
		box1.add(employeeComboBox);		
		box1.setMaximumSize(new Dimension(300, 100));
		
		orderModel = new DefaultTableModel() {
		    private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		orderModel.addColumn("Item"); 
		orderModel.addColumn("Amount"); 
		orderModel.addColumn("Price ($)"); 
		
		orderTable = new JTable(orderModel); 
		orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orderTable.getColumnModel().getColumn(1).setMaxWidth(60);
		orderTable.getColumnModel().getColumn(2).setMaxWidth(60);
		orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		JScrollPane scrollPane = new JScrollPane(orderTable);
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
			
		JButton clearOrderButton = new JButton("Clear Order");
		clearOrderButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear_order("Order is cleared.");
			}
		});
		JButton removeMenuItemButton = new JButton("Remove Item");
		removeMenuItemButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = orderTable.getSelectedRow();
				remove_menu_item_from_order(index);
			}
		});
		
		Box box2 = Box.createHorizontalBox();
		box2.setAlignmentX(LEFT_ALIGNMENT);
		box2.setMaximumSize(new Dimension(250, 100));
		box2.add(clearOrderButton);
		box2.add(Box.createRigidArea(new Dimension(5,5)));
		box2.add(removeMenuItemButton);
		
		totalTextField = new JTextField();
		totalTextField.setText("0.0");
		totalTextField.setEditable(false);
		JButton placeOrderButton = new JButton("Place Order");
		placeOrderButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (order_items.isEmpty()) {
					setStatus("No Order Items");
					return;
				}
				String message = "Are you sure you want to place the order?";
				String title = "Place order";
				int result = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
		
				if (result == JOptionPane.YES_OPTION) {
					int index = employeeComboBox.getSelectedIndex();
					int employee_id = Integer.valueOf(employees.get(index)[0]);
					String total = totalTextField.getText();
					double cost = Double.valueOf(total);
					place_order(employee_id, cost);
				}
			}
		});
		
		Box box3 = Box.createHorizontalBox();
		box3.setAlignmentX(LEFT_ALIGNMENT);
		box3.setMaximumSize(new Dimension(250, 100));
		box3.add(new JLabel("Total ($):"));
		box3.add(totalTextField);
		box3.add(Box.createRigidArea(new Dimension(5,5)));
		box3.add(placeOrderButton);
		
		Box box = Box.createVerticalBox();
		box.setBorder(new EmptyBorder(10, 10, 10, 10));
		box.add(box1);
		box.add(Box.createRigidArea(new Dimension(5,5)));
		box.add(scrollPane);
		box.add(Box.createRigidArea(new Dimension(5,5)));
		box.add(box2);
		box.add(Box.createRigidArea(new Dimension(5,5)));
		box.add(box3);
		
		return box;
	}

	/**
	 * @return panel a JPanel for the cashier interface, showing information relevant to taking customer orders
	 */
	private JPanel create_cashiers_panel () {
		Box menuBox = create_menu_box();
		Box commandBox = create_command_box();
		Box orderBox = create_order_box();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS)); 
		panel.setBackground(Color.LIGHT_GRAY);
		panel.add(menuBox);
		panel.add(commandBox);
		panel.add(orderBox);
		return panel;
	}

  	/**
 	 * @throws error if database cannot be accessed
 	 */
	private void create_db_connection() {
		try {
			String db_url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_903_03db";
			String db_username = "csce315_903_03user";
			String db_password = "lunar little lively";
			connection = DriverManager.getConnection(db_url, db_username, db_password);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			setStatus(e.getMessage());
			return;
		}
		setStatus("Opened database successfully.");
	}

	/**
 	 * @throws error if connection cannot be closed for whatever reason
 	 */
	private void close_db_connection() {
		try {
			connection.close();
		} catch(Exception e) {
			setStatus("Connection NOT Closed.");
			return;
		}
		setStatus("Connection Closed.");
	}

  	/**
 	 * @return list an array of all menu item ids, prices, and names gotten from sql query
	 * @throws error if _menu_item cannot be accessed
 	 */
	private List<Object[]> get_menu_items() {
		List<Object[]> list = new ArrayList<Object[]>();
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT menu_item_id, name, price from _menu_item;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			while (result.next()) {
				Object[] objects = new Object[3];
				objects[0] = result.getInt("menu_item_id");
				objects[1] = result.getString("name");
				objects[2] = result.getBigDecimal("price");
				list.add(objects);
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Menu Items.");
		}
		return list;
	}

	/**
 	 * @return list an array of all ingredients from the inventory database
	 * @throws error if _ingredient cannot be accessed
 	 */
	private List<Object[]> get_ingredients() {
		List<Object[]> list = new ArrayList<Object[]>();
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT ingredient_id, name, current_qty, needed_qty, cost from _ingredient;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			while (result.next()) {
				Object[] objects = new Object[5];
				objects[INGREDIENT_ID] = result.getInt("ingredient_id");
				objects[INGREDIENT_NAME] = result.getString("name");
				objects[INGREDIENT_CURRENT] = result.getInt("current_qty");
				objects[INGREDIENT_NEEDED] = result.getInt("needed_qty");
				objects[INGREDIENT_COST] = result.getBigDecimal("cost");
				list.add(objects);
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Ingredients.");
		}
		return list;
	}
	
  	/**
 	 * @return map an object HashMap containing item ingredient values including ingredient ids, menu item ids, and their quantities
	 * @throws error if _item_ingredient cannot be accessed
 	 */
	private Map<Integer, Map<Integer, Integer>> get_item_ingredients() {
		Map<Integer, Map<Integer, Integer>> map = new HashMap<Integer, Map<Integer, Integer>>();
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT ingredient_id, menu_item_id, quantity from _item_ingredient;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			while (result.next()) {
				int ingredient_id = result.getInt("ingredient_id");
				int menu_item_id = result.getInt("menu_item_id");
				int quantity = result.getInt("quantity");
				if (!map.containsKey(menu_item_id))
					map.put(menu_item_id, new HashMap<Integer, Integer>());
				map.get(menu_item_id).put(ingredient_id, quantity);
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Item Ingredients.");
		}
		return map;
	}

	/**
 	 * @return list an array of employee details including id and names
	 * @throws error if _employee cannot be accessed
 	 */
	private List<String[]> get_employees() {
		List<String[]> list = new ArrayList<String[]>();
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT employee_id, name from _employee;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			while (result.next()) {
				String[] strings = new String[3];
				strings[0] = result.getString("employee_id");
				strings[1] = result.getString("name");
				list.add(strings);
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Employees.");
		}
		return list;
	}
	
  	/**
 	 * @return list of all menu item ids, prices, and names gotten from sql query
	 * @throws exception if _manager cannot be accessed
 	 */
	private List<String[]> get_managers() {
		List<String[]> list = new ArrayList<String[]>();
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT manager_id, name from _manager;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			while (result.next()) {
				String[] strings = new String[2];
				strings[0] = result.getString("manager_id");
				strings[1] = result.getString("name");
				list.add(strings);
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Managers.");
		}
		return list;
	}
	
	private List<Object[]> get_usage(Date fromDate, Date toDate) {
		List<Object[]> list = new ArrayList<Object[]>();
		PreparedStatement preparedStatement; 
		ResultSet result;
		try {
			String sql = """
				SELECT i.name AS name, SUM(oi.amount * ii.quantity) AS amount
				FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi, _item_ingredient AS ii, _ingredient AS i
				WHERE co.order_date >= ? AND
				      co.order_date <= ? AND
					  co.customer_order_id = oi.customer_order_id AND
				      oi.menu_item_id = mi.menu_item_id AND
				      mi.menu_item_id = ii.menu_item_id AND
				      ii.ingredient_id = i.ingredient_id
				GROUP BY i.name
				ORDER BY amount DESC;	  
						""";
			//create a statement object
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, fromDate);
			preparedStatement.setDate(2, toDate);
			// send statement to DBMS
			
			result = preparedStatement.executeQuery();
			while (result.next()) {
				Object[] objects = new Object[2];
				objects[0] = result.getString("name");
				objects[1] = result.getInt("amount");
				list.add(objects);
			}
			result.close();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Usage.");
		}
		return list;
	}

  	/**
	 * @param fromDate the date where the report begins
	 * @param toDate the date where the report ends
 	 * @return list of objects which contains data from customer orders
	 * @throws exception if database cannot be accessed
 	 */
	private List<Object[]> get_sales(Date fromDate, Date toDate) {
		List<Object[]> list = new ArrayList<Object[]>();
		PreparedStatement preparedStatement; 
		ResultSet result;
		try {
			String sql = """
					SELECT mi.name AS name, COUNT(oi.amount) AS amount
					FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi
					WHERE co.order_date >= ? AND
						  co.order_date <= ? AND 
						  co.customer_order_id = oi.customer_order_id AND
						  oi.menu_item_id = mi.menu_item_id
					GROUP BY mi.name
					ORDER BY COUNT(oi.amount) DESC;	  
						""";
			//create a statement object
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, fromDate);
			preparedStatement.setDate(2, toDate);
			// send statement to DBMS
			
			result = preparedStatement.executeQuery();
			while (result.next()) {
				Object[] objects = new Object[2];
				objects[0] = result.getString("name");
				objects[1] = result.getInt("amount");
				list.add(objects);
			}
			result.close();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Sales.");
		}
		return list;
	}
	
  	/**
	 * @param fromDate the date where the report begins
	 * @param toDate the date where the report ends
 	 * @return list of objects which contains data from excess ingredients
	 * @throws exception if database cannot be accessed
 	 */
	private List<Object[]> get_excess(Date fromDate, Date toDate) {
		List<Object[]> list = new ArrayList<Object[]>();
		PreparedStatement preparedStatement; 
		ResultSet result;
		try {
			String sql = """
				WITH temp (id, name, amount) AS
					(SELECT i.ingredient_id AS id, i.name AS name, SUM(oi.amount * ii.quantity) AS amount
					 FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi, _item_ingredient AS ii, _ingredient AS i
					 WHERE co.order_date >= ? AND
						   co.order_date <= ? AND
						   co.customer_order_id = oi.customer_order_id AND
						   oi.menu_item_id = mi.menu_item_id AND
						   mi.menu_item_id = ii.menu_item_id AND
						   ii.ingredient_id = i.ingredient_id
					 GROUP BY i.ingredient_id)
				
				SELECT temp.name AS name, temp.amount AS amount, ROUND(i.current_qty * 0.10, 0) AS ten_percent
				FROM temp, _ingredient AS i
				WHERE
					temp.id = i.ingredient_id AND
					temp.amount < ROUND(i.current_qty * 0.10, 0)	  
						""";
			//create a statement object
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, fromDate);
			preparedStatement.setDate(2, toDate);
			// send statement to DBMS
			
			result = preparedStatement.executeQuery();
			while (result.next()) {
				Object[] objects = new Object[3];
				objects[0] = result.getString("name");
				objects[1] = result.getInt("amount");
				objects[2] = result.getInt("ten_percent");
				list.add(objects);
			}
			result.close();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Excess.");
		}
		return list;
	}
	
  	/**
 	 * @return list of objects which contains data from ingredients needing restocking
	 * @throws exception if database cannot be accessed
 	 */
	private List<Object[]> get_restock() {
		List<Object[]> list = new ArrayList<Object[]>();
		Statement statement; 
		ResultSet result;
		try {
			String sql = """
				SELECT i.name AS name, i.current_qty AS current_qty, ROUND(i.needed_qty * 0.25, 0) AS minimum_qty
				FROM _ingredient AS i
				WHERE i.current_qty < i.needed_qty * 0.25;  
						""";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			while (result.next()) {
				Object[] objects = new Object[3];
				objects[0] = result.getString("name");
				objects[1] = result.getString("current_qty");
				objects[2] = result.getString("minimum_qty");
				list.add(objects);
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Restock.");
		}
		return list;
	}

  	/**
	 * @param fromDate the date where the report begins
	 * @param toDate the date where the report ends
 	 * @return list of objects which contains data from customer orders
	 * @throws exception if database cannot be accessed
 	 */
	private List<Object[]> get_together(Date fromDate, Date toDate) {
		List<Object[]> list = new ArrayList<Object[]>();
		PreparedStatement preparedStatement; 
		ResultSet result;
		try {
			String sql = """
				SELECT mi1.name AS name1, mi2.name AS name2, COUNT(*) AS frequency
				FROM
				    (SELECT co.customer_order_id, mi.menu_item_id, mi.name
				     FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi
				     WHERE co.order_date >= ? AND
				           co.order_date <= ? AND
				           co.customer_order_id = oi.customer_order_id AND
				           oi.menu_item_id = mi.menu_item_id) mi1
				JOIN
				    (SELECT co.customer_order_id, mi.menu_item_id, mi.name
				     FROM _customer_order AS co, _ordered_item AS oi, _menu_item AS mi
				     WHERE co.order_date >= ? AND
				           co.order_date <= ? AND
				           co.customer_order_id = oi.customer_order_id AND
				           oi.menu_item_id = mi.menu_item_id) mi2
				ON (mi1.menu_item_id < mi2.menu_item_id AND
					mi1.customer_order_id = mi2.customer_order_id)
				GROUP BY name1, name2
				ORDER BY frequency DESC;
						""";
			//create a statement object
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, fromDate);
			preparedStatement.setDate(2, toDate);
			preparedStatement.setDate(3, fromDate);
			preparedStatement.setDate(4, toDate);
			
			// send statement to DBMS
			
			result = preparedStatement.executeQuery();
			while (result.next()) {
				Object[] objects = new Object[3];
				objects[0] = result.getString("name1");
				objects[1] = result.getString("name2");
				objects[2] = result.getInt("frequency");
				list.add(objects);
			}
			result.close();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Get Together.");
		}
		return list;
	}

  	/**
 	 * @return customer_order_id a new id for the current customer order
	 * @throws exception if _customer_order cannot be accessed
 	 */
	private int create_customer_order_id() {
		int customer_order_id = 0;
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT COALESCE(MAX(customer_order_id), 0) + 1 AS new_id FROM _customer_order;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			if (result.next()) {
				customer_order_id = result.getInt("new_id");
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Create Customer ID.");
		}
		return customer_order_id;
	}
	
  	/**
 	 * @return restock_order_id a new id for the current restock order
	 * @throws exception if _restock_order cannot be accessed
 	 */
	private int create_restock_order_id() {
		int restock_order_id = 0;
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT COALESCE(MAX(restock_order_id), 0) + 1 AS new_id FROM _restock_order;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			if (result.next()) {
				restock_order_id = result.getInt("new_id");
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Create Restock ID.");
		}
		return restock_order_id;
	}
	
  	/**
 	 * @param customer_order_id a value unique to a singular customer order
	 * @param employee_id a value unique to a specific employee
	 * @param cost the cost of the order
	 * @param order_date the date on which the order was made
 	 * @param order_time the time at which the order was made
	 * @throws error if _customer_order cannot be accessed
 	 */
	private void insert_customer_order(int customer_order_id, int employee_id, double cost, Date order_date, Time order_time) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "INSERT into _customer_order (customer_order_id, employee_id, cost, order_date, order_time) values (?, ?, ?, ?, ?);";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, customer_order_id);
			preparedStatement.setInt(2, employee_id);
			preparedStatement.setBigDecimal(3, new BigDecimal(cost));
			preparedStatement.setDate(4, order_date);
			preparedStatement.setTime(5, order_time);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Insert Customer Order.");
		}
	}

  	/**
 	 * @param ingredient_id a value unique to a singular ingredient
	 * @param name the name of the new ingredient
	 * @param current_qty the current quantity of the ingredient
	 * @param needed_qty the quantity needed of the ingredient, will go to this value for restock
 	 * @param date the earliest date where the ingredient will expire
	 * @throws error if _ingredient can't be accessed
 	 */
	private void insert_ingredient(int ingredient_id, String name, int current_qty, int needed_qty, Date date,
			BigDecimal cost, boolean perishable) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "INSERT into _ingredient (ingredient_id, name, current_qty, needed_qty, expiration_date,  cost, perishable) values (?, ?, ?, ?, ?, ?, ?);";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, ingredient_id);
			preparedStatement.setString(2, name);
			preparedStatement.setInt(3, current_qty);
			preparedStatement.setInt(4, needed_qty);
			preparedStatement.setDate(5, date);
			preparedStatement.setBigDecimal(6, cost);
			preparedStatement.setBoolean(7, perishable);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Insert Ingredient.");
		}
	}

  	/**
 	 * @return ingredient_id the newest id for an ingredient available
	 * @throws exception if the database for ingredients cannot be accessed
 	 */
	private int create_ingredient_id() {
		int ingredient_id = 0;
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT COALESCE(MAX(ingredient_id), 0) + 1 AS new_id FROM _ingredient;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			if (result.next()) {
				ingredient_id = result.getInt("new_id");
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Create Ingredient ID.");
		}
		return ingredient_id;
	}

  	/**
 	 * @param menu_item_id the id of the menu item using these ingredients
	 * @param new_item_ingredients a list of integer values pertaining to the _item_ingredient row
	 * @throws exception if database cannot be accessed
  	 */
	private void insert_item_ingredients(int menu_item_id, List<Integer> new_item_ingredients) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "INSERT into _item_ingredient (ingredient_id, menu_item_id, quantity) values (?, ?, ?);";
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);
			int quantity = 1;
			for (Integer ingridient_id : new_item_ingredients) { 
				preparedStatement.setInt(1, ingridient_id);
				preparedStatement.setInt(2, menu_item_id);
				preparedStatement.setInt(3, quantity);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Insert Item Ingredients.");
		}
	}

  	/**
 	 * @param menu_item_id the id of the menu item using these ingredients
	 * @param new_item_ingredients a list of integer values pertaining to the _item_ingredient row
	 * @throws exception if _menu_item database cannot be accessed
  	 */
	private void insert_menu_item(int menu_item_id, String name, BigDecimal price) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "INSERT into _menu_item (menu_item_id, name, price) values (?, ?, ?);";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, menu_item_id);
			preparedStatement.setString(2, name);
			preparedStatement.setBigDecimal(3, price);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Insert Menu Item.");
		}
	}

	/**
 	 * @return menu_item_id the next available id for a new menu item 
	 * @throws exception if database for _menu_item cannot be accessed
  	 */
	private int create_menu_item_id() {
		int menu_item_id = 0;
		Statement statement; 
		ResultSet result;
		try {
			String sql = "SELECT COALESCE(MAX(menu_item_id), 0) + 1 AS new_id FROM _menu_item;";
			//create a statement object
			statement = connection.createStatement();
			// send statement to DBMS
			
			result = statement.executeQuery(sql);
			if (result.next()) {
				menu_item_id = result.getInt("new_id");
			}
			result.close();
			statement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Create Menuitem ID.");
		}
		return menu_item_id;
	}

 	/**
 	 * @param restock_order_id a value unique to a singular restock order
	 * @param manager_id a value unique to a specific manager
	 * @param date the date on which the order was made
 	 * @param total_restock_price the total cost of restocking every inventory value to necessary quantity
	 * @throws exception if _restock_order cannot be accessed
 	 */
	private void insert_restock_order(int restock_order_id, int manager_id, Date date, BigDecimal total_restock_price) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "INSERT into _restock_order (restock_order_id, manager_id, restock_date, cost) values (?, ?, ?, ?);";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, restock_order_id);
			preparedStatement.setInt(2, manager_id);
			preparedStatement.setDate(3, date);
			preparedStatement.setBigDecimal(4, total_restock_price);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Insert Restock Order.");
		}
	}

	/**
 	 * @param customer_order_id a value unique to a singular customer order
	 * @param list an array of all items ordered in one customer order id
	 * @throws exception if _ordered_item cannot be accessed
 	 */
	private void insert_ordered_items(int customer_order_id, List<Integer[]> list) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "INSERT into _ordered_item (customer_order_id, menu_item_id, amount) values (?, ?, ?);";
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);
			for (Integer[] values: list) { 
				preparedStatement.setInt(1, customer_order_id);
				preparedStatement.setInt(2,values[0]);
				preparedStatement.setInt(3, values[1]);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Insert Ordered Items.");
		}
	}
	
  	/**
 	 * @param restock_order_id a value unique to a singular restock order
 	 * @param list an array of all items pertaining to one restock order id
	 * @throws error if _restock_ingredient cannot be accessed
 	 */
	private void insert_restocked_ingredients(int restock_order_id, List<Integer[]> list) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "INSERT into _restock_ingredient (ingredient_id, restock_order_id, quantity) values (?, ?, ?);";
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);
			for (Integer[] values: list) { 
				preparedStatement.setInt(1, values[0]);
				preparedStatement.setInt(2,restock_order_id);
				preparedStatement.setInt(3, values[1]);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Insert Restocked Ingredients.");
		}
	}

	private void populate() {
		ingredients = get_ingredients();
		menu_item_ingredients = get_item_ingredients();
		menu_items = get_menu_items();
		populate_menu_items();
		populate_menu_items2();
		populate_employees();
		populate_managers();
		populate_ingredients();
		populate_ingredients_combobox();
		order_items = new ArrayList<Object[]>();
		new_item_ingredients = new ArrayList<Integer>();
	}
	
	private void populate_ingredients_combobox() {
		ingredientComboBox.removeAllItems();
		for (Object[] ingredient : ingredients) {
			ingredientComboBox.addItem((String)ingredient[INGREDIENT_NAME]);	
		}
	}

	private void populate_employees() {
		employees = get_employees();
		for (String[] employee : employees) {
			employeeComboBox.addItem(employee[1]);	
		}
	}
	
	private void populate_managers() {
		managers = get_managers();
		for (String[] manager : managers) {
			managerComboBox.addItem(manager[1]);	
		}
		
	}

	private void populate_ingredients() {
		inventoryModel.setRowCount(0);
		total_restock_price = new BigDecimal(0.0);
		for (Object[] ingredient : ingredients) {
			int needed_qty = (Integer)ingredient[INGREDIENT_NEEDED];
			int current_qty = (Integer)ingredient[INGREDIENT_CURRENT];
			BigDecimal price = (BigDecimal)ingredient[INGREDIENT_COST];
			int restock_qty = (needed_qty > current_qty) ? needed_qty - current_qty : 0;
			BigDecimal restock_price = BigDecimal.valueOf(restock_qty).multiply(price);
			total_restock_price = total_restock_price.add(restock_price);
			inventoryModel.addRow(new Object[] {ingredient[INGREDIENT_NAME], price, current_qty, needed_qty, restock_qty, restock_price});
		}
		totalRestockField.setText(total_restock_price.toString());
	}
	
	private void populate_menu_items() {
		menuModel.setRowCount(0);
		for (Object[] menu_item : menu_items) {
			menuModel.addRow(new Object[] {menu_item[1], menu_item[2]});
		}
	}

	private void populate_menu_items2() {
		menuModel2.setRowCount(0);
		for (Object[] menu_item : menu_items) {
			menuModel2.addRow(new Object[] {menu_item[1], menu_item[2]});
		}
	}

	/**
 	 * @param index a value which describes item location within the list of _menu_items
	 * @param amount the quantity of items ordered
 	 */
	private void add_menu_item_to_order(int index, int amount) {
		if (index == -1) {
			setStatus("No Menu Item is selected");
			return;
		}
		Object[] menu_item = menu_items.get(index);
		BigDecimal price_unit = (BigDecimal)menu_item[2];
		BigDecimal price = price_unit.multiply(new BigDecimal(amount));
		Object[] order_item = { menu_item[0], menu_item[1], amount, price};
		orderModel.addRow(new Object[] {order_item[1], order_item[2], order_item[3]});
		order_items.add(order_item);
		update_total();
		setStatus("Menu item is added to order.");
	}

	/**
 	 * @param message a message to be displayed to the user in the status label
 	 */
	private void clear_order(String message) {
		orderModel.setRowCount(0);
		order_items.clear();
		update_total();
		setStatus(message);
	}

	/**
 	 * @param index a value which describes item location within the list of the order
 	 */
	private void remove_menu_item_from_order(int index) {
		if (index == -1) {
			setStatus("No Order Item is selected.");
			return;
		}
		orderModel.removeRow(index);
		order_items.remove(index);
		update_total();
		setStatus("Order Item is removed.");
	}

	/**
 	 * @param employee_id a unique value for a specific employee
	 * @param cost the total cost of a customer order
 	 */
	private void place_order(int employee_id, double cost) {
		Date date = Date.valueOf(LocalDate.now());
		Time time = Time.valueOf(LocalTime.now());
		int customer_order_id = create_customer_order_id();
		insert_customer_order(customer_order_id, employee_id, cost, date, time);
		Map<Integer, Integer> used_ingredients = new HashMap<Integer, Integer>();
		List<Integer[]> list = new ArrayList<Integer[]>();
		for (Object[] order_item : order_items) {
			int menu_item_id = (Integer)order_item[0];
			int amount = (Integer)order_item[2];
			list.add(new Integer[] {menu_item_id, amount});
			add_menu_items_to_used_ingredients(used_ingredients, menu_item_id, amount);
		}
		insert_ordered_items(customer_order_id, list);
		List<Integer[]> new_current_ingredient_qty = retrieve_updated_ingredients_qty(used_ingredients);
		update_ingredients_current_qty(new_current_ingredient_qty);
		populate_ingredients();
		clear_order("Order is placed.");
	}

	/**
  	 * @param used_ingredients an object holding the old and new values of edited ingredients
	 * @return a list of integer arrays each pertaining to a row of ingredients
 	 */
	private List<Integer[]>  retrieve_updated_ingredients_qty(Map<Integer, Integer> used_ingredients) {
		List<Integer[]> list = new ArrayList<Integer[]>();
		for (Object[] objects : ingredients) {
			int ingredient_id = (Integer)objects[INGREDIENT_ID];
			int quantity = (Integer)objects[INGREDIENT_CURRENT];
			if (used_ingredients.containsKey(ingredient_id)) {
				int used_quantity = used_ingredients.get(ingredient_id);
				list.add(new Integer[] {ingredient_id, quantity - used_quantity});
				objects[INGREDIENT_CURRENT] = quantity - used_quantity;
			}
		}
		return list;
	}

	/**
 	 * @param used_ingredients an object holding the old and new values of edited ingredients
	 * @param menu_item_id the menu item id that the ingredients were used in making
	 * @param amount the amount of times the menu item was made
 	 */
	private void add_menu_items_to_used_ingredients(Map<Integer, Integer> used_ingredients, int menu_item_id, int amount) {
		Map<Integer, Integer> map = menu_item_ingredients.get(menu_item_id);
		for (Integer ingredient_id :map.keySet()) {
			if (!used_ingredients.containsKey(ingredient_id)) {
				used_ingredients.put(ingredient_id, 0);
			}
			int used_quantity = used_ingredients.get(ingredient_id);
			used_ingredients.put(ingredient_id, amount * map.get(ingredient_id) + used_quantity);
		}
	}

	/**
 	 * @param manager_id a unique value for a specific manager
	 * @param total_restock_price the total cost of the restock order
 	 */
	private void place_restock_order(int manager_id, BigDecimal total_restock_price) {
		Date date = Date.valueOf(LocalDate.now());
		int restock_order_id = create_restock_order_id();
		insert_restock_order(restock_order_id, manager_id, date, total_restock_price);
		List<Integer[]> restock_list = new ArrayList<Integer[]>();
		List<Integer[]> ingredient_list = new ArrayList<Integer[]>();
		for (Object[] ingredient : ingredients) {
			int needed_qty = (Integer)ingredient[INGREDIENT_NEEDED];
			int current_qty = (Integer)ingredient[INGREDIENT_CURRENT];
			int restock_qty = needed_qty - current_qty;
			if (restock_qty <= 0)
				continue;
			ingredient[INGREDIENT_CURRENT] = ingredient[INGREDIENT_NEEDED];
			restock_list.add(new Integer[] {(Integer)ingredient[INGREDIENT_ID], restock_qty});
			ingredient_list.add(new Integer[] {(Integer)ingredient[INGREDIENT_ID], needed_qty});
		}
		insert_restocked_ingredients(restock_order_id, restock_list);
		update_ingredients_current_qty(ingredient_list);
		populate_ingredients();
	}

	/**
 	 * @param list an array containing the values of all ingredients whose values were edited
	 * @throws exception if database _ingredient cannot be accessed
 	 */
	private void update_ingredients_current_qty(List<Integer[]> list) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "UPDATE _ingredient SET current_qty = ? WHERE ingredient_id = ?;";
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);
			for (Integer[] values: list) { 
				preparedStatement.setInt(1, values[1]);
				preparedStatement.setInt(2, values[0]);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Update Ingredients.");
		}
	}

	/**
 	 * @param list an array containing the values of all ingredients whose values were edited
	 * @throws exception if database _ingredient cannot be accessed
 	 */
	  private void update_ingredients(List<Object[]> list) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "UPDATE _ingredient SET name = ?, current_qty = ?, cost = ? WHERE ingredient_id = ?;";
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);
			for (Object[] objects : list) { 
				preparedStatement.setString(1, (String)objects[INGREDIENT_NAME]);
				preparedStatement.setInt(2, (Integer)objects[INGREDIENT_CURRENT]);
				preparedStatement.setBigDecimal(3, (BigDecimal)objects[INGREDIENT_COST]);
				preparedStatement.setInt(4, (Integer)objects[INGREDIENT_ID]);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Update Ingredients.");
		}
	}

	/**
 	 * @param list an array containing the values of all menu items whose values were edited
	 * @throws exception if database for _menu_item cannot be accessed
 	 */	
	private void update_menu_items(List<Object[]> list) {
		PreparedStatement preparedStatement; 
		try {
			String sql = "UPDATE _menu_item SET name = ?, price = ? WHERE menu_item_id = ?;";
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);
			for (Object[] values: list) {
				int menu_item_id = (Integer)values[MENUITEM_ID];
				String name = (String)values[MENUITEM_NAME];
				BigDecimal price = (BigDecimal)values[MENUITEM_PRICE];
			
				preparedStatement.setString(1, name);
				preparedStatement.setBigDecimal(2, price);
				preparedStatement.setInt(3, menu_item_id);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			preparedStatement.close();
		} catch (Exception e){
			setStatus("Error accessing Database for Update Ingredients.");
		}
		
	}

	private void update_total() {;
		BigDecimal total = new BigDecimal(0.0);
		for (Object[] order_item : order_items) {
			total = total.add((BigDecimal)order_item[3]);
		}
		String text = total.toString();
		totalTextField.setText(text);
	}
	

	
	public static void main(String[] args) {
		// create GUI instance which is also a frame
		GUI gui = new GUI();
		gui.setTitle("Tiff's Treats");
		gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 
		gui.setSize(1000, 750);
		gui.setResizable(false);
	  
		gui.create_db_connection();
		gui.populate();
	  
		gui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame)e.getSource();
				String message = "Are you sure you want to exit the application?";
				String title = "Exit Application";
				int result = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
		
				if (result == JOptionPane.YES_OPTION) {
					gui.close_db_connection();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
		});
		gui.setVisible(true);
	}
}
