import java.util.Date;
class eg5psp
{
public static void main(String gg[])
{
DataManager dm=new DataManager();
dm.begin();
try
{
Course c=new Course();
c.code=1;
c.title="Computer Science";
dm.update(c);
dm.end();
}catch(DataException dataException)
{
System.out.println(dataException.getMessage());
dm.end();
}
}
}