import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Float;
import java.lang.Integer;

import static java.lang.System.out;

public class management {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    // Inventory UI elements
    private JTextField txt_InventoryName;
    private JTextField txt_InventoryType;
    private JTextField float_InventoryPrice;
    private JTextField int_InventoryQuantity;
    private JTextArea txt_InventoryDescription;
    private JButton refreshInventoryButton;
    private JButton deleteInventoryButton;
    private JButton addInventoryButton;
    private JButton editInventoryButton;
    private JTable inventoryTable;

    // Membership UI elements
    private JTextField int_MemberID;
    private JTextField txt_MemberFirstName;
    private JTextField txt_MemberLastName;
    private JTextField txt_MemberEmail;
    private JTextField txt_MemberAddress;
    private JTextField txt_MemberStudent;
    private JButton deleteMemberButton;
    private JButton addMemberButton;
    private JButton refreshMemberButton;
    private JButton editMemberButton;
    private JTable membershipTable;
    private JTextField int_InventoryID;


    public static void main (String [] args) {
        JFrame frame = new JFrame ("Yummy Pizza Management System");    // Create new JFrame instance
        frame.setContentPane(new management().mainPanel);                    // Set content of frame to management class main panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                // When window is closed, exit JFrame.
        frame.setSize(1000,600);                                // Set size of frame
        frame.setLocationRelativeTo(null);                                   // Centre frame on screen
        frame.setVisible(true);                                              // Make frame visible.
    }

    public void membershipTable_load()
    {
        try {
            // Prepare SQL statement
            databaseConnect.pst = databaseConnect.connection.prepareStatement(databaseConnect.connection.nativeSQL
                    ("SELECT memberID, memberFirstName,memberLastName,memberEmail,memberAddress,memberStudent " +
                            "FROM membership " +
                            "ORDER BY memberID"));

            // Execute query which returns ResultSet object "rs".
            ResultSet rs = databaseConnect.pst.executeQuery();

            // Utilise RS2XML.JAR to set the ResultSet object to the table.
            membershipTable.setModel(DbUtils.resultSetToTableModel(rs));

            // Print message to confirm table has been loaded.
            out.println("Membership Table Loaded...");
        }
        // If try fails print error message to console.
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void inventoryTable_load()
    {
        try {
            // Prepare SQL statement
            databaseConnect.pst = databaseConnect.connection.prepareStatement(databaseConnect.connection.nativeSQL
                    ("SELECT itemID, itemName, itemType, itemPrice, itemQuantity, itemDescription FROM inventory"));

            // Execute query which returns ResultSet object "rs".
            ResultSet rs = databaseConnect.pst.executeQuery();

            // Utilise RS2XML.JAR to set the ResultSet object to the table.
            inventoryTable.setModel(DbUtils.resultSetToTableModel(rs));

            // Print message to confirm table has been loaded.
            out.println("Inventory Table Loaded...");
        }
        // If try fails print error message to console.
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public management() {
        // Connect to database
        databaseConnect.connect();

        // Load Membership and Inventory Tables.
        membershipTable_load();
        inventoryTable_load();

        refreshMemberButton.addActionListener(e -> {
            // Reload data from Membership Table.
            membershipTable_load();

            // Set all text fields to empty.
            int_MemberID.setText("");
            txt_MemberFirstName.setText("");
            txt_MemberLastName.setText("");
            txt_MemberEmail.setText("");
            txt_MemberAddress.setText("");
            txt_MemberStudent.setText("");
        });

        addMemberButton.addActionListener(e -> {

                // Initialise necessary variables for the SQL statement.
                String memberfirstname,memberlastname,memberemail,memberaddress;
                boolean memberstudent;

                // Associate variables with user input fields.
                memberfirstname = txt_MemberFirstName.getText();
                memberlastname = txt_MemberLastName.getText();
                memberemail = txt_MemberEmail.getText();
                memberaddress = txt_MemberAddress.getText();
                memberstudent = Boolean.parseBoolean(txt_MemberStudent.getName());

                try {
                    // SQL Query to add a new row of data.
                    // Question marks used as placeholders.
                    String addMemberQuery = "INSERT INTO public.membership" +
                            "(memberfirstname,memberlastname,memberemail,memberaddress,memberstudent) VALUES" +
                            "(?,?,?,?,?)";
                    databaseConnect.pst = databaseConnect.connection.prepareStatement(addMemberQuery);

                    // Insert data from input fields into query.
                    databaseConnect.pst.setString(1, memberfirstname);
                    databaseConnect.pst.setString(2, memberlastname);
                    databaseConnect.pst.setString(3, memberemail);
                    databaseConnect.pst.setString(4, memberaddress);
                    databaseConnect.pst.setBoolean(5, memberstudent);

                    // Execute SQL Query.
                    databaseConnect.pst.executeUpdate();

                    // Inform user the action has been performed.
                    JOptionPane.showMessageDialog(null, "Member added successfully.");

                    // Reset input fields to empty.
                    int_MemberID.setText("");
                    txt_MemberFirstName.setText("");
                    txt_MemberLastName.setText("");
                    txt_MemberEmail.setText("");
                    txt_MemberAddress.setText("");
                    txt_MemberStudent.setName("");
                }
                // If Try unable to execute, print error message in terminal.
                catch(SQLException e1)
                {
                    e1.printStackTrace();
                }
            });

        deleteMemberButton.addActionListener(e -> {
            // Declare variables to be used in delete member method.
            int memberid;

            // Associate variables with user input
            memberid = Integer.parseInt(int_MemberID.getText());

            try {
                // Store string called removeMemberQuery.
                // Use "?" for user input placeholder.
                String removeMemberQuery = "DELETE FROM membership WHERE memberid = ?";

                // Connect to database and prepare SQL statement
                databaseConnect.pst = databaseConnect.connection.prepareStatement(removeMemberQuery);

                // Associate user input with placeholder "?".
                databaseConnect.pst.setInt(1, memberid);

                // Execute SQL query.
                databaseConnect.pst.executeUpdate();

                // User message to confirm action has been completed.
                JOptionPane.showMessageDialog(null, "Member deleted successfully.");

                // Reset user input fields to empty.
                int_MemberID.setText("");
                txt_MemberFirstName.setText("");
                txt_MemberLastName.setText("");
                txt_MemberEmail.setText("");
                txt_MemberAddress.setText("");
                txt_MemberStudent.setText("");

            // Catch statement if Try fails.
            } catch (SQLException e2)
            {
                e2.printStackTrace();
            }
        });

        editMemberButton.addActionListener(e -> {

            // If no member is selected from the table, show error message.
            if (membershipTable.getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "No member is selected.");
            } else {

                // Initialise necessary variables for the SQL statement.
                String memberfirstname,memberlastname,memberemail,memberaddress;
                boolean memberstudent;
                int memberid;

                // Associate variables with user input fields.
                memberid = Integer.parseInt(int_MemberID.getText());
                memberfirstname = txt_MemberFirstName.getText();
                memberlastname = txt_MemberLastName.getText();
                memberemail = txt_MemberEmail.getText();
                memberaddress = txt_MemberAddress.getText();
                memberstudent = Boolean.parseBoolean(txt_MemberStudent.getText());

                try {
                    // SQL Query to edit row of data.
                    // Question marks used as placeholders for updated values.
                    String editMemberQuery = "UPDATE public.membership " +
                            "SET memberfirstname = ?,memberlastname = ?,memberemail = ?,memberaddress = ?,memberstudent = ?" +
                            "WHERE memberID = ?";
                    databaseConnect.pst = databaseConnect.connection.prepareStatement(editMemberQuery);

                    // Insert data from input fields into query.
                    databaseConnect.pst.setString(1, memberfirstname);
                    databaseConnect.pst.setString(2, memberlastname);
                    databaseConnect.pst.setString(3, memberemail);
                    databaseConnect.pst.setString(4, memberaddress);
                    databaseConnect.pst.setBoolean(5, memberstudent);
                    databaseConnect.pst.setInt(6, memberid);

                    // Execute SQL Query.
                    databaseConnect.pst.executeUpdate();

                    // Inform user the action has been performed.
                    JOptionPane.showMessageDialog(null, "Member has been edited.");

                    // Reset input fields to empty.
                    txt_MemberFirstName.setText("");
                    txt_MemberLastName.setText("");
                    txt_MemberEmail.setText("");
                    txt_MemberAddress.setText("");
                    txt_MemberStudent.setText("");
                    int_MemberID.setText("");
                }
                // If Try is unable to execute, print error message in terminal.
                catch(SQLException e3)
                {
                    e3.printStackTrace();
                }
            }
        });

        membershipTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // listen for event mouse click
                super.mouseClicked(e);

                // Set new int i. Associate i with membership table selected row.
                // We can use this to iterate and get data for each row.
                int i = membershipTable.getSelectedRow();

                // Create new instance of TableModel called model
                // Associate model with membership table
                TableModel model = membershipTable.getModel();

                // Take data from table and set input values to data in table.
                int_MemberID.setText(model.getValueAt(i,0).toString());
                txt_MemberFirstName.setText(model.getValueAt(i,1).toString());
                txt_MemberLastName.setText(model.getValueAt(i,2).toString());
                txt_MemberEmail.setText(model.getValueAt(i,3).toString());
                txt_MemberAddress.setText(model.getValueAt(i,4).toString());
                txt_MemberStudent.setText(model.getValueAt(i,5).toString());
            }
        });

        addInventoryButton.addActionListener(e -> {

            // Declare variables to use in the add inventory method.
            String itemname,itemtype,itemdescription;
            float itemprice;
            int itemquantity;

            // Associate variables with text from input fields
            itemname = txt_InventoryName.getText();
            itemtype = txt_InventoryType.getText();
            itemprice = Float.parseFloat(float_InventoryPrice.getText());
            itemquantity = Integer.parseInt(int_InventoryQuantity.getText());
            itemdescription = txt_InventoryDescription.getText();

            // Try statement to execute query.
            try {

                // Store a string called SQLquery.
                // "?" used as placeholder for user input.
                String SQLquery = "INSERT INTO public.inventory" +
                        "(itemname,itemtype,itemprice,itemquantity,itemdescription) VALUES" +
                        "(?,?,?,?,?)";

                // Connect to database.
                databaseConnect.pst = databaseConnect.connection.prepareStatement(SQLquery);

                // Associate input from text fields to placeholder "?"
                databaseConnect.pst.setString(1,itemname);
                databaseConnect.pst.setString(2,itemtype);
                databaseConnect.pst.setFloat(3,itemprice);
                databaseConnect.pst.setInt(4,itemquantity);
                databaseConnect.pst.setString(5,itemdescription);

                // Execute SQL query.
                databaseConnect.pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record added successfully.");

                // Reset input fields to empty.
                txt_InventoryName.setText("");
                txt_InventoryType.setText("");
                float_InventoryPrice.setText("");
                int_InventoryQuantity.setText("");
                txt_InventoryDescription.setText("");
                txt_InventoryName.requestFocus();

                // Reload table
                inventoryTable_load();
            }

            // Catch if try not successful and print error in terminal.
            catch(SQLException e1)
            {
                e1.printStackTrace();
            }
        });

        refreshInventoryButton.addActionListener(e -> {
            // Reload Inventory Table.
            inventoryTable_load();

            // Reset input fields to empty.
            int_InventoryID.setText("");
            txt_InventoryName.setText("");
            txt_InventoryType.setText("");
            float_InventoryPrice.setText("");
            int_InventoryQuantity.setText("");
            txt_InventoryDescription.setText("");
        });

        deleteInventoryButton.addActionListener(e -> {
            // Declare variables to use in the delete inventory method.
            int itemID;
//            String itemName;

            // Associate variables with text from input fields
            itemID = Integer.parseInt(int_InventoryID.getText());
//            itemName = txt_InventoryName.getText();

            try {
                // SQL query to delete item from inventory.
                // "?" used as placeholder for user input.
                String removeItemQuery = "DELETE FROM inventory WHERE itemID = ?";

                // Connect to database and prepare SQL query.
                databaseConnect.pst = databaseConnect.connection.prepareStatement(removeItemQuery);

                // Associate user input with placeholder "?".
                databaseConnect.pst.setInt(1, itemID);
//                databaseConnect.pst.setString(1, itemName);

                // Execute SQL query.
                databaseConnect.pst.executeUpdate();

                // Message to tell user the item has been deleted.
                JOptionPane.showMessageDialog(null, "Item deleted successfully.");

                // Reset input fields to empty.
                int_InventoryID.setText("");
                txt_InventoryName.setText("");
                txt_InventoryType.setText("");
                float_InventoryPrice.setText("");
                int_InventoryQuantity.setText("");
                txt_InventoryDescription.setText("");

            }

            // If Try unable to execute, print error message in terminal.
            catch (SQLException e2)
            {
                e2.printStackTrace();
            }
        });

        editInventoryButton.addActionListener(e -> {
            // If no item is selected from the table, show error message.
            if (inventoryTable.getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "No item is selected.");
            } else {
                // Initialise necessary variables for the SQL statement.
                String itemname, itemtype, itemdescription;
                float itemprice;
                int itemquantity, itemid;

                // Associate variables with user input fields.
                itemid = Integer.parseInt(int_InventoryID.getText());
                itemname = txt_InventoryName.getText();
                itemtype = txt_InventoryType.getText();
                itemprice = Float.parseFloat(float_InventoryPrice.getText());
                itemquantity = Integer.parseInt(int_InventoryQuantity.getText());
                itemdescription = txt_InventoryDescription.getText();


                try {
                    // SQL Query to edit row of data.
                    // Question marks used as placeholders for updated values.
                    String addMemberQuery = "UPDATE public.inventory " +
                            "SET itemname = ?,itemtype = ?,itemprice = ?,itemquantity = ?,itemdescription = ?" +
                            "WHERE itemid = ?";
                    databaseConnect.pst = databaseConnect.connection.prepareStatement(addMemberQuery);

                    // Insert data from input fields into query.
                    databaseConnect.pst.setString(1, itemname);
                    databaseConnect.pst.setString(2, itemtype);
                    databaseConnect.pst.setFloat(3, itemprice);
                    databaseConnect.pst.setInt(4, itemquantity);
                    databaseConnect.pst.setString(5, itemdescription);
                    databaseConnect.pst.setInt(6, itemid);

                    // Execute SQL Query.
                    databaseConnect.pst.executeUpdate();

                    // Inform user the action has been performed.
                    JOptionPane.showMessageDialog(null, "Item has been edited.");

                    // Reset input fields to empty.
                    txt_InventoryName.setText("");
                    txt_InventoryType.setText("");
                    txt_InventoryDescription.setText("");
                    float_InventoryPrice.setText("");
                    int_InventoryQuantity.setText("");
                    int_InventoryID.setText("");
                }
                // If Try is unable to execute, print error message in terminal.
                catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }
        });

        inventoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // listen for event mouse click
                super.mouseClicked(e);

                // Set new int i. Associate i with membership table selected row.
                // We can use this to iterate and get data for each row.
                int i = inventoryTable.getSelectedRow();

                // Create new instance of TableModel called model
                // Associate model with membership table
                TableModel model = inventoryTable.getModel();

                // Take data from table and set input values to data in table.
                int_InventoryID.setText(model.getValueAt(i,0).toString());
                txt_InventoryName.setText(model.getValueAt(i,1).toString());
                txt_InventoryType.setText(model.getValueAt(i,2).toString());
                float_InventoryPrice.setText(model.getValueAt(i,3).toString());
                int_InventoryQuantity.setText(model.getValueAt(i,4).toString());
                txt_InventoryDescription.setText(model.getValueAt(i,5).toString());
            }
        });
    }
}
