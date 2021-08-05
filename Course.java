@Table(name="course")
class Course
{
@PrimaryKey()
@AutoIncrement()
@Column(name="code")
public Integer code;
@Column(name="title")
public String title;
}