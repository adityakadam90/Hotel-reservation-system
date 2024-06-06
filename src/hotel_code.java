
import java.sql.*;
import java.util.Scanner;

public class hotel_code {
    public static void main(String args[]) {
        String url = "jdbc:mysql://localhost:3306/hotel_system";
        String password = "@#aditya2006";
        String username = "root";
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            System.out.println("error drivers loading.................!");
        }
        try {
            Connection cn = DriverManager.getConnection(url,username,password);
            System.out.println();
            System.out.println("|--------------------------------------------|");
            System.out.println("|         Welcome to Shivtej Hotel           | ");
            System.out.println("|--------------------------------------------|");
            while(true) {
                System.out.println();
                System.out.println("1.Book the room.\n2.return a room.\n3.exit.\nenter your choice : ");
                int ch = sc.nextInt();
                switch (ch) {
                    case 1:Booking(cn,sc);
                    break;
                    case 2:returnRoom(cn,sc);
                    break;
                    case 3:return;
                }
            }

        }catch(SQLException e) {
            System.out.println("error at connection ........!");
        }
    }
    public static void Booking(Connection cn,Scanner sc) {
        System.out.println();
        System.out.println("*********** Welcome to Booking rooms Section ********");
        System.out.println();
        System.out.println("enter your name : ");
        String name = sc.next();
        System.out.println("what type of room you book (AC/NON) : ");
        String type = sc.next();
        if(type == "AC") {
            showACRooms(cn);
        }else {
            showNonACRooms(cn);
        }
        System.out.println("enter room no for book a room : ");
        int room_id = sc.nextInt();
        int hourRate = getCharge(cn,room_id);
        System.out.println("how many hours you book a room : ");
        int stayHours = sc.nextInt();
        System.out.println("your total amount is = "+hourRate * stayHours);
        System.out.println("payment Done............\nThank You !");
        String q3 = "insert into users(room_id,cust_name) values(?,?)";
        try {
            //System.out.println("going to insert users");
            PreparedStatement ps = cn.prepareStatement(q3);
            ps.setInt(1,room_id);
            ps.setString(2,name);
            int afr = ps.executeUpdate();
            //System.out.println("going to update rooms section .....");
            if(afr > 0) {
                String q4 = "update rooms set availability = false where room_id = ?";
                PreparedStatement ps2 = cn.prepareStatement(q4);
                ps2.setInt(1,room_id);
                int afr2 = ps2.executeUpdate();
                if(afr2 > 0) {
                    System.out.println("rooms booked succesfully....");
                    System.out.println("take a keys of room "+room_id+" from count 3...");
                }else {
                    System.out.println("error at update!!!!!!!!");
                }

            }else {
                System.out.println("soory...just wait some problem occurs....");
            }
        }catch (SQLException e) {
            System.out.println("error at booking....!");
        }

    }
    public static void showACRooms(Connection cn) {
        System.out.println();
        System.out.println();
        String q1 = "select room_id from rooms where availability = true ";
        try {
            PreparedStatement ps = cn.prepareStatement(q1);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                    System.out.println("room NO = "+rs.getInt("room_id"));

            }
            if(!rs.next()) {
                System.out.println("all ac rooms are booked!!!!!!!!!!!!!\nplease go to non ac section...!");
            }
        }catch (SQLException w) {
            System.out.println("error at show Rooms ........... !");
        }

    }
    public static void showNonACRooms(Connection cn) {
        System.out.println();
        System.out.println();
        String q1 = "SELECT room_id FROM rooms WHERE availability = true AND ACtype = 'non AC' ";
        try {
            PreparedStatement ps = cn.prepareStatement(q1);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                System.out.println("room NO = "+rs.getInt("room_id"));

            }
//            if(!rs.next()) {
//                System.out.println("all ac rooms are booked!!!!!!!!!!!!!\nplease go to non ac section...!");
//            }
        }catch (SQLException w) {
            System.out.println("error at show Rooms ........... !");
        }

    }
    public static int getCharge(Connection cn,int room_id) {
        String q2 = "select hour_rate from rooms where room_id = ?;";
        try {
            PreparedStatement ps = cn.prepareStatement(q2);
            ps.setInt(1,room_id);
           ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    return rs.getInt("hour_rate");
                }
        }catch (SQLException w){
            System.out.println("eror at charge.......!");
        }
        return -1;
    }
    public static void returnRoom(Connection cn,Scanner sc){

    }

}
