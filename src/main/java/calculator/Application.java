package calculator;

import camp.nextstep.edu.missionutils.Console;
import java.util.regex.Pattern;

public class Application {
    private static final String CUSTOM_SEPARATOR_BEFORE = "//";
    private static final String CUSTOM_SEPARATOR_AFTER = "\\n";
    private static final int CUSTOM_SEPARATOR_LENGTH = 5;
    private static final String DEFAULT_SEPARATORS = ",|:";

    public static void main(String[] args) {
        try{
            String inputStr = readInput();
            long answer = calculate(inputStr);
            printAnswer(answer);
        }catch(IllegalArgumentException e){
            System.out.println("잘못된 입력: " + e.getMessage());
            throw e;
        }finally{
            Console.close();
        }
    }

    private static String readInput(){
        System.out.println("덧셈할 문자열을 입력해 주세요.");
        return Console.readLine();
    }

    // 합 연산 결과 리턴
    private static long calculate(String inputStr){
        if (inputStr.isEmpty()) return 0;

        CustomSeparator separator = extractCustomSeparator(inputStr);
        String numberString = getNumberString(inputStr, separator);

        if (numberString.isEmpty()) return 0;

        return sumNumbers(numberString, separator);
    }

    // 커스텀 구분자 추출
    private static CustomSeparator extractCustomSeparator(String inputStr){
        if (!inputStr.startsWith(CUSTOM_SEPARATOR_BEFORE)) return new CustomSeparator(null, false);

        checkCustomSeparatorFormat(inputStr);
        char separator = inputStr.charAt(2);
        checkSeparatorIsNotDigit(separator);

        return new CustomSeparator(separator, true);
    }

    // 커스텀 구분자 형식 확인
    private static void checkCustomSeparatorFormat(String inputStr){
        if (inputStr.length() < CUSTOM_SEPARATOR_LENGTH) throw new IllegalArgumentException("커스텀 구분자 형식 오류.");
        if (!inputStr.substring(3, 5).equals(CUSTOM_SEPARATOR_AFTER)) throw new IllegalArgumentException("커스텀 구분자 형식 오류.");
    }

    // 커스텀 구분자 숫자 여부 확인
    private static void checkSeparatorIsNotDigit(char separator){
        if (Character.isDigit(separator)) throw new IllegalArgumentException("커스텀 구분자 숫자 불가.");
    }

    // 구분자 설정 부분 문자열에서 제거
    private static String getNumberString(String inputStr, CustomSeparator separator){
        if (separator.hasCustomSeparator()) return inputStr.substring(CUSTOM_SEPARATOR_LENGTH);
        return inputStr;
    }

    // 문자열 분리 후 합 연산
    private static long sumNumbers(String numberString, CustomSeparator separator){
        String separators = buildSeparatorPattern(separator);
        String[] numbers = numberString.split(separators);

        checkNumberCount(numberString, numbers.length, separator);

        long sum = 0;
        for (String number : numbers){
            sum += parseAndCheckNumber(number);
        }

        return sum;
    }

    // 구분자 설정 값 리턴
    private static String buildSeparatorPattern(CustomSeparator separator){
        if (!separator.hasCustomSeparator()) return DEFAULT_SEPARATORS;
        return DEFAULT_SEPARATORS + "|" + Pattern.quote(String.valueOf(separator.getSeparator()));
    }

    // 연산자, 피연산자 개수 확인
    private static void checkNumberCount(String numberString, int numberCount, CustomSeparator separator){
        int separatorCount = countSeparators(numberString, separator);
        if (separatorCount+1 != numberCount) throw new IllegalArgumentException("구분자와 숫자 개수 형식 불일치.");
    }

    // 구분자 개수 리턴
    private static int countSeparators(String numberString, CustomSeparator separator){
        int count = 0;
        for (char c : numberString.toCharArray()){
            if (isSeparator(c, separator)) count++;
        }

        return count;
    }

    // 구분자 여부 boolean 리턴
    private static boolean isSeparator(char c, CustomSeparator separator){
        if (c == ',' || c == ':') return true;
        return separator.hasCustomSeparator() && c == separator.getSeparator();
    }

    // 입력 값 int 범위 여부 확인 및 숫자형 포맷 여부 확인
    private static int parseAndCheckNumber(String number){
        if (number.isEmpty()) throw new IllegalArgumentException("빈 값 입력 불가.");

        try{
            int num = Integer.parseInt(number);
            checkPositive(num);

            return num;
        }catch(NumberFormatException e){
            throw new IllegalArgumentException("올바른 숫자 형식이 아님.");
        }
    }

    // 양수 여부 판단
    private static void checkPositive(int number){
        if (number <= 0) throw new IllegalArgumentException("양수만 입력 가능.");
    }

    // output 출력
    private static void printAnswer(long answer){
        System.out.println("결과 : " + answer);
    }


    private static class CustomSeparator{
        private final Character separator;
        private final boolean hasCustomSeparator;

        public CustomSeparator(Character separator, boolean hasCustomSeparator){
            this.separator = separator;
            this.hasCustomSeparator = hasCustomSeparator;
        }

        public char getSeparator(){ return separator; }
        public boolean hasCustomSeparator(){ return hasCustomSeparator; }
    }
}