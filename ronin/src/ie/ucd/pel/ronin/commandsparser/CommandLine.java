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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Come CACHARD
 *
 * Describes a list of options of a command line.
 */
public class CommandLine {

    /**
     * The map of options in command lines that are given by the user mapped to
     * their values if they have one. Else the required option is mapped to an
     * empty List if it does not have an argument value.
     */
    private final Map<String, List<String>> usedOptions;

    /**
     * The list of all available options.
     */
    private final OptionsList optionsList;

    /**
     * Constructs and initializes a command line with the available options that
     * can be given and considered.
     *
     * @param optionsList the available options that can be given and considered
     */
    public CommandLine(OptionsList optionsList) {
        this.optionsList = optionsList;
        usedOptions = new HashMap<>();
    }

    /**
     * Returns a non modifiable view of the values associated to an option if
     * used by the user; null otherwise.
     *
     * @param optionName the name of the option we want the values
     * @return a non modifiable view of the values associated to an otpion if
     * used by the user; null otherwise.
     */
    public List<String> getValuesForOption(String optionName) {
        if (usedOptions.containsKey(optionName)) {
            return Collections.unmodifiableList(usedOptions.get(optionName));
        }
        return null;
    }

    /**
     * Returns a non modifiable view of the values of the arguments of an option
     * if they are used by the user; null otherwise.
     *
     * @param optionName the name of the option we want the values of its
     * arguments
     * @return the values of the arguments of an option if they are used by the
     * user; null otherwise.
     */
    public List<String> getOptionArgumentValues(String optionName) {
        Option option = optionsList.getOptionWithName(optionName);
        if (!option.hasArgument()) {
            throw new RuntimeException("Error : the specified option does not take arguments.");
        }
        return Collections.unmodifiableList(usedOptions.get(option.getName()));
    }

    /**
     * Returns a non modifiable view of the values of the arguments of an option
     * if they are used by the user; null otherwise.
     *
     * @param option the option we want the values of its arguments
     * @return the values of the arguments of an option if they are used by the
     * user; null otherwise.
     */
    public List<String> getOptionArgumentValues(Option option) {
        if (!option.hasArgument()) {
            throw new RuntimeException("Error : the specified option does not take arguments.");
        }
        return Collections.unmodifiableList(usedOptions.get(option.getName()));
    }

    /**
     * Returns a non modifiable view of the options contained in the command
     * line.
     *
     * @return a non modifiable view of the options contained in the command
     * line
     */
    public Map<String, List<String>> getUsedOptions() {
        return Collections.unmodifiableMap(usedOptions);
    }

    /**
     * Returns the options that are considered for this command line.
     *
     * @return the options that are considered for this command line
     */
    public OptionsList getOptionsList() {
        return optionsList;
    }

    /**
     * Adds to the list of options used by the user a new option.
     *
     * @param optionName the name of the option given by the user
     * @param values the values associated to the option
     */
    public void addUsedOption(String optionName, List<String> values) {
        if (usedOptions.containsKey(optionName)) {
            throw new RuntimeException("Error : The option " + optionName + " is used more than once.");
        }

        if (values == null) {
            throw new RuntimeException("Error : values cannot be null. If the option has no arguments, it is an empty list.");
        }

        Option option = optionsList.getOptionWithName(optionName);
        if (option == null) {
            throw new RuntimeException("Error : the option \"" + optionName + "\" is not recognized.");
        }
        if (option.hasArgument() && values.isEmpty()) {
            throw new RuntimeException("Error : the option \"" + optionName + "\" requires arguments  but has not any.");
        }

        if (option.getNbArguments() >= 0 && values.size() != option.getNbArguments()) {
            throw new RuntimeException("Error : the option \"" + optionName + "\" must take "
                    + option.getNbArguments() + " argument(s) but received " + values.size() + " argument(s)");
        }

        usedOptions.put(optionName, values);
    }

    /**
     * Returns true if the option is used by the user; otherwise false.
     *
     * @param optionName the name of the option we want to consider
     * @return true if the option is used by the user; otherwise false
     */
    public boolean isOptionUsed(String optionName) {
        return usedOptions.containsKey(optionName);
    }

    /**
     * Returns true if the option is used by the user; otherwise false.
     *
     * @param option the option we want to consider
     * @return true if the option is used by the user; otherwise false
     */
    public boolean isOptionUsed(Option option) {
        return isOptionUsed(option.getName());
    }
}
