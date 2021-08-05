import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.sql.*;
import java.lang.*;
import java.lang.reflect.*;
public class DataManager
{
public Connection connection=null;
public static DataManager dataManager=null;
private DataManager()
{
}
public static DataManager getDataManager()
{
if(dataManager!=null) return dataManager;
dataManager=new DataManager();
return dataManager;
}

public void begin()
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
connection=DriverManager.getConnection(connectionURL,username,password);
connection.setAutoCommit(false);
}catch(Exception ioe)
{
System.out.println(ioe);
}
}

public int save(Object obj) throws DataException
{
int returnValue=0;
ResultSet rs;
Statement s;
try
{
Class c=obj.getClass();
Table table=(Table)c.getAnnotation(Table.class);
String tableName=table.name();
String query="insert into "+tableName+" (";
String values="";
int i=0;
Field fields[] = c.getFields();
Field f;
for (i=0;i<fields.length;i++) 
{
f=fields[i];
ForeignKey fk=f.getAnnotation(ForeignKey.class);
if(fk!=null)
{
String fkCheck="select * from "+fk.parent()+" where "+fk.column()+"="+f.get(obj)+";";
s=connection.createStatement();
rs=s.executeQuery(fkCheck);
if(!rs.next()) throw new DataException("Foreign key constraint violated");
}

AutoIncrement ai= f.getAnnotation(AutoIncrement.class);
if(ai==null) 
{

if(f.getType().equals(Integer.class))
{
//System.out.println(f.getName()+","+f.getType()+","+f.get(obj)); 
values=values+f.get(obj);
}
if(f.getType().equals(String.class))
{
values=values+"\""+f.get(obj)+"\"";
}

if(f.getType().equals(java.util.Date.class))
{
java.util.Date date=(java.util.Date)f.get(obj);
java.sql.Date sqlDate=new java.sql.Date(date.getTime());
values=values+"\""+sqlDate+"\"";
}
if(i<fields.length-1) values=values+",";
}
Column column = f.getAnnotation(Column.class);
if (column != null) 
{
ai= f.getAnnotation(AutoIncrement.class);
if(ai==null) 
{
query=query+column.name();
if(i<fields.length-1) query=query+",";
}
}
}
query=query+") values (";
values=values+");";
query=query+values;
//System.out.println("Query is : "+query);

s=connection.createStatement();
s.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
rs=s.getGeneratedKeys();
while(rs.next()) returnValue=rs.getInt(1);
rs.close();
s.close();
}catch(Exception exception)
{
throw new DataException(exception.getMessage());
}
return returnValue;
}
public void update(Object obj) throws DataException
{
Statement s;
ResultSet rs;
try
{
Class c=obj.getClass();
Table table=(Table)c.getAnnotation(Table.class);
String tableName=table.name();
String query="update "+tableName+" set ";
String where=" where ";
int prev=0;
int i=0;
Field fields[] = c.getFields();
Field f;
for (i=0;i<fields.length;i++) 
{
f=fields[i];
PrimaryKey pk;
Column column = f.getAnnotation(Column.class);
if (column != null) 
{
pk=f.getAnnotation(PrimaryKey.class);
if(pk!=null)
{
String pkCheck="select * from "+tableName+" where "+column.name()+"="+f.get(obj)+";";
s=connection.createStatement();
rs=s.executeQuery(pkCheck);
if(!rs.next()) throw new DataException(f.get(obj)+" in "+tableName+"does not exist");
where=where+column.name(); 
where=where+"=";
if(f.getType().equals(Integer.class))
{
where=where+f.get(obj);
}
if(f.getType().equals(String.class))
{
where=where+"\""+f.get(obj)+"\"";
}

if(f.getType().equals(java.util.Date.class))
{
java.util.Date date=(java.util.Date)f.get(obj);
java.sql.Date sqlDate=new java.sql.Date(date.getTime());
where=where+"\""+sqlDate+"\"";
}
}
else
{
if(f.get(obj)!=null)
if(prev==1) query=query+",";
prev=1;
query=query+column.name()+"=";
if(f.getType().equals(Integer.class))
{
query=query+f.get(obj);
}
if(f.getType().equals(String.class))
{
query=query+"\""+f.get(obj)+"\"";
}

if(f.getType().equals(java.util.Date.class))
{
java.util.Date date=(java.util.Date)f.get(obj);
java.sql.Date sqlDate=new java.sql.Date(date.getTime());
query=query+"\""+sqlDate+"\"";
}
}
}
}
query=query+where+";";
//System.out.println("Query is : "+query);
s=connection.createStatement();
s.executeUpdate(query);
s.close();

}catch(Exception exception)
{
throw new DataException(exception.getMessage());
}
}

public void delete(Class c,Object obj) throws DataException
{
Statement s;
ResultSet rs;
try
{
Table table=(Table)c.getAnnotation(Table.class);
String tableName=table.name();
String query="delete from "+tableName+" where ";
int i=0;
Field fields[] = c.getFields();
Field f;

for (i=0;i<fields.length;i++) 
{
f=fields[i];
PrimaryKey pk=f.getAnnotation(PrimaryKey.class);
if(pk!=null) 
{
String pkCheck="select * from "+tableName+" where "+f.getName()+"="+obj+";";
s=connection.createStatement();
rs=s.executeQuery(pkCheck);
if(!rs.next()) throw new DataException(obj+" in "+tableName+"does not exist");

query=query+f.getName()+"=";
if(obj.getClass().equals(Integer.class)) query=query+obj+";";
else query=query+"\""+obj+"\";";
}
}
s=connection.createStatement();
s.executeUpdate(query);
s.close();
}catch(Exception exception)
{
throw new DataException(exception.getMessage());
}
}



public List<Object> query(Class c) throws DataException
{
List<Object> list=new ArrayList<>();
try
{
Table table=(Table)c.getAnnotation(Table.class);
String tableName=table.name();
Field fields[];
int i=0;
String query="select * from "+tableName+";";
Statement s;
s=connection.createStatement();
ResultSet rs;
rs=s.executeQuery(query);
while(rs.next())
{
Object obj=c.newInstance();
fields= c.getFields();
for(i=0;i<fields.length;i++)
{
Field f=fields[i];
if(f.getType().equals(Integer.class))
f.set(obj,rs.getInt(f.getAnnotation(Column.class).name()));

if(f.getType().equals(String.class))
f.set(obj,rs.getString(f.getAnnotation(Column.class).name()));

if(f.getType().equals(java.util.Date.class))
f.set(obj,rs.getDate(f.getAnnotation(Column.class).name()));

}
list.add(obj);
}

s.close();
rs.close();
}catch(Exception exception)
{
throw new DataException(exception.getMessage());
}
return list;
}




public void end()
{
try
{
connection.commit();
}catch(SQLException sqlException)
{
System.out.println(sqlException);
}
}

}
