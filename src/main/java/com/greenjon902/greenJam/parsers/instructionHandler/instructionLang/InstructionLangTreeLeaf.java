package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

/**
 * The base interface for an object that can be tokenized or parsed by the instruction lang.
 * These can represent both tokens, and tree items. The tokenizer only spits out tokens, the parser uses those and makes
 * some tree items which may use other tree items or tokens. E.g. a plus operator needs to change, but an identifier
 * can stay the same.
 */
public interface InstructionLangTreeLeaf {
}
