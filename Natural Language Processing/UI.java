import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.TableColumn;
import tagger.*;
public class UI {
public static String actName;
public static JTextField username_field = new JTextField();

public UI() {
    JFrame main = new JFrame("ACTIVITY");
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    main.setResizable(false);
    main.setLayout(null);
    main.setPreferredSize(new Dimension(1000,800));
    main.setLocation(400, 200);

    // Heading: LOGIN
    JLabel heading = new JLabel("Knowlywood");

    heading.setFont(new Font("Courier New", Font.BOLD, 12));
    heading.setBounds(250, 60, 100, 60);
    main.add(heading);


    // Label Username
    JLabel username_label = new JLabel("Activity:");
    username_label.setFont(new Font("Courier New", Font.BOLD, 12));
    username_label.setBounds(250, 100, 75, 20);
    main.add(username_label);

    // Textfield Username

    username_field.setFont(new Font("Courier New", Font.PLAIN, 12));
    username_field.setBounds(325, 100, 200, 20);
    main.add(username_field);

   // this.name = username_field.getText();

    // Button Login
    JButton loginBtn = new JButton("SUBMIT");
    loginBtn.setFont(new Font("Courier New", Font.BOLD, 8));
    loginBtn.setBounds(300, 150, 80, 15);
    main.add(loginBtn);
    Similarity_2 sim=new Similarity_2();
    sim.createMap();
        actName=username_field.getText().toString();
        main.pack();
        main.setVisible(true);
        loginBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Button Clicked");
            actName = username_field.getText();
            if(actName!="")
            showTable(main,username_field.getText().toString());
            //System.out.println(name); //IT WORKS
        }
    });
}

public void showTable(JFrame main,String actName)
{

    String[] columns = new String[] {
            "Frame", "Value", "Value", "Value","Value","Value"
        };
        String[] actInfo=new String[] {
            "Similar Activity","Next Activity","Previous Activity","Participant","Source"
        };
        Object[][] data = new Object[6][6];
        //actual data for the table in a 2d array
       /* Object[][] data = new Object[][] {
            {1, "John", 40.0, false },
            {2, "Rambo", 70.0, false },
            {3, "Zorro", 60.0, true },
        };*/
        Similarity_2 sim=new Similarity_2();

        Activity act=sim.getActivity1(actName);
        if(act==null)
        act=sim.getActivity(actName);
        System.out.println(act.similarActivity);
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<6;j++)
            {
                if(j==0)
                data[i][j]=actInfo[i];
                else
                {
                        if(i==0)
                        {
                            if(j<=act.similarActivity.size())
                            data[i][j]=act.similarActivity.get(j-1);
                            else
                                data[i][j]="";
                        }
                        else if(i==1)
                        {
                            if(j<=act.next.size())
                            data[i][j]=act.next.get(j-1);
                            else
                            data[i][j]="";
                        }
                       else if(i==2)
                       {
                            if(j<=act.prev.size())
                            data[i][j]=act.prev.get(j-1);
                            else
                            data[i][j]="";
                       }
                       else if(i==3)
                       {
                           if(j<=act.participant.size())
                            data[i][j]=act.participant.get(j-1);
                            else
                                data[i][j]="";
                       }
                       else if(i==4)
                       {
                           if(j==1)
                            data[i][j]=act.source;
                           else
                            data[i][j]="";
                       }
                }
            }
        }
        //create table with data
        DefaultTableModel dtm = new DefaultTableModel(data, columns);
        JTable table = new JTable( dtm) {
            public TableCellRenderer getCellRenderer( int row, int column ) {
                return new PlusMinusCellRenderer();
            }
         };
         table.setRowHeight(30);
         table.getTableHeader().setFont( new Font( "Arial" , Font.BOLD, 10 ));
         table.getTableHeader().setPreferredSize(new Dimension(10,15));
         table.addMouseListener(new JTableButtonMouseListener(table));

          table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        for (int column = 0; column < table.getColumnCount(); column++)
        {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        int preferredWidth = tableColumn.getMinWidth();
        int maxWidth = tableColumn.getMaxWidth();

        for (int row = 0; row < table.getRowCount(); row++)
        {
            TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
            Component c = table.prepareRenderer(cellRenderer, row, column);
            int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
            preferredWidth = Math.max(preferredWidth, width);

            //  We've exceeded the maximum width, no need to check other rows

            if (preferredWidth >= maxWidth)
            {
                preferredWidth = maxWidth;
                break;
            }
        }

    tableColumn.setPreferredWidth( preferredWidth );
    }
        //add the table to the frame
        JScrollPane s = new JScrollPane(table);
        s.setBounds(50, 200,800, 200);
        s.setFont(new Font("Courier New", Font.PLAIN, 10));

        main.add(s);

}

class PlusMinusCellRenderer extends JPanel implements TableCellRenderer {
        public Component getTableCellRendererComponent(
                            final JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
                if(value!=null)
                {
                JLabel val = new JLabel(value.toString());
                val.setFont(new Font("Arial", Font.PLAIN, 9));
                if((row==1 && column!=0)|(row==2 && column!=0))
                val.setForeground(Color.BLUE);
                this.add(val);
                }
                return this;
        }
}
public class JTableButtonMouseListener extends MouseAdapter {
      private final JTable table;

      public JTableButtonMouseListener(JTable table) {
        this.table = table;
      }

      @Override public void mouseClicked(MouseEvent e) {
        int column = table.getColumnModel().getColumnIndexAtX(e.getX());
        int row    = e.getY()/table.getRowHeight();
        System.out.println("Clicked row: "+row + " Col :"+column );
        Object s =(Object)table.getModel().getValueAt(row, column);
        System.out.println(s.toString());
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int rows = model.getRowCount();
        if(s.toString()!="")
        {
        for(int i = rows - 1; i >=0; i--)
        {
            model.removeRow(i);
        }
        String[] columns = new String[] {
            "Frame", "Value", "Value", "Value","Value","Value"
        };
        String[] actInfo=new String[] {
            "Similar Activity","Next Activity","Previous Activity","Participant","Source"
        };

        Object[][] data1 = new Object[6][6];

        Similarity_2 sim=new Similarity_2();
        username_field.setText(s.toString());
        Activity act=sim.getActivity1(s.toString());
        if(act==null)
        act=sim.getActivity(s.toString());
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<6;j++)
            {
                if(j==0)
                data1[i][j]=actInfo[i];
                else
                {
                        if(i==0)
                        {
                            if(j<=act.similarActivity.size())
                            data1[i][j]=act.similarActivity.get(j-1);
                            else
                                data1[i][j]="";
                        }
                        else if(i==1)
                        {
                            if(j<=act.next.size())
                            data1[i][j]=act.next.get(j-1);
                            else
                            data1[i][j]="";
                        }
                       else if(i==2)
                       {
                            if(j<=act.prev.size())
                            data1[i][j]=act.prev.get(j-1);
                            else
                            data1[i][j]="";
                       }
                       else if(i==3)
                       {
                           if(j<=act.participant.size())
                            data1[i][j]=act.participant.get(j-1);
                            else
                                data1[i][j]="";
                       }
                       else if(i==4)
                       {
                           if(j==1)
                            data1[i][j]=act.source;
                           else
                            data1[i][j]="";
                       }
                }
            }
        }
        for(int i=0;i<data1.length;i++)
        {
            model.addRow(data1[i]);
        }
        }
      }
    }
public static void main(String[] args) {
    UI me = new UI();
    System.out.println("Why no work");
    //me.print();//I EXPECT IT WILL PRINT @name BUT NO, WHY?
}

public void print() {
 //   System.out.println(name);
}
}
