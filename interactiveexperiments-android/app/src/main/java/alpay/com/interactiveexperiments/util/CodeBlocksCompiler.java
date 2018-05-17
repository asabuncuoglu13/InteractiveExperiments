package alpay.com.interactiveexperiments.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeBlocksCompiler {

    // Lengths of commands
    int l_start = 6;
    int l_end = 4;

    // IDs of Simulation Parameters
    final int EXPERIMENT = 50;
    final int ANGLE = 51;
    final int FRICTION = 52;
    final int WEIGHT = 53;
    final int POSITION = 54;
    final int VELOCITY = 55;
    final int ACCELERATION = 56;
    final int PULLEY_WEIGHT = 57;

    HashMap parameterMap = new HashMap(8);

    public static String compile(String code) {
        CodeBlocksCompiler compiler = new CodeBlocksCompiler();
        code = compiler.deleteExtraSpacesFromCode(code);
        code = compiler.checkStartEndThenReturnSubCode(code);
        if(compiler.chekIfSetExperimentUsedOnce(code))
            return compiler.returnSetExperimentStatement(code);
        else
            return null;
    }

    public CodeBlocksCompiler() {
        parameterMap.put("ANGLE", ANGLE);
        parameterMap.put("FRICTION", FRICTION);
        parameterMap.put("WEIGHT", WEIGHT);
        parameterMap.put("POSITION", POSITION);
        parameterMap.put("VELOCITY", VELOCITY);
        parameterMap.put("ACCELER", ACCELERATION);
        parameterMap.put("PULLEY", PULLEY_WEIGHT);
    }

    public String deleteExtraSpacesFromCode(String codeText) {
        codeText = codeText.replaceAll("\\s+", "");
        return codeText;
    }

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

    public String returnSetExperimentStatement(String codeline) {
        return codeline.substring("SET-EXPERIMENT".length(), codeline.length());
    }

    public double[] returnSetStatement(String codeline) {
        int param = mapIterator(parameterMap, codeline);
        double[] out = {0.0, 0.0};
        if (param < 49 || param > 60) {
            //do smth
        } else {
            codeline = codeline.replaceAll("[^-?0-9]+", " ");
            List param_arr = Arrays.asList(codeline.trim().split(" "));
            if (param_arr.size() == 1) {
                //There is one integer in the codeline
                try {
                    out[0] = (double) param;
                    out[1] = Double.valueOf((String) param_arr.get(0));
                } catch (NumberFormatException n) {

                }
            }
        }
        return out;
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

    public boolean chekIfSetExperimentUsedOnce(String code) {
        Matcher m = Pattern.compile("(?=(SET-EXPERIMENT))").matcher(code);
        if (m.find(2)) return false;
        else return true;
    }

}

