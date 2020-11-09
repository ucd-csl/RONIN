/* 
 * Copyright (C) 2017 Come CACHARD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ie.ucd.pel.ronin.commandsparser;

import static ie.ucd.pel.ronin.commandsparser.Option.PREFIX_OPTION_FULLNAME;
import static ie.ucd.pel.ronin.commandsparser.Option.PREFIX_OPTION_SHORTCUT;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Come CACHARD
 *
 * Object that parses command line arguments and create a command line that
 * describes the options given by the user.
 */
public class CommandLineParser {

    /**
     * The list of all available options.
     */
    private final OptionsList optionsList;

    /**
     * The name of the application.
     */
    private final String applicationName;

    /**
     * Constructs and initializes a command line parser with the available
     * options that can be given and considered.
     *
     * @param optionsList the available options that can be given and considered
     * @param applicationName the name of the application
     */
    public CommandLineParser(OptionsList optionsList, String applicationName) {
        this.optionsList = optionsList;
        this.applicationName = applicationName;
    }

    /**
     * Parses commands given in arguments and returns a command line containing
     * the options given by the user and their values.
     *
     * @param args the commands to parse
     * @return a command line containing the options given by the user and their
     * values
     */
    public CommandLine parse(String[] args) {
        CommandLine cmd = new CommandLine(optionsList);

        //boolean used in order to be sure that we have options before their values.
        //Initialized to false because we must start with an optionName.
        boolean lookingForArgumentValuesAvailable = false;
        //used to know if the arg is an optionName or a value.
        boolean isArgAnOption = false;
        String optionName = null;
        //tampon name
        String futureOptionName = null;
        List<String> values = null;

        int i = 0;
        for (String arg : args) {

            isArgAnOption = false;

            if (arg.startsWith(PREFIX_OPTION_FULLNAME)) {
                isArgAnOption = true;
                futureOptionName = arg.substring(PREFIX_OPTION_FULLNAME.length());
                checkOptionHelp(futureOptionName);
            } else if (arg.startsWith(PREFIX_OPTION_SHORTCUT)) {
                isArgAnOption = true;
                futureOptionName = optionsList.getOptionNameWithShortcut(arg.substring(PREFIX_OPTION_SHORTCUT.length()));
                checkOptionHelp(futureOptionName);
            }

            if (isArgAnOption) {

                if (values != null) {
                    cmd.addUsedOption(optionName, values);
                }

                optionName = futureOptionName;
                values = new LinkedList();

                lookingForArgumentValuesAvailable = true;

            } else {

                //We cannot have an argument before an optionName
                if (lookingForArgumentValuesAvailable == false) {
                    throw new RuntimeException("Error : an argument has been given without an option.");
                }

                values.add(arg);
            }

            //if we are considering the last arg, and that we are going to go out of the loop.
            if (i == args.length - 1) {
                cmd.addUsedOption(optionName, values);
            }

            i++;
        }

        checkRequiredOptions(cmd);

        return cmd;
    }

    /**
     * Checks if all the required otions have been given by the user. If some
     * are missing we throw an exception.
     *
     * @param cmd the command line that contains the options given by the user.
     */
    private void checkRequiredOptions(CommandLine cmd) {
        Set<String> requiredOptions = optionsList.getRequiredOptions().keySet();
        Map<String, List<String>> givenOptions = cmd.getUsedOptions();
        List<Option> missingOptions = new LinkedList<>();

        for (String requiredKey : requiredOptions) {
            if (!givenOptions.containsKey(requiredKey)) {
                missingOptions.add(optionsList.getOptionWithName(requiredKey));
            }
        }

        if (!missingOptions.isEmpty()) {
            StringBuilder sb = new StringBuilder("Error : the following options have not been given while required :");

            for (Option o : missingOptions) {
                sb.append("\n").append("\t").append(o.getUsage());
            }

            throw new RuntimeException(sb.toString());
        }

    }

    /**
     * Checks if the command help is used by the user. If it is used, we print
     * the usage of the application and exit.
     */
    private void checkOptionHelp(String optionName) {
        if ("help".equals(optionName)) {
            printUsage();
            System.exit(0);
        }
    }

    /**
     * Prints the usage of the application to the user.
     */
    public void printUsage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage : ").append(applicationName);

        optionsList.getRequiredOptions().values().stream().forEach((option) -> {
            sb.append(" ").append(option.getCmdUsage());
        });
        optionsList.getOptionalOptions().values().stream().forEach((option) -> {
            sb.append(" ").append(option.getCmdUsage());
        });

        sb.append("\n").append("\t").append("Required options :");
        optionsList.getRequiredOptions().values().stream().forEach((requiredOption) -> {
            sb.append("\n").append("\t").append("\t").append(requiredOption.getUsage());
        });
        sb.append("\n");
        sb.append("\n").append("\t").append("Optional options :");
        optionsList.getOptionalOptions().values().stream().forEach((optionalOption) -> {
            sb.append("\n").append("\t").append("\t").append(optionalOption.getUsage());
        });

        System.out.println(sb.toString());
    }
}
