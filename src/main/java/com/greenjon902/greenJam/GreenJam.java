package com.greenjon902.greenJam;

import com.greenjon902.greenJam.common.*;
import com.greenjon902.greenJam.instructionHandler.StandardInstructionHandler;
import com.greenjon902.greenJam.parser.Parser;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GreenJam {
    public static final String NAME = "GreenJam";
    public static final String AUTHOR = "GreenJon902";
    public static final String VERSION = "Alpha-V3";
    public static final String DESCRIPTION = "The build, run, and control tools for the GreenJam language";

    public static StringInputStream load_file_contents(String packName, String type) {
        URL url = GreenJam.class.getResource("/com/greenjon902/greenJam/syntaxPacks/" + packName);
        if (url == null) {
            try {
                url = new File(packName).toURI().toURL();
            } catch (MalformedURLException e) {
                Errors.packLoading_doesntExist(packName, type);
                return null;  // Will never get here
            }
        }
        try {
            File file = Paths.get(url.toURI()).toFile();
            try {
                StringInputStream stringInputStream = StringInputStream.from(file);
                return stringInputStream;
            } catch (IOException e) {
                Errors.packLoading_doesntExist(file.toString(), type);
                return null;  // Will never get here
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws ArgumentParserException {
        ArrayList<StringInputStream> pre_packs = new ArrayList<>();
        ArrayList<StringInputStream> post_packs = new ArrayList<>();

        ArgumentParser argumentParser = buildArgumentParser();
        Namespace namespace = argumentParser.parseArgs(args);

        for (String pre : (List<String>) namespace.get("pre_packs")) {
            StringInputStream stringInputStream = load_file_contents(pre, "pre-pack");
            pre_packs.add(stringInputStream);
        }

        for (String post : (List<String>) namespace.get("post_packs")) {
            StringInputStream stringInputStream = load_file_contents(post, "post-pack");
            post_packs.add(stringInputStream);
        }

        StringInputStream entry_file = load_file_contents(namespace.get("entry_file").toString(), "entry-file");

        // All args are parsed, we can now move on
        System.out.println(NAME + " by " + AUTHOR);
        System.out.println("     " + "VERSION " + VERSION);

        SyntaxContext syntaxContext = new SyntaxContext();
        ErrorContext errorContext = new ErrorContext();
        StandardInstructionHandler instructionHandler = new StandardInstructionHandler(new Contexts(syntaxContext, errorContext));
        Parser parser = new Parser(instructionHandler, syntaxContext, errorContext);

        ArrayList<AstNode> astNodes = new ArrayList<>();
        for (StringInputStream pre_pack : pre_packs) {
            astNodes.add(parser.parse(pre_pack));
        }
        astNodes.add(parser.parse(entry_file));
        for (StringInputStream post_pack : post_packs) {
            astNodes.add(parser.parse(post_pack));
        }
        System.out.println(astNodes);

        System.exit(0);
    }

    public static ArgumentParser buildArgumentParser() {
        ArgumentParser parser = ArgumentParsers.newFor(NAME).build()
                .defaultHelp(true)
                .description(DESCRIPTION)
                .version(VERSION);

        parser.addArgument("entry-file")
                .type(File.class);

        parser.addArgument("-b", "--pre-packs")
                .nargs("*")
                .type(String.class)
                .setDefault(List.of(new String[]{"jam.jam"}));
        parser.addArgument("-a", "--post-packs")
                .nargs("*")
                .type(String.class)
                .setDefault();

        return parser;
    }
}
