import java.util.Date;
class eg6psp
{
public static void main(String gg[])
{
DataManager dm=DataManager.getDataManager();
dm.begin();
try
{
int code=9;
dm.delete(Course.class,code);
dm.end();
}catch(DataException dataException)
{
System.out.println(dataException.getMessage());
dm.end();
}
}
}