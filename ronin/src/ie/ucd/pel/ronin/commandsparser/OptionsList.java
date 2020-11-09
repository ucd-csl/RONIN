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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Come CACHARD
 *
 * Object that contains the list of available options.
 */
public final class OptionsList {

    /**
     * Map that contains all the available required options mapped by their
     * name.
     */
    private final Map<String, Option> requiredOptions;

    /**
     * Map that contains all the available optional options mapped by their
     * name.
     */
    private final Map<String, Option> optionalOptions;

    /**
     * Map that contains the mapping between the shortcut of an option and its
     * name;
     */
    private final Map<String, String> optionsShortcuts;

    /**
     * Constructs and initializes a list of options with an empty list of
     * options.
     */
    public OptionsList() {
        requiredOptions = new HashMap<>();
        optionalOptions = new HashMap<>();
        optionsShortcuts = new HashMap<>();

        Option optionHelp = new Option("help", "h", false, false, "print the usage of this application.");
        this.addOption(optionHelp);
    }

    /**
     * Returns a non modifiable view of the required options.
     *
     * @return a non modifiable view of the required options
     */
    public Map<String, Option> getRequiredOptions() {
        return Collections.unmodifiableMap(requiredOptions);
    }

    /**
     * Returns a non modifiable view of the optional options.
     *
     * @return a non modifiable view of the optional options
     */
    public Map<String, Option> getOptionalOptions() {
        return Collections.unmodifiableMap(optionalOptions);
    }

    /**
     * Returns a non modifiable view of the available options.
     *
     * @return a non modifiable view of the available options
     */
    public Map<String, Option> getOptions() {
        Map<String, Option> optionsList = new HashMap<>(requiredOptions);
        optionsList.putAll(optionalOptions);
        return Collections.unmodifiableMap(optionsList);
    }

    /**
     * Returns the Option associated to the given name if found in the list of
     * available options; otherwise returns null if Option not found.
     *
     * @param optionName the name of the Option we want
     * @return the Option associated to the given name if found; null otherwise
     */
    public Option getOptionWithName(final String optionName) {
        if (requiredOptions.containsKey(optionName)) {
            return requiredOptions.get(optionName);
        } else if (optionalOptions.containsKey(optionName)) {
            return optionalOptions.get(optionName);
        }

        return null;
    }

    /**
     * Returns the Option associated to the given shortcut if found in the list
     * of available options; otherwise returns null if Option not found.
     *
     * @param optionShortcut the shortcut of the Option we want
     * @return the Option associated to the given shortcut if found; null
     * otherwise
     */
    public Option getOptionWithShortcut(final String optionShortcut) {
        String optionName = optionsShortcuts.get(optionShortcut);

        if (optionName == null) {
            return null;
        }

        return getOptionWithName(optionName);
    }

    /**
     * Returns the name of the option associated to the given shortcut. Returns
     * null if not found.
     *
     * @param shortcut the shortcut of the option we want the full name
     * @return the name of the option associated to the given shortcut. Returns
     * null if not found.
     */
    public String getOptionNameWithShortcut(final String shortcut) {
        return optionsShortcuts.get(shortcut);
    }

    /**
     * Return a non modifiable view of the mapping between option's shortcut and
     * their name.
     *
     * @return a non modifiable view of the mapping between option's shortcut
     * and their name
     */
    public Map<String, String> getOptionsShortcutsMapping() {
        return Collections.unmodifiableMap(optionsShortcuts);
    }

    /**
     * Adds an option to the list of available options.
     *
     * @param option the option to add to the list of available options.
     */
    public void addOption(final Option option) {
        checksIfNameAlreadyUsed(option.getName());
        checksIfShortcutAlreadyUsed(option.getShortcut());
        if (option.isRequired()) {
            requiredOptions.put(option.getName(), option);
        } else {
            optionalOptions.put(option.getName(), option);
        }

        if (!option.getShortcut().isEmpty() && option.getShortcut() != null) {
            optionsShortcuts.put(option.getShortcut(), option.getName());
        }

    }

    /**
     * Checks if the given shortcut is already used by another option. If it is
     * already used, we quit and print an error to the user.
     *
     * @param shortcut the shortcut we want to check
     */
    private void checksIfShortcutAlreadyUsed(String shortcut) {
        if (optionsShortcuts.containsKey(shortcut)) {
            throw new RuntimeException("Error : the shortcut " + PREFIX_OPTION_SHORTCUT + shortcut
                    + " is already used for option " + PREFIX_OPTION_FULLNAME + optionsShortcuts.get(shortcut));
        }

        Map<String, Option> options = getOptions();
        if (options.containsKey(shortcut)) {
            throw new RuntimeException("Error : the name " + shortcut
                    + " is already used for option " + PREFIX_OPTION_FULLNAME + options.get(shortcut));
        }
    }

    /**
     * Checks if the given name is already used by another option. If it is
     * already used, we quit and print an error to the user.
     *
     * @param optionName the name we want to check
     */
    private void checksIfNameAlreadyUsed(String optionName) {
        if (optionsShortcuts.containsKey(optionName)) {
            throw new RuntimeException("Error : the name " + optionName
                    + " is already used for shortcut of the option " + PREFIX_OPTION_FULLNAME + optionsShortcuts.get(optionName));
        }

        Map<String, Option> options = getOptions();
        if (options.containsKey(optionName)) {
            throw new RuntimeException("Error : the name " + optionName + " is already used.");
        }
    }

}
