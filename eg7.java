import java.util.Date;
import java.util.*;
class eg7psp
{
public static void main(String gg[])
{
DataManager dm=DataManager.getDataManager();
dm.begin();
try
{
List<Object> students=dm.query(Student.class);
Student s;
for(Object obj: students)
{
s=(Student)obj;
System.out.println(s.rollNumber);
System.out.println(s.firstName);
System.out.println(s.lastName);
System.out.println(s.aadharCardNumber);
System.out.println(s.courseCode);
System.out.println(s.gender);
System.out.println(s.dateOfBirth);

}
}catch(DataException dataException)
{
System.out.println(dataException.getMessage());
}
}
}