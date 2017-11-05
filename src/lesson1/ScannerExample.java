package lesson1;

import java.util.Scanner;

public class ScannerExample {

    public static void main(String[] args) {

        System.out.print("Введите ваше имя " );
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("Привет, " + name);

        while (scanner.hasNext()){
            String line = scanner.nextLine();
            System.out.println(line);
            if(line.equals("bye")){
                break;
            }
        }

    }

}
