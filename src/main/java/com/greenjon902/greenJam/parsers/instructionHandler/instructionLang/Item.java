package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

/**
 * The storage for a get item operation. e.g. a[3]
 *
 * @param from The object to get it from
 * @param item The "name" of the item to get
 */
public record Item(InstructionLangTreeLeaf from, InstructionLangTreeLeaf item) implements InstructionLangTreeLeaf {
}
