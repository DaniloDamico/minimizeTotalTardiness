import com.ampl.AMPL;
import com.ampl.Environment;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        try (AMPL ampl = new AMPL(new Environment("D:/ampl.mswin64"));) {

            ampl.setOption("solver", "cplex");
            String modelDirectory = "./model";

            ampl.read(modelDirectory + "/scheduling.mod");
            ampl.readData(modelDirectory + "/paper.dat");

            LawlerAlgorythm solver = new LawlerAlgorythm(ampl);
            solver.solve();
        }
    }




}