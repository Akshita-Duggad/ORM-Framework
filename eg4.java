import java.util.Date;
class eg4psp
{
public static void main(String gg[])
{
DataManager dm=new DataManager();
dm.begin();
try
{
/*Student s=new Student();
s.rollNumber=101;
s.firstName="Akshita";
s.lastName="Duggad";
s.aadharCardNumber="695523336";
s.courseCode=1;
s.gender="M";
Date d=new Date(2020,11,21);
s.dateOfBirth=d;
int ans=dm.save(s);
System.out.println("ANs for student is : "+ans);
*/
Course c=new Course();
c.title="Electrical";
int ans=dm.save(c);
System.out.println("ANs for course is : "+ans);
dm.end();
}catch(DataException dataException)
{
System.out.println(dataException.getMessage());
dm.end();
}
}
}