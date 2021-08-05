@Table(name="student")
class Student
{
@PrimaryKey()
@Column(name="roll_number")
public Integer rollNumber;
@Column(name="first_name")
public String firstName;
@Column(name="last_name")
public String lastName;
@Column(name="aadhar_card_number")
public String aadharCardNumber;
@ForeignKey(parent="course",column="code")
@Column(name="course_code")
public Integer courseCode;
@Column(name="gender")
public String gender;
@Column(name="date_of_birth")
public java.util.Date dateOfBirth;
}