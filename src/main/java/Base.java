import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Программа реализует простой математический фокус для угадывания дня рождения.
 * Полное описание фокуса:
 * см. "2. Угаданный день рождения" по ссылке:
 * https://mel.fm/poleznyye_navyki/5379208-math_focus
 */
public class Base {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Привет, малыш! Давай я угадаю твой день рождения! А именно -- день и месяц! Приступим?");
        System.out.println("После каждого запрошенного действия, просто нажимай Enter...");

        System.out.print("Умножь на 2 число дня своего рождения... ");
        scanner.nextLine(); //ожидание перевода каретки на следующую строку

        System.out.print("Прибавь 5 к результату... ");
        scanner.nextLine();

        System.out.print("Умножь полученное на 50... ");
        scanner.nextLine();

        System.out.print("Прибавить к этому номер своего месяца рождения (июль — 7, январь — 1)... ");
        scanner.nextLine();

        boolean correctInput; //доп.переменная для зацикливания ввода (до получения корректных данных)
        int result = 0;
        do { //зацикливание пользовательского ввода для обеспечения корректных данных
            try {
                System.out.println("Что у тебя получилось? Введи ответ и нажми Enter. ");
                //ввод полученного числа (парсинг из строки нужен т.к. nextInt некорректно работает в случае исключения)
                result = Integer.parseInt(scanner.next());
                correctInput = true;
            } catch (Exception e) { //обработка исключений при вводе некорректных данных (не целых чисел)
                System.out.println("Малыш, кроме целых чисел вводить ничего нельзя! Давай ещё разок?");
                correctInput = false;
            }

        } while (!correctInput);

        //вызываем метод угадывания даты рождения и знака зодиака по введенному числу
        guessBirthday(result);
    }

    /**
     * Основной метод определения даты рождения по полученным данным от пользователя
     *
     * @param result посчитанный результат пользователя
     */
    private static void guessBirthday(int result) {
        int secretResult = result - 250;

        if (secretResult > 3112 || secretResult < 101) {    //некорректный расчёт за пределами возможных дат
            System.err.println("Похоже ты ошибся в вычислениях, малыш! Попробуй ещё разок как-нибудь!");
        } else if (secretResult / 1000 != 0) {              //четырёхзначное число
            System.out.println("Твой день рождения - " + convertNumberToBirthday(secretResult, 4) + "!");
            System.out.println("Твой знак зодиака - " + convertNumberToZodiac(secretResult, 4) + "!");
        } else {                                            //трехзначное число
            System.out.println("Твой день рождения - " + convertNumberToBirthday(secretResult, 3) + "!");
            System.out.println("Твой знак зодиака - " + convertNumberToZodiac(secretResult, 3) + "!");
        }
    }

    /**
     * Метод перевода искомого 3-х или 4-х-значного числа в дату рождения (месяц и день).
     *
     * @param number        измененное число, полученное от пользователя
     * @param digitsCounter количество цифр в числе, 3 или 4
     * @return дата в удобном формате (dd MMMM или d MMMM)
     */
    private static String convertNumberToBirthday(int number, int digitsCounter) {
        //инициализируем переменные цифр искомого числа, от первого до четвертого
        int firstNumber, secondNumber, thirdNumber, fourthNumber;

        if (digitsCounter == 4) { //для четырехзначного числа
            //выделяем цифры из общего числа
            firstNumber = number / 1000;
            secondNumber = (number - firstNumber * 1000) / 100;
            thirdNumber = (number - firstNumber * 1000 - secondNumber * 100) / 10;
            fourthNumber = number - firstNumber * 1000 - secondNumber * 100 - thirdNumber * 10;

            int birthDay = firstNumber * 10 + secondNumber; // вычисляем день рождения
            int birthMonth = thirdNumber * 10 + fourthNumber; // вычисляем месяц рождения

            //создаем доп.переменную даты (2012 год выбран как високосный, чтобы программа учла 29 февраля)
            LocalDate date = LocalDate.of(2012, birthMonth, birthDay);

            //в случае если у пользователя сегодня день рождения -- поздравляем его :)
            LocalDate currentDate = LocalDate.now();
            if ((currentDate.getMonth().getValue() == birthMonth) && (currentDate.getDayOfMonth() == birthDay)) {
                System.out.println("Опачки! C днем рождения, малыш!");
            }

            //возвращаем результат в удобном для чтения формате
            return date.format(DateTimeFormatter.ofPattern("dd MMMM"));

        } else {  //для трехзначного числа
            //выделяем цифры из общего числа
            firstNumber = number / 100;
            secondNumber = (number - firstNumber * 100) / 10;
            thirdNumber = number - firstNumber * 100 - secondNumber * 10;

            int birthDay = firstNumber; // вычисляем день рождения
            int birthMonth = secondNumber * 10 + thirdNumber; // вычисляем месяц рождения

            //создаем доп.переменную даты (2012 год выбран как високосный, чтобы программа учла 29 февраля)
            LocalDate date = LocalDate.of(2012, birthMonth, birthDay);

            //в случае если у пользователя сегодня день рождения -- поздравляем его :)
            LocalDate currentDate = LocalDate.now();
            if ((currentDate.getMonth().getValue() == birthMonth) && (currentDate.getDayOfMonth() == birthDay)) {
                System.out.println("Опачки! C днем рождения, малыш!");
            }

            return date.format(DateTimeFormatter.ofPattern("d MMMM"));
        }
    }

    /**
     * Метод перевода искомого 3-х или 4-х-значного числа в знак зодиака.
     *
     * @param number        измененное число, полученное от пользователя
     * @param digitsCounter количество цифр в числе, 3 или 4
     * @return знак Зодиака в виде строки
     */
    private static String convertNumberToZodiac(int number, int digitsCounter) {
        //инициализируем переменные цифр искомого числа, от первого до четвертого
        int firstNumber, secondNumber, thirdNumber, fourthNumber;

        if (digitsCounter == 4) { //для четырехзначного числа
            //выделяем цифры из общего числа
            firstNumber = number / 1000;
            secondNumber = (number - firstNumber * 1000) / 100;
            thirdNumber = (number - firstNumber * 1000 - secondNumber * 100) / 10;
            fourthNumber = number - firstNumber * 1000 - secondNumber * 100 - thirdNumber * 10;

            int birthDay = firstNumber * 10 + secondNumber; // вычисляем день рождения
            int birthMonth = thirdNumber * 10 + fourthNumber; // вычисляем месяц рождения

            //вызываем метод определения знака зодиака по дате рождения
            return calculateZodiac(birthDay, birthMonth);
        } else { //для трехзначного числа
            //выделяем цифры из общего числа
            firstNumber = number / 100;
            secondNumber = (number - firstNumber * 100) / 10;
            thirdNumber = number - firstNumber * 100 - secondNumber * 10;

            int birthDay = firstNumber; // вычисляем день рождения
            int birthMonth = secondNumber * 10 + thirdNumber; // вычисляем месяц рождения

            //вызываем метод определения знака зодиака по дате рождения
            return calculateZodiac(birthDay, birthMonth);
        }
    }

    /**
     * Метод вычисления знака Зодиака по дню и месяцу рождения.
     *
     * @param dayOfTheMonth день рождения
     * @param month         месяц рождения
     * @return знак Зодиака в виде строки
     */
    private static String calculateZodiac(int dayOfTheMonth, int month) {
        switch (month) {
            case 1:
                if (dayOfTheMonth > 20)
                    return "Водолей";
                else
                    return "Козерог";
            case 2:
                if (dayOfTheMonth > 20)
                    return "Рыбы";
                else
                    return "Водолей";
            case 3:
                if (dayOfTheMonth > 20)
                    return "Овен";
                else
                    return "Рыбы";
            case 4:
                if (dayOfTheMonth > 20)
                    return "Телец";
                else
                    return "Овен";
            case 5:
                if (dayOfTheMonth > 20)
                    return "Близнецы";
                else
                    return "Телец";
            case 6:
                if (dayOfTheMonth > 20)
                    return "Рак";
                else
                    return "Близнецы";
            case 7:
                if (dayOfTheMonth > 20)
                    return "Лев";
                else
                    return "Рак";
            case 8:
                if (dayOfTheMonth > 20)
                    return "Дева";
                else
                    return "Лев";
            case 9:
                if (dayOfTheMonth > 20)
                    return "Весы";
                else
                    return "Дева";
            case 10:
                if (dayOfTheMonth > 20)
                    return "Скорпион";
                else
                    return "Весы";
            case 11:
                if (dayOfTheMonth > 20)
                    return "Стрелец";
                else
                    return "Скорпион";
            case 12:
                if (dayOfTheMonth > 20)
                    return "Козерог";
                else
                    return "Стрелец";
            default:
                return null;
        }
    }
}
