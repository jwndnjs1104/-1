
public class ContactsApp {

	public static void main(String[] args) {
		ContactsLogic logic = new ContactsLogic();
		while(true) {
			//1. 메인 메뉴 출력
			logic.printMenu();
			
			//2. 메인메뉴 번호 입력받기
			int mainMenu = logic.getMenuNumber();
			
			//3. 메인메뉴에 따른 분기
			logic.seperateMainMenu(mainMenu);
			
			
		}///while
	}////main

}/////class
