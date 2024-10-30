
public class Person {
	String name;
	private int age;
	private String address;
	private String tel;
	
	public Person(String name, int age, String address, String tel) {
		this.name=name;
		this.age=age;
		this.address=address;
		this.tel=tel;
	}
	
	@Override
	public String toString() {
		return String.format("이름: %s, 나이: %s, 주소: %s, 연락처: %s",name,age,address,tel);
	}

	
	
	public int getAge() {
		return age;
	}

	public String getAddress() {
		return address;
	}

	public String getTel() {
		return tel;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	
	
//배경음악,, 버튼,, 파일저장(ObjectIn/OutputStream),

}
