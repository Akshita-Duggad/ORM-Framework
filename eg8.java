import java.text.*;
class eg8psp
{
public static void main(String gg[])
{
java.util.Date date=new java.util.Date(2000,11,21);
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
System.out.println(sdf.format(date));  
java.sql.Date sqlDate=new java.sql.Date(date.getYear(),date.getMonth(),date.getDate());
System.out.println(sqlDate);
}
}
