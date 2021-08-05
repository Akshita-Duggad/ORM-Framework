import java.util.Date;
class eg9psp
{
public static void main(String gg[])
{
DataManager dm=DataManager.getDataManager();
dm.begin();
try
{
Student s=new Student();
s.rollNumber=102;
s.firstName="Akshita";
s.lastName="Duggad";
s.aadharCardNumber="695523336";
s.courseCode=5;
s.gender="M";
Date d=new Date(2020,11,21);
s.dateOfBirth=d;
int ans=dm.save(s);
System.out.println("ANs for student is : "+ans);
dm.end();
}catch(DataException dataException)
{
System.out.println(dataException.getMessage());
dm.end();
}
}
}