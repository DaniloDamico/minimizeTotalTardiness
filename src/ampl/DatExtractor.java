package ampl;

import com.ampl.AMPL;
import com.ampl.DataFrame;
import com.ampl.Parameter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatExtractor {

    public static List<Map.Entry<String, Integer>> extract(AMPL ampl, String parName) {
        Parameter parameter = ampl.getParameter(parName);
        DataFrame data = parameter.getValues();
        List<Map.Entry<String, Integer>> dataList = new ArrayList<>();

        for (int i = 0; i < data.getNumRows(); i++) {
            Object[] row = data.getRowByIndex(i);

            String jobName = extractName(row[0]);
            int value = extractValue((Double) row[1]);

            dataList.add(new AbstractMap.SimpleEntry<>(jobName, value));
        }

        return dataList;
    }

    public static int extractValue(Double d) {
        return d.intValue();
    }

    public static String extractName(Object name) {
        if (name instanceof Double)
            return String.valueOf(extractValue((Double) name));
        else
            return name.toString();
    }
}
