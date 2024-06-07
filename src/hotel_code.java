
import java.sql.*;
import java.util.Scanner;

public class hotel_code {
    public static final String hotelPassword = "shiv0921";
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
            while(true) {
                System.out.println();
                System.out.println("|--------------------------------------------|");
                System.out.println("|         Welcome to Shivtej Hotel           | ");
                System.out.println("|--------------------------------------------|");
                System.out.println();
                System.out.println("1.Book the room.\n2.return a room.\n3.show all rooms.\n4.allocate new rooms.\n5.exit.\nenter task choice : ");
                int ch = sc.nextInt();
                switch (ch) {
                    case 1:Booking(cn,sc);
                    break;
                    case 2:returnRoom(cn,sc);
                    break;
                    case 3:showACRooms(cn);showNonACRooms(cn);
                    break;
                    case 4:allocateNewRooms(cn,sc);
                    break;
                    case 5:return;
                }
            }

        }catch(SQLException e) {
            System.out.println("error at connection ........!");
        }
    }
    public static void allocateNewRooms(Connection cn,Scanner sc) {
        System.out.println();
        System.out.println("This section only for special staff....!");
        System.out.print("enter the access password of hotel : ");
        String accessPass = sc.next();
        if(accessPass.equals(hotelPassword)) {
            System.out.println();
            System.out.println("Welcome Sir.........");
            System.out.print("enter type of room : ");
            String typeroom = sc.next();
            System.out.print("enter hour rate for room : ");
            int hour_rate = sc.nextInt();
            String q = "insert into rooms(hour_rate,ACtype) values (?,?);";
            try {
                PreparedStatement ps = cn.prepareStatement(q);
                ps.setInt(1,hour_rate);
                ps.setString(2,typeroom);
                int afr = ps.executeUpdate();
                if(afr > 0) {
                    System.out.println("Rooms succesfully Allocated.....|");
                }else {
                    System.out.println("such Wrong......!");
                }
            }catch (SQLException e) {
                System.out.println("error at allocating new rooms............!");
            }
        }else {
            System.out.println("Wrong access pin..........!!");
        }
    }
    public static void Booking(Connection cn,Scanner sc) {
        System.out.println();
        System.out.println("*********** Welcome to Booking rooms Section ********");
        System.out.println();
        System.out.print("enter customer name : ");
        String name = sc.next();
        System.out.print("what type of room customer book (AC/NON) : ");
        String type = sc.next();
        System.out.print("|                Available Rooms                 |");
        if(type.equals("AC")) {
            showACRooms(cn);
        }else {
            showNonACRooms(cn);
        }
        System.out.print("enter room no for book a room : ");
        int room_id = sc.nextInt();
        int hourRate = getCharge(cn,room_id);
        System.out.print("how many hours customer book a room : ");
        int stayHours = sc.nextInt();
        System.out.println("total amount room rent = "+hourRate * stayHours);
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
        System.out.println("            Ac Types Rooms");
        String q1 = "select room_id,hour_rate from rooms where availability = true AND ACtype = 'AC'";
        try {
            PreparedStatement ps = cn.prepareStatement(q1);
            ResultSet rs = ps.executeQuery();
            int c =0;
            while(rs.next()) {
                    System.out.print("room NO = "+rs.getInt("room_id"));
                    System.out.println("\t| Hour Rate = "+rs.getInt("hour_rate"));
                    c++;
            }
            if(c == 0) {
                System.out.println("all ac rooms are booked!!!!!!!!!!!!!\nplease go to non ac section...!");
            }
        }catch (SQLException w) {
            System.out.println("error at show Rooms ........... !");
        }

    }
    public static void showNonACRooms(Connection cn) {
        System.out.println();
        System.out.println("            Non Ac Type Rooms");
        String q1 = "SELECT room_id,hour_rate FROM rooms WHERE availability = true AND ACtype = 'non AC';";
        try {
            PreparedStatement ps = cn.prepareStatement(q1);
            ResultSet rs = ps.executeQuery();
            int c = 0;
            while(rs.next()) {
                System.out.print("room NO = "+rs.getInt("room_id"));
                System.out.println("\t| hour rate = "+rs.getInt("hour_rate"));
                c++;
            }
            if(c == 0) {
                System.out.println("all non ac rooms are booked!!!!!!!!!!!!!\nplease go to  ac section...!");
            }
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
        System.out.println(" **** Welcome to return room section *****");
        System.out.println();
        System.out.print("enter room no of customer returnig room : ");
        int returnRoomNo = sc.nextInt();
       // System.out.println("just wait.......cheking customer pay all ammount of room rent..");
        String q = "delete from users where room_id = ?";
        try {
            PreparedStatement ps = cn.prepareStatement(q);
            ps.setInt(1,returnRoomNo);
            int afr = ps.executeUpdate();
            if(afr > 0) {
                System.out.println("succesfully room returned.\nThank you visit again.");
            }else {
                System.out.println("This room not allcate to anyone...you give information wrong....!");
            }
        }catch (SQLException e) {
            System.out.println("erorr at returning room..........1");
        }
    }


}
