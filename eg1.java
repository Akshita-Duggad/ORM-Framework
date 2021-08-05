import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.sql.*;
class eg1psp
{
public static void main(String gg[])
{
try
{
JSONParser parser=new JSONParser();
Object obj=parser.parse(new FileReader("conf.json"));
JSONObject jsonObj=(JSONObject)obj;
String jdbcDriver=(String)jsonObj.get("jdbc-driver");
String connectionURL=(String)jsonObj.get("connection-url");
String username=(String)jsonObj.get("username");
String password=(String)jsonObj.get("password");
Class.forName(jdbcDriver);
Connection connection=DriverManager.getConnection(connectionURL,username,password);
DatabaseMetaData metaData = connection.getMetaData();
String[] types={"TABLE"};
ResultSet rs = metaData.getTables(null,null,"%",types);
String tableName;
while (rs.next()) 
{
tableName=rs.getString("TABLE_NAME");
System.out.println(tableName);
ResultSet primaryKey=metaData.getPrimaryKeys(null,null,tableName);
while(primaryKey.next())
{
System.out.println("Primary key is : "+primaryKey.getString(4));
}
ResultSet rsColumns=metaData.getColumns(null,null,tableName,null);
while (rsColumns.next()) 
{
System.out.println("Column Name is : "+rsColumns.getString("COLUMN_NAME"));
System.out.println("Column Type is : "+rsColumns.getString("TYPE_NAME"));
}
}
}catch(Exception ioe)
{
System.out.println("Exception");
}
}
}
