import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.sql.*;
class eg2psp
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


File f;
RandomAccessFile raf;
String fileName;
ResultSet rs = metaData.getTables(null,null,"%",types);
String tableName;
while (rs.next())
{
tableName=rs.getString("TABLE_NAME");
fileName=tableName.substring(0,1).toUpperCase()+tableName.substring(1);
f=new File(fileName+".java");
raf=new RandomAccessFile(f,"rw");
raf.writeBytes("@Table(name=\""+tableName+"\")");
raf.writeBytes("\n");
raf.writeBytes("class "+fileName);
raf.writeBytes("\n");
raf.writeBytes("{");
raf.writeBytes("\n");
String primaryKey="";
int index=0;
ResultSet primaryKeyrs=metaData.getPrimaryKeys(null,null,tableName);
while(primaryKeyrs.next())
{
primaryKey=primaryKeyrs.getString(4);
}
String foreignKey="";
String fkParentTable="";
String fkParentColumn="";
ResultSet fk = metaData.getImportedKeys(null, null, tableName);
while(fk.next())
{
fkParentTable=fk.getString("PKTABLE_NAME");
fkParentColumn=fk.getString("PKCOLUMN_NAME");
foreignKey=fk.getString("FKCOLUMN_NAME");
}
String columnName;
String column;
String columnType;
String autoIncrement="";
ResultSet rsColumns=metaData.getColumns(null,null,tableName,null);
while (rsColumns.next())
{
autoIncrement=rsColumns.getString("IS_AUTOINCREMENT");
columnName=rsColumns.getString("COLUMN_NAME");
column=columnName;
index=columnName.indexOf("_");
while(index!=-1)
{
columnName=columnName.substring(0,index)+columnName.substring(index+1,index+2).toUpperCase()+columnName.substring(index+2);
index=columnName.indexOf("_");
}

if(column.equals(primaryKey))
{
raf.writeBytes("@PrimaryKey()");
raf.writeBytes("\n");
}

if(column.equals(foreignKey))
{
raf.writeBytes("@ForeignKey(parent=\""+fkParentTable+"\",column=\""+fkParentColumn+"\")");
raf.writeBytes("\n");
}
if(autoIncrement.equals("YES"))
{
raf.writeBytes("@AutoIncrement()");
raf.writeBytes("\n");
}
raf.writeBytes("@Column(name=\""+column+"\")");
raf.writeBytes("\n");
columnType=rsColumns.getString("TYPE_NAME");
if(columnType.equals("INT"))
{
raf.writeBytes("public Integer "+columnName);
raf.writeBytes(";\n");
}
if(columnType.equals("CHAR"))
{
raf.writeBytes("public String "+columnName);
raf.writeBytes(";\n");
}
if(columnType.equals("DATE"))
{
raf.writeBytes("public java.util.Date "+columnName);
raf.writeBytes(";\n");
}
}
raf.writeBytes("}");
raf.close();

}

}catch(Exception ioe)
{
System.out.println("Exception");
}
}
}