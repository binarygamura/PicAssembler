package org.pureanger.piniata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author binary
 */
public class Assembler {
    
    private static final Logger LOGGER = LogManager.getLogger(Assembler.class);
    
    private Assembler() {
        
    }
    
    public static void main(String[] args) {
        System.out.println("Piniata\n=======\nver.: v.0.1-SNAFU\n");
        Option helpOption = Option.builder("h")
                .desc("show this help page.")
                .longOpt("help")
                .build();
            Option outputFileOption = Option.builder("o")
                .desc("name of the output file. if not specified, a file named \"out.hex\" will be used.")
                .longOpt("outfile")
                    .hasArg()
                        .argName("path to outfile")
                .build();

            Options options = new Options();
            options.addOption(helpOption);
            options.addOption(outputFileOption);
            
        try {
            if(args.length == 0) {
                throw new ParseException("please enter the name of the inputfile as the last argument.");
            }
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(options, args); 
            
            if(commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar Piniata.jar <options> <outputfile>", options);
            }
            else {
                String pathToSourceFile = args[args.length - 1];
                Path sourceFile = Paths.get(pathToSourceFile);
                if(Files.notExists(sourceFile)) {
                    throw new AssemblerUsageException("the inputfile \""+pathToSourceFile+"\" doesnt exist.");
                }
                LOGGER.info("parsing file "+pathToSourceFile);
                
                String pathToOutFile = commandLine.hasOption("o") ? commandLine.getOptionValue("o") : "out.hex";
                Path outputFile = Paths.get(pathToOutFile);
                Files.createFile(outputFile);
                LOGGER.info("output will be stored in \""+pathToOutFile+"\"");
            }
        }
        catch(AssemblerUsageException ex) {
            LOGGER.error(ex.getMessage());
        }
        catch(ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();
            System.out.println("error: "+ex.getMessage());
            formatter.printHelp("java -jar Piniata.jar <options> <outputfile>", options);
        }
        catch(IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }   
}