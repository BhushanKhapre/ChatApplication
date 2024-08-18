package ChatApplication;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Client implements ActionListener {
    //global declrations
    JTextField text;
    static JPanel p2;
    static Box vertical = Box.createVerticalBox();//message will be shown one after another in the vertical fashion
    static DataOutputStream dout;
    static JFrame f = new JFrame();


    //constructor - calls automatically when object is created
    Client(){
        f.setLayout(null);
        //green color panal at top
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(0x5990D2));
        p1.setBounds(0, 0, 400, 70);
        f.add(p1);

        //back image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        p1.setLayout(null);
        back.setBounds(5,20,25,25);//setBound works only if setLayout is null
        p1.add(back);

        //on click on back button the program will end
        back.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });

        //DP image
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image i5 = i4.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel DP = new JLabel(i6);
        p1.setLayout(null);
        DP.setBounds(40,10,50,50);//setBound works only if setLayout is null
        p1.add(DP);

        //video call image
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        p1.setLayout(null);
        video.setBounds(270, 20,30,30);
        p1.add(video);

        //phone image
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        p1.setLayout(null);
        phone.setBounds(320,20,30,30);
        p1.add(phone);

        //menu - three vertical dots
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel menu = new JLabel(i15);
        p1.setLayout(null);
        menu.setBounds(370, 20, 10, 25);
        p1.add(menu);

        //profile name
        JLabel name = new JLabel("Bunty");
        name.setBounds(100, 15,100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        //Active(online) or not
        JLabel online = new JLabel("Active now");
        online.setBounds(100, 35, 100, 18);
        online.setForeground(Color.WHITE);
        online.setFont(new Font("SAN_SERIF", Font.BOLD, 13));
        p1.add(online);

        //2nd JPanel just below the green header
        p2 = new JPanel();
        p2.setBounds(5, 75, 390, 570);
        p2.setBackground(new Color(242, 239, 239));
        f.add(p2);

        //text feild at bottom
        text = new JTextField();
        text.setBounds(5,650,310,40);
        text.setFont(new Font("SEN_SERIF",Font.PLAIN,16));
        f.add(text);

        //send message button
        JButton button = new JButton("Send");
        button.setBounds(325,650,70,40);
        button.setBackground(new Color(0x5990D2));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SEN_SERIF",Font.PLAIN,16));
        f.add(button);
        button.addActionListener(this);


        //main frame size
        f.setSize(400, 700);
        f.setLocation(800, 50);
        f.setUndecorated(true);//to remove the title bar
        f.getContentPane().setBackground(new Color(255,255,255));
        f.setVisible(true);

    }

    //usage - on send button
    public void actionPerformed(ActionEvent ae){
        try {
            String out = text.getText();
            if (!out.isBlank()) {
                JPanel p3 = formatLabel(out);

                p2.setLayout(new BorderLayout());
                JPanel right = new JPanel(new BorderLayout());
                right.add(p3, BorderLayout.LINE_END);//typed message will be shown at the right end (due to - BorderLayout.LINE_END)

                vertical.add(right);
                vertical.add(Box.createVerticalStrut(15));//spaces between two messages

                p2.add(vertical, BorderLayout.PAGE_START);

                dout.writeUTF(out);

                text.setText("");
                f.repaint();
                f.invalidate();
                f.validate(); //refresh the panel
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public static JPanel formatLabel(String out){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + " </p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(Color.GREEN);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(5,5,5,50));
        p.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        p.add(time);

        return p;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Please provide the ip Address : ");
        String ipAddress = sc.next();
        System.out.print("Please  provide port number : ");
        int portNumber = sc.nextInt();
        //annanomus object
        new Client();

        //socket programming
        try{
            Socket s = new Socket(ipAddress, portNumber);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true){
                p2.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);
                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                p2.add(vertical, BorderLayout.PAGE_START);
                f.validate();//to reload the frame
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
