package brm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class BookFrame {
    Connection con;
    PreparedStatement ps;
    JFrame frame=new JFrame("BRM Project");
    JTabbedPane tabbedPane=new JTabbedPane();
    JPanel insertPanel, viewpanel;
    JLabel l1,l2,l3,l4,l5;
    JTextField t1,t2,t3,t4,t5;
    JButton saveButton,updateButton,deleteButton;
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tm;
    String []colName={"Book ID","Title","Price","Author","Publisher"};

    public BookFrame(){
        getConnectionFromMySQL();
        initComponents();
    }
    void getConnectionFromMySQL(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shivanidb", "root", "Shivijais1201@");
            System.out.println("connection Established");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    void initComponents(){
        //components for insert form
        l1=new JLabel();
        l1.setText("Book Id:");
        l2=new JLabel();
        l2.setText("Title:");
        l3=new JLabel();
        l3.setText("Price:");
        l4=new JLabel();
        l4.setText("Author:");
        l5=new JLabel();
        l5.setText("Publisher:");
        t1=new JTextField();
        t2=new JTextField();
        t3=new JTextField();
        t4=new JTextField();
        t5=new JTextField();
        saveButton=new JButton("Save");
        l1.setBounds(100,100,100,20);
        l2.setBounds(100,150,100,20);
        l3.setBounds(100,200,100,20);
        l4.setBounds(100,250,100,20);
        l5.setBounds(100,300,100,20);
        t1.setBounds(250,100,100,20);
        t2.setBounds(250,150,100,20);
        t3.setBounds(250,200,100,20);
        t4.setBounds(250,250,100,20);
        t5.setBounds(250,300,100,20);
        saveButton.setBounds(100,350,100,30);
        saveButton.addActionListener(new InsertBookRecord());

        insertPanel=new JPanel();
        insertPanel.setLayout(null);
        insertPanel.add(l1);
        insertPanel.add(l2);
        insertPanel.add(l3);
        insertPanel.add(l4);
        insertPanel.add(l5);
        insertPanel.add(t1);
        insertPanel.add(t2);
        insertPanel.add(t3);
        insertPanel.add(t4);
        insertPanel.add(t5);
        insertPanel.add(saveButton);
        ArrayList<Book> bookList =fetchBookRecords();
        setDataOnTable(bookList);
        updateButton=new JButton("update book");
        updateButton=new JButton("update Book");
        deleteButton=new JButton("delete book");
        deleteButton.addActionListener(new DeleteBookRecord());
        viewpanel=new JPanel();
        viewpanel.add(updateButton);
        viewpanel.add(deleteButton);
        scrollPane=new JScrollPane(table);
        viewpanel.add(scrollPane);

        tabbedPane.add(insertPanel);
        tabbedPane.add(viewpanel);
        tabbedPane.addChangeListener(new TabChangeHandler());

        frame.add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);

    }
    void setDataOnTable(ArrayList<Book> bookList){
        Object [][]obj=new Object[bookList.size()][5];
        for(int i=0;i<bookList.size();i++){
            obj[i][0]=bookList.get(i).getBookId();
            obj[i][1]=bookList.get(i).getTitle();
            obj[i][2]=bookList.get(i).getPrice();
            obj[i][3]=bookList.get(i).getAuthor();
            obj[i][4]=bookList.get(i).getPublisher();
        }
        table=new JTable();
        tm=new DefaultTableModel();
        tm.setColumnCount(5);
        tm.setRowCount(bookList.size());
        tm.setColumnIdentifiers(colName);
        for(int i=0;i<bookList.size();i++){
            tm.setValueAt(obj[i][0],i,0);
            tm.setValueAt(obj[i][1],i,1);
            tm.setValueAt(obj[i][2],i,2);
            tm.setValueAt(obj[i][3],i,3);
            tm.setValueAt(obj[i][4],i,4);
        }
        table.setModel(tm);
    }
    void updateTable(ArrayList<Book> bookList){
        Object [][]obj=new Object[bookList.size()][5];
        for(int i=0;i<bookList.size();i++){
            obj[i][0]=bookList.get(i).getBookId();
            obj[i][1]=bookList.get(i).getTitle();
            obj[i][2]=bookList.get(i).getPrice();
            obj[i][3]=bookList.get(i).getAuthor();
            obj[i][4]=bookList.get(i).getPublisher();
        }
        tm.setRowCount(bookList.size());
        for(int i=0;i<bookList.size();i++){
            tm.setValueAt(obj[i][0],i,0);
            tm.setValueAt(obj[i][1],i,1);
            tm.setValueAt(obj[i][2],i,2);
            tm.setValueAt(obj[i][3],i,3);
            tm.setValueAt(obj[i][4],i,4);
        }
        table.setModel(tm);
    }
    ArrayList<Book> fetchBookRecords(){
        ArrayList <Book>bookList=new ArrayList<Book>();
        String q="Select * from book";
        try{
            ps=con.prepareStatement(q);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Book b=new Book();
                b.setBookId(rs.getInt(1));
                b.setTitle(rs.getString(2));
                b.setPrice(rs.getDouble(3));
                b.setAuthor(rs.getString(4));
                b.setPublisher(rs.getString(5));
                bookList.add(b);
            }
        }
        catch (SQLException ex){
            System.out.println("exception: "+ex.getMessage());
        }
        finally {
            return bookList;
        }
    }
    class InsertBookRecord implements ActionListener{


        public void actionPerformed(ActionEvent e){
          Book b1=readFromData();
          String q="insert into Book (bookid,title,price,author,publisher) value(?,?,?,?,?)";
          try{
              ps=con.prepareStatement(q);
              ps.setInt(1,b1.getBookId());
              ps.setString(2,b1.getTitle());
              ps.setDouble(3,b1.getPrice());
              ps.setString(4,b1.getAuthor());
              ps.setString(5,b1.getPublisher());
              ps.execute();
              t1.setText("");
              t2.setText("");
              t3.setText("");
              t4.setText("");
              t5.setText("");
          }
          catch (SQLException ex){
              System.out.println("exception: "+ex.getMessage());
          }
        }
        Book readFromData(){
            Book b1=new Book();
            b1.setBookId(Integer.parseInt(t1.getText()));
            b1.setTitle(t2.getText());
            b1.setPrice(Double.parseDouble(t3.getText()));
            b1.setAuthor(t4.getText());
            b1.setPublisher(t5.getText());
            return b1;
        }
    }
    class TabChangeHandler implements ChangeListener{

       public void stateChanged(ChangeEvent e){
           int index=tabbedPane.getSelectedIndex();
           if (index == 0) {
               System.out.println("insert");
           }
           if(index==1){
               ArrayList<Book>bookList=fetchBookRecords();
              updateTable(bookList);
           }
       }
    }
    class UpdateBookRecord implements ActionListener{
        public void actionPerformed(ActionEvent e){
            ArrayList<Book>updatedBookList=readTableData();
            String q="update Book set title=?,author=? where bookid=?";
            try{
                ps=con.prepareStatement(q);
                for(int i=0;i<updatedBookList.size();i++){
                    ps.setString(1,updatedBookList.get(i).getTitle());
                    ps.setDouble(2,updatedBookList.get(i).getPrice());
                    ps.setString(3,updatedBookList.get(i).getAuthor());
                    ps.setString(4,updatedBookList.get(i).getPublisher());
                    ps.setInt(5,updatedBookList.get(i).getBookId());
                    ps.executeUpdate();
                }
            }
            catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
        ArrayList<Book>readTableData(){
            ArrayList<Book>updatedBookList=new ArrayList<Book>();
            for(int i=0;i<table.getRowCount();i++){
                Book b=new Book();
                b.setBookId(Integer.parseInt(table.getValueAt(i,0).toString()));
                b.setTitle(table.getValueAt(i,1).toString());
                b.setPrice(Double.parseDouble(table.getValueAt(i,2).toString()));
                b.setAuthor(table.getValueAt(i,3).toString());
                b.setPublisher(table.getValueAt(i,4).toString());
                updatedBookList.add(b);
            }
            return updatedBookList;
        }
    }
    class DeleteBookRecord implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int rowNo=table.getSelectedRow();
            if(rowNo!=-1){
                int id=(int)table.getValueAt(rowNo,0);
                String q="delete from book where bookid=?";
                try{
                    ps=con.prepareStatement(q);
                    ps.setInt(1,id);
                    ps.execute();
                }
                catch(SQLException ex){
                    System.out.println(ex.getMessage());
                }
                finally {
                    ArrayList<Book> bookList=fetchBookRecords();
                    updateTable(bookList);
                }
            }
        }
    }
}

