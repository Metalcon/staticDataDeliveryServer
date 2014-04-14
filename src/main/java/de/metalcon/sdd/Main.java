package de.metalcon.sdd;

import java.io.IOException;

import de.metalcon.sdd.config.ClassGenerator;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length >= 1) {
            String[] argsWithoutClass = new String[args.length - 1];
            System.arraycopy(args, 1, argsWithoutClass, 0,
                    argsWithoutClass.length);

            switch (args[0]) {
                case "classGenerator":
                    ClassGenerator.main(argsWithoutClass);
                    break;

                case "sdd":
                    StaticDataDelivery.main(argsWithoutClass);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid main class: "
                            + args[0]);
            }
        } else {
            StaticDataDelivery.main(new String[0]);
        }
    }

}
