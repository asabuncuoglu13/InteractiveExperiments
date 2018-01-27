package alpay.com.codenotesinteractive.compiler.Components;
import android.util.Log;

import java.util.*;

public class CodeBlocksCompiler {

    /* Lengths of commands
     * Since when the text is built with StringBuilder it adds \n
     * It is ELSE\n so the length is not 4 but 5
     * If and While has conditionals near them so they have the same lengths*/
    int l_start = 6;
    int l_end = 4;
    int l_if = 2;
    int l_set = 3;
    int l_else = 5;
    int l_for = 3;
    int l_while = 5;

    HashMap conditionalMap = new HashMap(8);
    HashMap outputMap = new HashMap(8);
    HashMap whileMap = new HashMap(8);
    HashMap parameterMap = new HashMap(8);

    public CodeBlocksCompiler() {
        /* init conditionalMap */
        conditionalMap.put("3is", -1);
        conditionalMap.put("7is", 1);
        conditionalMap.put("Iam", -1);
        conditionalMap.put("Mymom", 1);
        conditionalMap.put("Microsoft", -1);
        conditionalMap.put("Apple", 1);
        conditionalMap.put("Fork", -1);
        conditionalMap.put("Spoon", 1);

        /*init outputMap */

        outputMap.put("tree", 101); // draw a tree
        outputMap.put("lion", 102); // draw a lion
        outputMap.put("Youtube", 103);
        outputMap.put("Tutorial", 104);
        outputMap.put("cube", 105);
        outputMap.put("sphere", 106);
        outputMap.put("Coldplay", 107);
        outputMap.put("Mor", 108);

        parameterMap.put("ANGLE", 51);
        parameterMap.put("FRICTION", 52);
        parameterMap.put("WEIGHT", 53);
        parameterMap.put("POSITION", 54);
        parameterMap.put("VELOCITY", 55);
        parameterMap.put("ACCELERATION", 56);
    }

    public String createCodeFromText(String codeText) {
        codeText = codeText.replaceAll("\\n", "-");
        codeText = codeText.replaceAll("\\s+", "");
        return codeText;
    }

    /*Checks if the program starts with "START" command
    and ends with "END" command.*/
    public String checkStartEndThenReturnSubCode(String code) {
        try {
            if (code.substring(0, l_start).compareTo("START-") == 0) {
                if (code.substring((code.length() - l_end), code.length()).compareTo("END-") == 0) {
                    String subCode = code.substring(l_start, code.length() - l_end);
                    if (!(subCode.contains("START") || subCode.contains("END"))) {
                        return subCode;
                    }
                }
            }
        } catch (StringIndexOutOfBoundsException se) {
            return null;
        }
        return null;
    }

    private boolean returnValueOfIfStatement(String codeline) {
        if (mapIterator(conditionalMap, codeline) > 0) {
            return true;
        }
        return false;
    }

    public int[] returnSetStatement(String codeline) {
        int param = mapIterator(parameterMap, codeline);
        int[] out = {0,0};
        if (param < 50 || param > 60) {
            Log.d("Hello", "returnSetStatement: ");
        } else {
            codeline = codeline.replaceAll("[^-?0-9]+", " ");
            List param_arr = Arrays.asList(codeline.trim().split(" "));
            if (param_arr.size() == 1) {
                //There is one integer in the codeline
                try {
                    out[0] = param;
                    out[1] = Integer.valueOf((String) param_arr.get(0));
                } catch (NumberFormatException n) {

                }
            }
        }
        return out;
    }


    public int[] returnOutput(String code) {
        int[] out = {1, 0};
        int[] param_out = new int[20];
        String subCode = "subcode";
        if (code != null) {
            subCode = code.substring(0, l_while);
        } else {
            out[0] = -1;
            return out;
        }
        if (subCode.contains("IF")) {
            int elsePosition = code.indexOf("ELSE-");
            String ifConditionalText = code.substring(0 + l_if, findFirstAppearence(code, "-"));
            if (returnValueOfIfStatement(ifConditionalText)) {
                String codeAfterIf = code.substring(0 + 2, elsePosition);
                out[1] = mapIterator(outputMap, codeAfterIf);
                return out;
            } else {
                String codeAfterElse = code.substring(elsePosition + l_else, code.length());
                out[1] = mapIterator(outputMap, codeAfterElse);
                return out;
            }
        }
        else if(subCode.contains("SET"))
        {
            String[] setlines = code.split("-");
            int i = 0;
            for(String line : setlines)
            {
                int[] params = returnSetStatement(line);
                param_out[i] = params[0];
                i++;
                param_out[i] = params[1];
                i++;
            }
            return param_out;
        }
        else if (subCode.contains("FOR")) {
            String times = code.substring(3, findFirstAppearence(code, "times"));
            int outNum = mapIterator(outputMap, code);
            out[0] = Integer.parseInt(times);
            out[1] = outNum;
            return out;
        } else if (subCode.contains("WHILE")) {
            int outNum = mapIterator(outputMap, code);
            out[0] = 50;
            out[1] = outNum;
            return out;
        } else {
            out[1] = mapIterator(outputMap, code);
            return out;
        }// returns -1 if there is no possibility for current code part
    }

    public int mapIterator(HashMap hm, String code) {
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (code.contains((CharSequence) pair.getKey())) {
                it.remove(); // avoids a ConcurrentModificationException
                return (int) pair.getValue();
            }
        }
        it.remove(); // avoids a ConcurrentModificationException
        return -1;
    }

    public int findFirstAppearence(String text, String word) {
        for (int i = 1; (i = text.indexOf(word, i + 1)) != -1; i++) {
            return i;
        }
        return -1;
    }

    public int[] compile(String code) {
        if (code != null) {
            code = createCodeFromText(code);
            code = checkStartEndThenReturnSubCode(code);
            return returnOutput(code);
        } else {
            int[] out = {-1, -1};
            return out;
        }

    }
}


