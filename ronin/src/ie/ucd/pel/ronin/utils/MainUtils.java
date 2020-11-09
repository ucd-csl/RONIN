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
package ie.ucd.pel.ronin.utils;

import ie.ucd.pel.ronin.commandsparser.CommandLine;
import ie.ucd.pel.ronin.commandsparser.CommandLineParser;
import ie.ucd.pel.ronin.commandsparser.OptionsList;
import ie.ucd.pel.ronin.main.RoninCommandLineConfigurationInfos;

/**
 *
 * @author Come CACHARD
 *
 * Class providing usefull methods for Main Class
 */
public class MainUtils {

    /**
     * Get the Ronin configuration from the command line arguments.
     *
     * @param args the args of the command line
     * @return the ronin configuration
     */
    public static RoninCommandLineConfigurationInfos getRoninCmdConfiguration(String[] args) {
        OptionsList options = RoninCommandLineConfigurationInfos.getParserOptionsList();

        CommandLineParser cmdParser = new CommandLineParser(options, "Ronin");
        CommandLine cmdLine = null;
        try {
            cmdLine = cmdParser.parse(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("");
            cmdParser.printUsage();
            System.exit(1);
        }

        return new RoninCommandLineConfigurationInfos(cmdLine);
    }

}
