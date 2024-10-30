import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ContactsLogic {
	private Map<Character, List<Person>> contacts;
	private List<Person> personList;
	private Scanner sc = new Scanner(System.in);
	
	public ContactsLogic() {
		contacts= new HashMap<>();
		load();
	}
	
	//메인메뉴 출력]
	public void printMenu() {
		System.out.println("======================주소록 메뉴=========================");
		System.out.println("1.입력   2.출력   3.수정   4.삭제   5.검색   6.저장   9.종료");
		System.out.println("=======================================================");
		System.out.println("메뉴번호를 입력하세요");
	}
	
	//메뉴 번호 입력받기]
	public int getMenuNumber() {
		while(true) {
			try {
				String menuStr = sc.nextLine().trim();
				return Integer.parseInt(menuStr);
			}
			catch (Exception e) {
				System.out.println("숫자만 입력해주세요");
			}
		}////while
	}//////getMenuNumber
	
	//메인메뉴 번호별 분기]
	public void seperateMainMenu(int mainMenu) {
		switch(mainMenu) {
			case 1://입력
				setPerson();
				break;
			case 2://출력
				printContacts();
				break;
			case 3://수정
				updatePerson();
				break;
			case 4://삭제 
				deletePerson();
				break;
			case 5://검색
				searchPerson();
				break;
			case 6://저장
				Save();
				break;
			case 9://종료
				System.out.println("프로그램을 종료합니다");
				System.exit(0);
			default:System.out.println("메뉴에 없는 번호입니다");
		}///////switch
	}////////////////////seperateMainMenu(int mainMenu)	

	//주어진 문자의 초성을 추출하는 메소드]
	public char getInitialConsonant(String value) {
		//if('가'>=value.toCharArray()[0] && value.toCharArray()[0] <= '낗') return 'ㄱ';
		if(!Pattern.matches("^[가-힣]{2,}$", value.trim())) return '0';
		char lastName = value.trim().charAt(0);
		//초성의 인덱스 얻기
		int index = (lastName-'가')/28/21;
		char[] initialConsonant = {'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'};
		return initialConsonant[index];
	}///////////
	
	//정보 입력 후 연락처에 추가]
	private void setPerson() {
		Person person;
		
		System.out.println("이름을 입력하세요");
		String name;
		while(true) {
			name = sc.nextLine().trim();
			if(Character.isDigit(getInitialConsonant(name))) {
				System.out.println("이름이 숫자형식입니다. 다시 입력해주세요");
				continue;
			}
			break;
		}
		System.out.println("나이를 입력하세요");
		int age = 0;
		while(true) {
			try {
				age = Integer.parseInt(sc.nextLine().trim());
				break;
			}
			catch (Exception e) {
				System.out.println("숫자만 입력해주세요");
			}
		}///////while
		System.out.println("사는곳을 입력하세요");
		String address = sc.nextLine().trim();
		System.out.println("연락처를 입력하세요");
		String tel = sc.nextLine().trim();
		if(contacts.containsKey(getInitialConsonant(name))) { //초성 이미 있으면
			personList = contacts.get(getInitialConsonant(name)); //이미 있는 리스트를 가져와서
			personList.add(new Person(name, age, address, tel)); //Person 추가
			contacts.replace(getInitialConsonant(name), personList); //다시 주소록에 넣기
		}
		else { //초성 없으면 새로 리스트 만들어서 map에 추가
			personList = new ArrayList<>();
			personList.add(new Person(name, age, address, tel));
			contacts.put(getInitialConsonant(name), personList);
		}
	}//////////serPerson
	
	//연락처 출력 메소드]
	private void printContacts() {
		if(!contacts.isEmpty()) {
			for(Character key:contacts.keySet()) {//keySet으로 키 set 불러와서 for문
				System.out.println(String.format("[%s 으로 시작하는 명단]", key));
				for(Person person:contacts.get(key)) System.out.println(person);
			}/////바깥 for문
		}
		else System.out.println("주소록이 비었습니다");
	}////////////////printContacts
	
	//이름으로 탐색, Person타입 list로 반환, 중복된 이름도 가능
	private List<Person> findPerson(String message) {
		personList =  new ArrayList<>();
		System.out.println(String.format("%s할 사람의 이름을 입력하세요", message));
		String name = sc.nextLine().trim();
		
		if(!contacts.containsKey(getInitialConsonant(name))) {
			System.out.println("입력한 이름과 일치한 결과가 없습니다");
			return personList;
		}
		//입력한 이름과 일치하는 사람이면 리스트에 담기
		for(Person person:contacts.get(getInitialConsonant(name)))
			if(person.name.equals(name)) personList.add(person); 
		
		if(personList.size()!=0) return personList;
		else {
			System.out.println("입력한 이름과 일치한 결과가 없습니다");
			return personList;
		}/////
	}///////////////findPerson
	
	//검색 결과 리스트 출력
	private void searchPerson() {
		personList = findPerson("검색");
		if(!personList.isEmpty()) {
			System.out.println("[검색 결과]");
			for(Person value:personList) System.out.println(value);
		}
	}/////////SearchPerson
	
	//연락처 삭제용 메소드]
	private void deletePerson() {
		personList = findPerson("삭제");
		
		if(personList.size()==1) {
			contacts.get(getInitialConsonant(personList.get(0).name)).remove(personList.get(0));
			System.out.println("삭제되었습니다");
		}
		else if(personList.size()>1) {
			System.out.println(String.format("[%s 과(와) 일치한 결과]", personList.get(0).name));
			for(int i=0;i<personList.size();i++)
				System.out.println(String.format("%s. %s",(i+1),personList.get(i)));
			
			System.out.println("\n삭제할 항목의 번호를 입력하세요");
			while(true) {
				int deleteNum;
				try {
					deleteNum=sc.nextInt();
				}
				catch (InputMismatchException e) {
					System.out.println("숫자만 입력하세요");
					continue;
				}
				
				if(deleteNum>personList.size() || deleteNum <= 0) {
					System.out.println("입력한 번호의 항목이 없습니다. 다시 입력하세요");
					continue;
				}
				
				contacts.get(getInitialConsonant(personList.get(deleteNum-1).name)).remove(personList.get(deleteNum-1));
				System.out.println("삭제되었습니다");
				break;
			}/////while
		}////else if
	}//////////////////deletePerson
	
	//수정 메소드]
	private void updatePerson() {
		personList = findPerson("수정");
		
		if(personList.size()==1) {
			System.out.println(String.format("[%s 과(와) 일치한 결과]", personList.get(0).name));
			System.out.println(personList.get(0));
			
			System.out.print(String.format("(수정 전 나이: %s)%n수정할 나이 입력: ", personList.get(0).getAge()));
			int age;
			while(true) {
				try {
					age = Integer.parseInt(sc.nextLine().trim());
					break;
				}
				catch (Exception e) {
					System.out.println("숫자만 입력해주세요");
				}
			}///////while
			System.out.print(String.format("(수정 전 주소: %s)%n수정할 주소 입력: ", personList.get(0).getAddress()));
			String address = sc.nextLine().trim();
			System.out.print(String.format("(수정 전 연락처: %s)%n수정할 연락처 입력: ", personList.get(0).getTel()));
			String tel = sc.nextLine().trim();
			
			int index = contacts.get(getInitialConsonant(personList.get(0).name)).indexOf(personList.get(0));
			contacts.get(getInitialConsonant(personList.get(0).name)).set(index,new Person(personList.get(0).name, age, address, tel));
			System.out.println("수정되었습니다");
		}
		else if(personList.size()>1) {
			System.out.println(String.format("[%s 과(와) 일치한 결과]", personList.get(0).name));
			for(int i=0;i<personList.size();i++)
				System.out.println(String.format("%s. %s",(i+1),personList.get(i)));
			
			System.out.println("\n수정을 원하는 항목의 번호를 입력하세요");
			int updateNum;
			while(true) {
				try {
					updateNum = sc.nextInt();
					if(updateNum>personList.size() || updateNum <= 0) {
						System.out.println("입력한 번호의 항목이 없습니다. 다시 입력하세요");
						continue;
					}
					break;
				}
				catch (InputMismatchException e) {
					System.out.println("숫자만 입력해주세요");
					continue;
				}
			}//////while
			
			System.out.print(String.format("(수정 전 나이: %s)%n수정할 나이 입력: ", personList.get(updateNum-1).getAge()));
			int age;
			while(true) {
				try {
					age = Integer.parseInt(sc.nextLine().trim());
					break;
				}
				catch (Exception e) {
					System.out.println("숫자만 입력해주세요");
				}
			}///////while
			System.out.print(String.format("(수정 전 주소: %s)%n수정할 주소 입력: ", personList.get(updateNum-1).getAddress()));
			String address = sc.nextLine().trim();
			System.out.print(String.format("(수정 전 연락처: %s)%n수정할 연락처 입력: ", personList.get(updateNum-1).getTel()));
			String tel = sc.nextLine().trim();
			
			int index = contacts.get(getInitialConsonant(personList.get(updateNum-1).name)).indexOf(personList.get(updateNum-1));
			contacts.get(getInitialConsonant(personList.get(updateNum-1).name)).set(index,new Person(personList.get(updateNum-1).name, age, address, tel));
			System.out.println("수정되었습니다");
		}/////
	}//////updatePerson
	
	//파일저장
	private void Save() {
		if(contacts.isEmpty()) {
			System.out.println("저장할 명단이 없습니다");
			return;
		}
		
		ObjectOutputStream oos=null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("src/contacts.txt"));
			oos.writeObject(contacts);
			System.out.println("주소록이 저장되었습니다");
		}
		catch (IOException e) {System.out.println("주소록 저장시 오류");}
		finally {
			try {
				if(oos!=null) oos.close();
			}
			catch (IOException e) {}
		}
	}//////save
	
	private void load() {
		ObjectInputStream ois=null;
		try {
			ois = new ObjectInputStream(new FileInputStream("src/contacts.txt"));
			Object obj = ois.readObject();	
			contacts = (Map<Character, List<Person>>)obj;
		}
		catch (Exception e) {}
		finally {
			try {
				if(ois!=null) ois.close();
			}
			catch (IOException e) {}
		}
	}/////load
	
}
