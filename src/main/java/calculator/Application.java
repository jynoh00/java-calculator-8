package calculator;

import camp.nextstep.edu.missionutils.Console;
import java.util.regex.Pattern;

public class Application {
    private static final char NO_CUSTOM_SEPARATOR = '1';
    private static final char HAS_CUSTOM_SEPARATOR_FLAG = 't';
    private static final char NO_CUSTOM_SEPARATOR_FLAG = 'f';

    public static void main(String[] args) {
        try{
            System.out.println("덧셈할 문자열을 입력해 주세요.");
            String inputStr = Console.readLine();

            if (inputStr.isEmpty()){
                System.out.println("결과 : " + 0);
                return;
            }

            // 커스텀 구분자
            char[] customSeparator = getCustomSeparator(inputStr); // 커스텀 구분자 없음 -> '1'
//            System.out.println(custom_separator);

            // 커스텀세파레이터가 숫자면 에러 처리
            if (customSeparator[1] == HAS_CUSTOM_SEPARATOR_FLAG && Character.isDigit(customSeparator[0])) throw new IllegalArgumentException("커스텀 구분자는 숫자일 수 없음.");
            inputStr = customSeparator[1] == HAS_CUSTOM_SEPARATOR_FLAG ? inputStr.substring(5) : inputStr;

            // ex) "//;\n" -> 0
            if (inputStr.isEmpty()){
                System.out.println("결과 : " + 0);
                return;
            }

            // 구분자 설정
            String separators = customSeparator[1] == 'f'? ",|:" : ",|:|" + Pattern.quote(String.valueOf(customSeparator[0]));
            String[] resultArr = inputStr.split(separators);

            int separator_count = countSeparators(inputStr, customSeparator[0], customSeparator[1] == HAS_CUSTOM_SEPARATOR_FLAG);
            if (separator_count+1 != resultArr.length) throw new IllegalArgumentException("구분자와 숫자의 개수가 형식과 맞지 않음.");

            long answer = 0;
            for (String s : resultArr){
                if (s.isEmpty()) throw new IllegalArgumentException("빈 값은 입력할 수 없음.");

                try{
                    int number = Integer.parseInt(s);
                    if (number <= 0) throw new IllegalArgumentException("양수값만 입력 할 수 있음.");
                    answer += number;
                }catch(NumberFormatException e){
                    throw new IllegalArgumentException("입력 숫자값이 int 범위를 벗어남.");
                }
            }

            System.out.println("결과 : " + answer);
        }catch(IllegalArgumentException e){
            System.out.println("잘못된 입력: " + e.getMessage());
            throw e;
        }finally{
            Console.close();
        }
    }

    public static char[] getCustomSeparator(String str){
        char[] returnArr = {NO_CUSTOM_SEPARATOR, NO_CUSTOM_SEPARATOR_FLAG}; // 커스텀 구분자, 커스텀 구분자 존재 t/f

        if (str.startsWith("//")){
            if (str.length() < 5) throw new IllegalArgumentException("커스텀 구분자 형식 불일치");

            char separator = str.charAt(2);

            if (str.charAt(3) == '\\' && str.charAt(4) == 'n'){
                returnArr[0] = separator;
                returnArr[1] = HAS_CUSTOM_SEPARATOR_FLAG;
            }else throw new IllegalArgumentException("커스텀 구분자 형식 불일치");
        }

        return returnArr;
    }

    public static int countSeparators(String str, char custom_separator, boolean has_custom){
        int returnCnt = 0;
        for (char c : str.toCharArray()){
            if (c == ',' || c == ':' || (has_custom && c == custom_separator)) returnCnt++;
        }

        return returnCnt;
    }
}